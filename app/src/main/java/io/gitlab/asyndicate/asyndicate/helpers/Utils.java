package io.gitlab.asyndicate.asyndicate.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Utils {
    private Context mContext;

    public static boolean putFileContents(String file, byte[] contents) {
        return putFileContents(file, contents, false);
    }

    public static boolean putFileContents(String file, byte[] contents, boolean append) {

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(contents);
            outputStream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getFileContents(String path) {
        String contents = "";
        File formFile = new File(path);
        if (!formFile.exists()) {
            return null;
        }
        try {
            Scanner fileReader = new Scanner(formFile);
            while (fileReader.hasNext()) {
                contents += fileReader.nextLine();
            }
            return contents;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Utils(Context context) {
        this.mContext = context;
    }

    public boolean checkNetwork() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static String getHash(String string) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");

            m.reset();
            m.update(string.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void excecute(PayloadRunnable worker, PayloadRunnable after) {
        new BackGroundTask(mContext, after).execute(worker);
    }

    public void LoadImage(String path, int width, PayloadRunnable runnable) {
        new LoadImage(mContext, path, width, runnable).execute();
    }

    public void  LoadImage(int res, int width, PayloadRunnable runnable) {
        Log.d("Image", "attempting to decode image");
        new LoadImage(mContext, res, width, runnable).execute();
    }

    public class BackGroundTask extends AsyncTask<PayloadRunnable, Void, Object> {
        private final PayloadRunnable afterRunable;
        private Context mContext;
        private Object result;

        public BackGroundTask(Context context, PayloadRunnable after) {
            this.mContext = context;
            this.afterRunable = after;
        }

        @Override
        protected Void doInBackground(PayloadRunnable... runnables) {
            for (PayloadRunnable runnable : runnables) {
                runnable.run(null);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... locations) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (afterRunable != null) {
                afterRunable.run(result);
            }
        }
    }

    public class LoadImage extends AsyncTask<Void, Void, Object> {

        private int requestWidth;
        private int imageResource;
        private String imagePath;
        private Context mContext;
        private Bitmap bitmap;
        private PayloadRunnable afterRunable;

        public LoadImage(Context context, int resource, int requestWidth, PayloadRunnable afterRubbale) {
            this.mContext = context;
            this.imageResource = resource;
            this.requestWidth = requestWidth;
            this.afterRunable = afterRubbale;
        }

        public LoadImage(Context context, String path, int requestWidth, PayloadRunnable afterRubbale) {
            this.mContext = context;
            this.imagePath = path;
            this.requestWidth = requestWidth;
            this.afterRunable = afterRubbale;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                int w = requestWidth;
                int h = 0;
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inJustDecodeBounds = true;
                if (imageResource != 0) {
                    BitmapFactory.decodeResource(mContext.getResources(), imageResource, op);
                } else {
                    BitmapFactory.decodeFile(imagePath, op);
                }

                if (w != 0) {
                    h = w * op.outHeight / op.outWidth;
                } else if (h != 0) {
                    w = h * op.outWidth / op.outHeight;
                } else {
                    w = 1;
                    h = 1;
                }
                op.inSampleSize = Math.max(op.outWidth / w, op.outHeight / h);
                op.inJustDecodeBounds = false;

                if (imageResource != 0) {
                    bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResource, op);
                } else {
                    bitmap = BitmapFactory.decodeFile(imagePath, op);
                }
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (afterRunable != null && bitmap != null) {
                Log.d("Image", "Image decoded running  afterCallback");
                afterRunable.run(bitmap);
            } else {
                Log.d("Image", "Invalid image");
            }
        }
    }
}