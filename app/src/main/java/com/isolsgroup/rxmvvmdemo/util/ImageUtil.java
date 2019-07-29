package com.isolsgroup.rxmvvmdemo.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.isolsgroup.rxmvvmdemo.model.PhotoResizeResult;
import com.isolsgroup.rxmvvmdemo.model.PhotoSize;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.inject.Singleton;

/**
 * Created by Ashutosh Verma on 22-Apr-17.
 */

@Singleton
public class ImageUtil {
    public PhotoSize getPhotoSize(String photoPath){
        PhotoSize photoSize= new PhotoSize();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try{
            BitmapFactory.decodeStream(new FileInputStream(new File(photoPath)), null, options);
            photoSize.width= options.outWidth;
            photoSize.height= options.outHeight;
            return photoSize;

        }catch (Exception e){ Log.e("Error", e.getMessage()); return null; }
    }

    public String mediaBasePath(Context context){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+File.separator+Utils.getAppName(context)+File.separator+"Media");
        if (!mediaStorageDir.exists()) { mediaStorageDir.mkdirs(); }
        return mediaStorageDir.getAbsolutePath();
    }

    public Uri savePicture(Context context, byte[] data, int rotation, boolean flip) {
        File mediaFile = new File(mediaBasePath(context) + File.separator + "IMG_" + Utils.getTimeStamp() + ".jpg");
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data.length);
            if (rotation != 0 || flip) {
                Bitmap oldBitmap = bitmap;
                Matrix matrix = new Matrix();
                if(flip) {
                    matrix.preScale(-1, 1);
                    matrix.postRotate(-rotation);
                }else { matrix.postRotate(rotation); }

                bitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false);
                oldBitmap.recycle();
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            FileOutputStream stream = new FileOutputStream(mediaFile);
            stream.write(out.toByteArray());
            stream.close();

        }catch (Exception e){ Log.e("Error", e.getMessage()); }

        if(mediaFile.exists()) { Log.e("Photo Size: ",""+Integer.parseInt(String.valueOf(mediaFile.length()/1024))); }

        Uri fileContentUri = Uri.fromFile(mediaFile);
        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileContentUri);
        context.sendBroadcast(mediaScannerIntent);

        return fileContentUri;
    }

    public String copyFile(Context context, String filePath){
        File newFile=null;
        try{
            File oldFile= new File(filePath);
            if(oldFile.exists()){
                newFile = new File(mediaBasePath(context) + File.separator + "IMG_" + Utils.getTimeStamp() + ".jpg");
                FileUtils.copyFile(oldFile, newFile);

            }else return filePath;
        }catch (Exception e){ return filePath; }
        return newFile.getAbsolutePath();
    }

    public PhotoResizeResult getBitmapFromFile(String filePath, int reqSize) {

        PhotoResizeResult result= new PhotoResizeResult();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(new FileInputStream(new File(filePath)), null, options);
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = calculateInSampleSize(options, reqSize);
            result.bitmap= BitmapFactory.decodeStream(new FileInputStream(new File(filePath)), null, o2);
            result.width= o2.outWidth; result.height= o2.outHeight;

            Log.e("photoSize", "width-"+result.width+" height-"+result.height);
            return result;
        } catch(FileNotFoundException e) { Log.e("Error", e.getMessage()); }
        return null;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqSize) {
        int inSampleSize = 1;
        final int width = options.outWidth;

        for (int i = 1; ; i++){
            int mid= ((reqSize*i)+(reqSize*(i+1)))/2;
            if(width<mid){
                inSampleSize= i+1;
                break;
            }
        }
        return inSampleSize;
    }

    // Not Required with Fresco
    public void checkOrientation(String photoPath) {
        int rotate = 0;
        File file= new File(photoPath);
        if(file.exists()) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                Log.e("orientation: ", orientation + "");
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }
                Bitmap oldBitmap = bitmap;
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);

                OutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
                fOut.flush();
                fOut.close();

            } catch(Exception e) {}
        }
    }

    // FRESCO
    public void displayImage(SimpleDraweeView simpleDraweeView, String url, int placeHolder){
        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setPlaceholderImage(placeHolder);
        hierarchy.setFailureImage(placeHolder);
        loadImage(simpleDraweeView, Uri.parse(url));
    }

    public void displayImageCenterCrop(SimpleDraweeView simpleDraweeView, String url, int placeHolder){
        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setPlaceholderImage(placeHolder);
        hierarchy.setFailureImage(placeHolder, ScalingUtils.ScaleType.CENTER_CROP);
        loadImage(simpleDraweeView, Uri.parse(url));
    }

    public void displayImageCenterCrop(SimpleDraweeView simpleDraweeView, File file){
        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        loadImage(simpleDraweeView, Uri.fromFile(file));
    }

    public void displayImageCenterCrop(SimpleDraweeView simpleDraweeView, File file, int placeHolder){
        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setPlaceholderImage(placeHolder);
        hierarchy.setFailureImage(placeHolder, ScalingUtils.ScaleType.CENTER_CROP);
        loadImage(simpleDraweeView, Uri.fromFile(file));
    }


    public void displayImageCenterCropCircle(SimpleDraweeView simpleDraweeView, String url, int placeHolder){
        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setPlaceholderImage(placeHolder);
        hierarchy.setFailureImage(placeHolder, ScalingUtils.ScaleType.CENTER_CROP);

        RoundingParams roundingParams=RoundingParams.asCircle();
        hierarchy.setRoundingParams(roundingParams);
        loadImage(simpleDraweeView, Uri.parse(url));
    }

    public void displayImageCenterCropCircle(SimpleDraweeView simpleDraweeView, File file, int placeHolder){
        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setPlaceholderImage(placeHolder);
        hierarchy.setFailureImage(placeHolder, ScalingUtils.ScaleType.CENTER_CROP);

        RoundingParams roundingParams=RoundingParams.asCircle();
        hierarchy.setRoundingParams(roundingParams);
        loadImage(simpleDraweeView, Uri.fromFile(file));
    }

    public void displayImageCenterCropCircle(SimpleDraweeView simpleDraweeView, int resId, int placeHolder){
        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setPlaceholderImage(placeHolder);
        hierarchy.setFailureImage(placeHolder, ScalingUtils.ScaleType.CENTER_CROP);

        RoundingParams roundingParams=RoundingParams.asCircle();
        hierarchy.setRoundingParams(roundingParams);
        loadImageResource(simpleDraweeView, resId);
    }



    public void displayImageCenterCrop(SimpleDraweeView simpleDraweeView, String url, int placeHolder, int round){
        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setPlaceholderImage(placeHolder);
        hierarchy.setFailureImage(placeHolder, ScalingUtils.ScaleType.CENTER_CROP);

        RoundingParams roundingParams=RoundingParams.fromCornersRadius(round);
        hierarchy.setRoundingParams(roundingParams);
        loadImage(simpleDraweeView, Uri.parse(url));
    }


    public void displayImageCenterInside(SimpleDraweeView simpleDraweeView, String url, int placeHolder){
        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        hierarchy.setPlaceholderImage(placeHolder);
        hierarchy.setFailureImage(placeHolder, ScalingUtils.ScaleType.FIT_CENTER);
        loadImage(simpleDraweeView, Uri.parse(url));
    }



    public void displayImageCenterInsideNoCache(SimpleDraweeView simpleDraweeView, File file, int placeHolder){
        Uri uri=Uri.fromFile(file);
        ImagePipeline imagePipeline=Fresco.getImagePipeline();
        imagePipeline.evictFromCache(uri);

        GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
        hierarchy.setPlaceholderImage(placeHolder);
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        hierarchy.setFailureImage(placeHolder, ScalingUtils.ScaleType.FIT_CENTER);
        loadImage(simpleDraweeView, uri);
    }


    public void displayGalleryImages(SimpleDraweeView simpleDraweeView, File file, int placeHolder){
        Uri uri=Uri.fromFile(file);
        ImagePipeline imagePipeline=Fresco.getImagePipeline();
        imagePipeline.evictFromCache(uri);

        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        hierarchy.setPlaceholderImage(placeHolder);
        hierarchy.setFailureImage(placeHolder, ScalingUtils.ScaleType.CENTER_CROP);
        loadImageResized(simpleDraweeView, uri, 150);
    }





    private void loadImage(SimpleDraweeView simpleDraweeView, Uri uri){
        ImageRequest request=ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .setRotationOptions(RotationOptions.autoRotate())
                .build();
        DraweeController controller=Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(simpleDraweeView.getController()).build();
        simpleDraweeView.setController(controller);
    }

    private void loadImageResource(SimpleDraweeView simpleDraweeView, int resId){
        ImageRequest request=ImageRequestBuilder
                .newBuilderWithResourceId(resId)
                .setProgressiveRenderingEnabled(true)
                .setRotationOptions(RotationOptions.autoRotate())
                .build();
        DraweeController controller=Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(simpleDraweeView.getController()).build();
        simpleDraweeView.setController(controller);
    }


    private void loadImageResized(SimpleDraweeView simpleDraweeView, Uri uri, int size){
        ImageRequest request=ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(size, size))
                .setProgressiveRenderingEnabled(true)
                .setRotationOptions(RotationOptions.autoRotate())
                .build();
        DraweeController controller=Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(simpleDraweeView.getController()).build();
        simpleDraweeView.setController(controller);
    }



    public void displayImageResource(SimpleDraweeView simpleDraweeView, int resource){
        GenericDraweeHierarchy hierarchy=simpleDraweeView.getHierarchy();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);

        ImageRequest request=ImageRequestBuilder
                .newBuilderWithResourceId(resource)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller=Fresco.newDraweeControllerBuilder().setImageRequest(request).setOldController(simpleDraweeView.getController()).build();
        simpleDraweeView.setController(controller);
    }



}
