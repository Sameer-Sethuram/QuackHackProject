package com.example.billsplitter.processes.imageProcessing;

import java.io.File;
import java.util.Locale;

import android.os.SystemClock;
import android.util.Log;

import com.example.billsplitter.entities.Item;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;

public class ImageScraper
{
    private static final String TAG = "ImageScraper";

    private final TessBaseAPI tessApi = new TessBaseAPI();

    private boolean tessInit;

    private volatile boolean tessProcessing;

    private final Object recycleLock = new Object();

    //Central stuff
    public ImageScraper()
    {
        if (!OpenCVLoader.initDebug()) {
            Log.e("OpenCV", "ImageScraper: Unable to load OpenCV!");
        }
        else {
            Log.d("OpenCV", "ImageScraper: OpenCV loaded Successfully!");
        }
    }

    public Item[] getBillItemsFromImage(File imagePath, File IntermediaryPath)
    {
        if (!tessInit) {
            Log.e(TAG, "ImageScraper: Tesseract is not initialized!");
            return null;
        }
        File processedImage = preProcess(imagePath, IntermediaryPath);
        recognizeImage(processedImage);
        return null;
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
    private void recognizeImage(File imagePath) {
        if (!tessInit) {
            Log.e(TAG, "recognizeImage: Tesseract is not initialized");
            return;
        }
        if (tessProcessing) {
            Log.e(TAG, "recognizeImage: Processing is in progress");
            return;
        }
        tessProcessing = true;

        Log.d(TAG, "recognizeImage: Processing Started");

        // Start process in another thread
        new Thread(() -> {
            tessApi.setImage(imagePath);
            // Or set it as Bitmap, Pix,...
            // tessApi.setImage(imageBitmap);

            // Set page segmentation mode (default is PSM_SINGLE_BLOCK)
            tessApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_COLUMN);

            long startTime = SystemClock.uptimeMillis();

            tessApi.getHOCRText(0);
            String text = tessApi.getUTF8Text();
            tessApi.clear();

            // Publish the results
            Log.d(TAG, "recognizeImage: " + text);
            long duration = SystemClock.uptimeMillis() - startTime;
            Log.d(TAG, String.format(Locale.ENGLISH, "recognizeImage: Completed in %.3fs.", (duration / 1000f)));

            synchronized (recycleLock) {
                tessProcessing = false;
                tessApi.recycle();
            }
        }).start();
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

        Mat imgAdaptiveThreshold = new Mat();
        Imgproc.adaptiveThreshold(imgGaussianBlur, imgAdaptiveThreshold, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C ,Imgproc.THRESH_BINARY, 99, 4);
        Imgcodecs.imwrite(String.format("%s/AdaptiveThreshold.png", intermediateDirectory), imgAdaptiveThreshold);

        File preProcessedImageFile = new File(intermediateDirectory, "AdaptiveThreshold.png");
        Log.i(TAG, String.format("preProcess: final preprocess image written to %s", preProcessedImageFile.getAbsolutePath()));

        return preProcessedImageFile;
    }
}
