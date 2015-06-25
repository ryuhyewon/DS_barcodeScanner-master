package com.example.bera.ds_barcodescanner.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by bera on 2015-05-31.
 */
public class FileLoader {
    public static String loadRawFile(Context context, int id) {
        InputStream inputStream = context.getResources().openRawResource(id);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            // e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
