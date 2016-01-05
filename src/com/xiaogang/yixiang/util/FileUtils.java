package com.xiaogang.yixiang.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    /**
     * 获取保存图片的目录
     *
     * @return
     */
    public static File getAlbumDir() {
        File dir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getAlbumName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
    /**
     * 获取保存 隐患检查的图片文件夹名称
     *
     * @return
     */
    public static String getAlbumName() {
        return "sheguantong";
    }

    //压缩bitmap
    public static Bitmap resize_img(Bitmap bitmap, float pc) {

        Matrix matrix = new Matrix();
        Log.i("mylog2", "缩放比例--" + pc);
        matrix.postScale(pc, pc); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        bitmap.recycle();
        bitmap = null;
        System.gc();

        int width = resizeBmp.getWidth();
        int height = resizeBmp.getHeight();
        Log.i("mylog2", "按比例缩小后宽度--" + width);
        Log.i("mylog2", "按比例缩小后高度--" + height);

        return resizeBmp;
    }

    //将压缩的bitmap保存到sdcard卡临时文件夹img_interim，用于上传
    @SuppressLint("SdCardPath")
    public static File saveMyBitmap(String filename, Bitmap bit) {
        File dir = new File("/sdcard/img_interim/");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File f = new File("/sdcard/img_interim/" + filename);
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            f = null;
            e1.printStackTrace();
        }

        return f;
    }

    /**
     * 保存图片到指定的目录
     *
     * @param bit
     * @param fileName 文件名
     * @return
     */
    public static String saveBitToSD(Bitmap bit, String fileName) {
        if (bit == null || bit.isRecycled()) return "";

        File file = new File(Environment.getExternalStorageDirectory(), "/uniapp/photoCache");
        File dirFile = new File(file.getAbsolutePath());
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File pathFile = new File(dirFile, fileName);
        if (pathFile.exists()) {
            return pathFile.getAbsolutePath();
        } else {
            ImageUtils.Bitmap2File(bit, pathFile.getAbsolutePath());
            return pathFile.getAbsolutePath();
        }
    }


    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }



    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }
}
