package com.example.billsplitter.processes.imageProcessing;

import java.io.File;
import java.util.Locale;

import android.os.SystemClock;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;
public class ImageScraper
{
    private static final String TAG = "ImageScraper";

    private final TessBaseAPI tessApi = new TessBaseAPI();

    private boolean tessInit;

    private volatile boolean stopped;

    private volatile boolean tessProcessing;

    private volatile boolean recycleAfterProcessing;

    private final Object recycleLock = new Object();

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
    public void recognizeImage( File imagePath) {
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
        stopped = false;

        // Start process in another thread
        new Thread(() -> {
            tessApi.setImage(imagePath);
            // Or set it as Bitmap, Pix,...
            // tessApi.setImage(imageBitmap);

            // Set page segmentation mode (default is PSM_SINGLE_BLOCK)
            tessApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);

            long startTime = SystemClock.uptimeMillis();

            // Use getHOCRText(0) method to trigger recognition with progress notifications and
            // ability to cancel ongoing processing.
            tessApi.getHOCRText(0);

            // At this point the recognition has completed (or was interrupted by calling stop())
            // and we can get the results we want. In this case just normal UTF8 text.
            //
            // Note that calling only this method (without the getHOCRText() above) would also
            // trigger the recognition and return the same result, but we would received no progress
            // notifications and we wouldn't be able to stop() the ongoing recognition.
            String text = tessApi.getUTF8Text();

            // Alternatively we can get resulting text filtered based on confidence threshold.
            // Note this call internally calls getResultIterator() which means we need to call
            // getHOCRText or getUTF8Text method before to make sure there are results to process.
            //String text = tessApi.getConfidentText(40, TessBaseAPI.PageIteratorLevel.RIL_WORD);

            // We can free up the recognition results and any stored image data in the tessApi
            // if we don't need them anymore.
            tessApi.clear();

            // Publish the results
            Log.d(TAG, "recognizeImage: " + text);
            long duration = SystemClock.uptimeMillis() - startTime;
            Log.d(TAG, String.format(Locale.ENGLISH, "recognizeImage: Completed in %.3fs.", (duration / 1000f)));

            synchronized (recycleLock) {
                tessProcessing = false;

                // Recycle the instance here if the view model is already destroyed
                if (recycleAfterProcessing) {
                    tessApi.recycle();
                }
            }
        }).start();
    }
}
