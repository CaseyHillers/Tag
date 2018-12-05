package com.github.jovenpableo.friendtag.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;

import com.github.jovenpableo.friendtag.MapActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    public String uuid;

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(1000);
            con.setConnectTimeout(1000);
            con.setRequestMethod("GET");
            con.connect();

            InputStream is = con.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(is);
            return  image;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap == null) {
            return;
        }

        int squareBitmapWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());

        Bitmap dstBitmap = Bitmap.createBitmap(
                squareBitmapWidth, // Width
                squareBitmapWidth, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        Canvas canvas = new Canvas(dstBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
        RectF rectF = new RectF(rect);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        float left = (squareBitmapWidth-bitmap.getWidth())/2;
        float top = (squareBitmapWidth-bitmap.getHeight())/2;
        canvas.drawBitmap(bitmap, left, top, paint);
        bitmap.recycle();

        MapActivity.renderAfter(dstBitmap, uuid);
    }
}
