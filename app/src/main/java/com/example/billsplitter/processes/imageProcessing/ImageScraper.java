package com.example.billsplitter.processes.imageProcessing;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.SystemClock;
import android.util.Log;

import com.example.billsplitter.entities.Item;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;
import org.opencv.photo.Photo;

/*
    How To Use:

    Import:
    import com.example.billsplitter.processes.imageProcessing.*;
    import com.googlecode.tesseract.android.TessBaseAPI;

    Then Actual Code:
    Assets.extractAssets(getApplicationContext());
    ImageScraper ourScraper = new ImageScraper();
    ourScraper.initTesseract(Assets.getTessDataPath(getApplicationContext()),"eng", TessBaseAPI.OEM_LSTM_ONLY);
    ourScraper.getBillItemsFromImage(Assets.getImageFile(getApplicationContext(), "receipt3.jpg"), Assets.getIntermediaryProcessDir(getApplicationContext()), 0);

    Note:
    Calls above must be done in activity context.
    The "receipt3.jpg" can be replaced with the name of any image file as long as it is present in the app's file directory.
    getBillItemsFromImage is multi-threaded, therefore you must wait until ourScraper.billItems is initialized and then
    access the Items array with ourScraper.billItems as the thread will run independently.
 */

public class ImageScraper
{
    private static final String TAG = "ImageScraper";
    private static final Pattern REGEX = Pattern.compile("[0-9]+ [a-zA-Z]*.+ [$0-9]+[.][0-9]+");
    private static final Pattern SUBNAMEREGEX = Pattern.compile("(?<=\\d\\s)(.*?)(?=\\s*\\$)");
    private static final Pattern SUBCOSTREGEX = Pattern.compile("[0-9]+[.][0-9]+");

    private final TessBaseAPI tessApi = new TessBaseAPI();

    private boolean tessInit;

    private volatile boolean processing;
    public volatile Item[] billItems;

    private final Object processLock = new Object();

    //Central stuff
    public ImageScraper()
    {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "ImageScraper: Unable to load OpenCV!");
        }
        else {
            Log.d(TAG, "ImageScraper: OpenCV loaded Successfully!");
        }
    }

    public void getBillItemsFromImage(File imagePath, File IntermediaryPath, int billID)
    {
        if (!tessInit) {
            Log.e(TAG, "getBillItemsFromImage: Tesseract is not initialized!");
            return;
        }
        if (processing) {
            Log.e(TAG, "getBillItemsFromImage: Processing is in progress!");
            return;
        }
        processing = true;
        new Thread(() -> {
            long startTime = SystemClock.uptimeMillis();

            Log.i(TAG, "getBillItemsFromImage: Processing started");

            File processedImage = preProcess(imagePath, IntermediaryPath);
            String text = recognizeImage(processedImage);
            Item[] outputItems = getItemsFromStr(text, billID);

            long duration = SystemClock.uptimeMillis() - startTime;

            Log.i(TAG, String.format(Locale.ENGLISH, "getBillItemsFromImage: Processing completed in %.3fs.", (duration / 1000f)));

            synchronized(processLock)
            {
                billItems = outputItems;
                processing = false;
            }
        }).start();
        return;
    }

    //Tesseract OCR

    public void initTesseract(String dataPath, String language, int engineMode) {
        Log.i(TAG, "Initializing Tesseract with: dataPath = [" + dataPath + "], " +
                "language = [" + language + "], engineMode = [" + engineMode + "]");
        try {
            tessInit = tessApi.init(dataPath, language, engineMode);
        } catch (IllegalArgumentException e) {
            tessInit = false;
            Log.e(TAG, "Cannot initialize Tesseract:", e);
        }
    }
    private String recognizeImage(File imagePath) {
        if (!tessInit) {
            Log.e(TAG, "recognizeImage: Tesseract is not initialized");
            return null;
        }

        Log.i(TAG, "recognizeImage: Image interpretation started");

        tessApi.setImage(imagePath);
        // Or set it as Bitmap, Pix,...
        // tessApi.setImage(imageBitmap);

        // Set page segmentation mode (default is PSM_SINGLE_BLOCK)
        tessApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_COLUMN);

        tessApi.getHOCRText(0);
        String text = tessApi.getUTF8Text();
        tessApi.clear();

        Log.i(TAG, "recognizeImage: Image interpretation completed");
        Log.d(TAG, String.format(Locale.ENGLISH, "recognizeImage: Output: \n%s", text));

        return text;
    }

    //Pre Processing Stuff

    private File preProcess(File imagePath, File intermediateDirectory)
    {
        Log.i(TAG, "preProcess: Started preProcess");

        Mat img = new Mat();
        img = Imgcodecs.imread(imagePath.getAbsolutePath());
        Imgcodecs.imwrite(String.format("%s/trueImage.png", intermediateDirectory), img);

        Mat imgGray = new Mat();
        Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY);
        Imgcodecs.imwrite(String.format("%s/GreyImage.png", intermediateDirectory), imgGray);

        Mat imgGaussianBlur = new Mat();
        Imgproc.GaussianBlur(imgGray,imgGaussianBlur,new Size(3, 3),0);
        Imgcodecs.imwrite(String.format("%s/GaussianImage.png", intermediateDirectory), imgGaussianBlur);

        Mat imgDenoised = new Mat();
        Photo.fastNlMeansDenoising(imgGaussianBlur, imgDenoised, 30, 7, 21);
        Imgcodecs.imwrite(String.format("%s/DenoisedImage.png", intermediateDirectory), imgDenoised);

        File preProcessedImageFile = new File(intermediateDirectory, "DenoisedImage.png");
        Log.i(TAG, String.format("preProcess: final preprocess image written to %s", preProcessedImageFile.getAbsolutePath()));

        return preProcessedImageFile;
    }

    //Text Postprocessing

    private Item[] getItemsFromStr(String inputString, int billID)
    {
        Log.i(TAG, "getItemsFromStr: Text postprocessing started");
        Matcher matcher = REGEX.matcher(inputString);

        ArrayList<Item> currentItemList = new ArrayList<>();
        while (matcher.find()) {
            Matcher matchName = SUBNAMEREGEX.matcher(matcher.group());
            Matcher matchCost = SUBCOSTREGEX.matcher(matcher.group());

            if(!matchName.find() || !matchCost.find())
            {
                Log.e(TAG, String.format("getItemsFromStr: Failed to identify name or cost of item: %s", matcher.group()));
                continue;
            }

            Item curItem;
            try {
                curItem = new Item(billID, Double.parseDouble(matchCost.group()), matchName.group());
            } catch (Exception e) {
                Log.e(TAG, "getItemFromStr: Match caused exception when attempting to create item object!", e);
                continue;
            }
            Log.d(TAG, String.format("getItemsFromStr: New item found: [Name: %s, Cost: %,.2f]", curItem.displayName, curItem.amount));
            currentItemList.add(curItem);
        }
        Item[] returnArr = new Item[currentItemList.size()];
        returnArr = currentItemList.toArray(returnArr);

        Log.i(TAG, "getItemsFromStr: Text postprocessing completed");
        return returnArr;
    }
}
