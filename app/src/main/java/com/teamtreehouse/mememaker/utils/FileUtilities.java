package com.teamtreehouse.mememaker.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.teamtreehouse.mememaker.MemeMakerApplicationSettings;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Evan Anger on 7/28/14.
 */
public class FileUtilities {

    public static void saveAssetImage(Context context, String assetName) {
        File fileDirectory = context.getFilesDir();
        File fileToWrite = new File(fileDirectory, assetName);//give us file into file directory named assetName

        AssetManager assetManager = context.getAssets();
        try {
            InputStream in = assetManager.open(assetName); // input for reading file
            FileOutputStream out = new FileOutputStream(fileToWrite);
            copyFile(in, out);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //FileOutputStream out2 = context.openFileOutput(fileToWrite.getAbsolutePath(), context.MODE_PRIVATE);//alternative
        //second parameter make file readable writable outside app
    }

    //standard method to copy files
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

   public static File[] listFiles(Context context) {
        File fileDirectory = context.getFilesDir(); // only for jpg
        File[] filteredFiles = fileDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.getAbsolutePath().contains(".jpg")) {
                    return true;
                }
                return false;
            }
        });

        return filteredFiles;

    }

    public static Uri saveImageForSharing(Context context, Bitmap bitmap,  String assetName) {
        File fileToWrite = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), assetName);

        try {
            FileOutputStream outputStream = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return Uri.fromFile(fileToWrite);
        }
    }


    public static void saveImage(Context context, Bitmap bitmap, String name) {
        File fileDirectory = context.getFilesDir();
        File fileToWrite = new File(fileDirectory, name);

        try {
            FileOutputStream outputStream = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
