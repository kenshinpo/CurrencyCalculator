package com.pochih.currencycalculator.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.MalformedInputException;

/**
 * Created by PoChih on 2017/12/23.
 */

public class ImageHelper {
    public static int calcInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeBitmap(String url, int maxWidth) {

        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = calcInSampleSize(options, maxWidth, maxWidth);
            InputStream is = (InputStream) new URL(url).getContent();
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (MalformedInputException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
