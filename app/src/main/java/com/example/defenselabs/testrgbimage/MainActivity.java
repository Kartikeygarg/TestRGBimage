package com.example.defenselabs.testrgbimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public int width =640  , height=480;
    ImageView img;
    static String Image_URL = "http://apps1.defenselabs.org/iff/images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
        new DownloadImageTask((ImageView) findViewById(R.id.img))
                .execute(Image_URL + "sukesh1.rgb");
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urldisplay = strings[0];
            Bitmap imageBitmap = null;
            try {
               InputStream is = new URL(urldisplay).openStream();
                int bufferSize = width*height*3;

               /* ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

                // this is storage overwritten on each iteration with bytes

                byte[] buffer = new byte[bufferSize];

                // we need to know how may bytes were read to write them to the byteBuffer
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }*/



                byte[] colors = IOUtils.toByteArray(is);

            //    imageBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
              int nrOfPixels = bufferSize / 3; // Three bytes per pixel.
                int pixels[] = new int[nrOfPixels];
                long millis = System.currentTimeMillis();
                for(int i = 0; i < nrOfPixels; i++) {
                    int r = (int)(0xFF & colors[3*i]);
                    int g = (int)(0xFF & colors[3*i+1]);
                    int b = (int)(0xFF & colors[3*i+2]);
                    pixels[i] = Color.rgb(r,g,b);
                }
                long diff = System.currentTimeMillis()- millis;
                System.out.print("DIFF "+ diff );
                imageBitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_4444);

                /*YuvImage yuvImage = new YuvImage(colors, ImageFormat.NV21, width, height, null);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, os);
                byte[] jpegByteArray = os.toByteArray();
                imageBitmap  = BitmapFactory.decodeByteArray(jpegByteArray, 0, jpegByteArray.length);*/
               // long diff = System.currentTimeMillis()- millis;
                    Log.i("DIFF2 ",""+ diff );

                is.close();

            }catch (Exception e) {
                Log.i("ERROR",""+e.getMessage());

                e.printStackTrace();
            }
            return imageBitmap;
        }


        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}
