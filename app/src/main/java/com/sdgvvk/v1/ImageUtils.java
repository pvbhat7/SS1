package com.sdgvvk.v1;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {

    public static boolean downloadAndSaveImage(Context context, String imageUrl, String fileName) {
        try {
            Log.e("ImageUtils", "ImageUtils imageUrl: " + imageUrl);
            Log.e("ImageUtils", "ImageUtils fileName: " + fileName);
            // Create a URL object from the provided imageUrl
            URL url = new URL(imageUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            // Create an InputStream to read the image data
            InputStream inputStream = connection.getInputStream();

            // Create a directory if it doesn't exist
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyImages");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create a file to save the image
            File file = new File(directory, fileName);

            // Create an OutputStream to write the image data to the file
            OutputStream outputStream = new FileOutputStream(file);

            // Read the image data from the input stream and write it to the file
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the input and output streams
            inputStream.close();
            outputStream.close();

            // Add the image to the MediaStore (for gallery access)
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Log.e("ImageUtils", "ImageUtils image saved here "+directory);
            return true;
        } catch (Exception e) {
            Log.e("ImageUtils", "ImageUtils Error downloading and saving image: " + e.toString());
            return false;
        }
    }

    public static boolean deleteSavedImage(Context context, String imagePath) {
        try {
            // Delete the image file
            File file = new File(imagePath);
            if (file.exists() && file.delete()) {
                // Remove the image from the MediaStore (gallery)
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String selection = MediaStore.Images.Media.DATA + "=?";
                String[] selectionArgs = {imagePath};
                contentResolver.delete(uri, selection, selectionArgs);
                return true;
            } else {
                return false; // File doesn't exist or couldn't be deleted
            }
        } catch (Exception e) {
            Log.e("ImageUtils", "Error deleting image: " + e.getMessage());
            return false;
        }
    }
}

