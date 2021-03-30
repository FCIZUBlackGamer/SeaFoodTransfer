package com.ccis.seafoodtrans.scan_barcode_with_java_code;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.exifinterface.media.ExifInterface;

import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.CameraSizePair;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public final class Utils {
    public static final float ASPECT_RATIO_TOLERANCE = 0.01F;
    public static final int REQUEST_CODE_PHOTO_LIBRARY = 1;
    private static final String TAG = "Utils";
    public static final Utils INSTANCE;

    private Utils() {
    }

    static {
        INSTANCE = new Utils();
    }

    public final void requestRuntimePermissions(@NotNull Activity activity) {
        String[] allNeededPermissions = this.getRequiredPermissions((Context)activity);
        Arrays.stream(allNeededPermissions).filter(it->checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        );

        if (allNeededPermissions.length!=0) {
            ActivityCompat.requestPermissions(activity, allNeededPermissions, 0);
        }

    }

    public final boolean allPermissionsGranted(@NotNull Context context) {
        boolean checkAllPermissionGranted = false;
       for (String requestPermission : getRequiredPermissions(context)) {

            if (checkSelfPermission(context, requestPermission) == PackageManager.PERMISSION_GRANTED ) {
                checkAllPermissionGranted = true;
            }
            else {
                checkAllPermissionGranted = false;
                break;
            }

        }
        return checkAllPermissionGranted;
    }

    private final String[] getRequiredPermissions(Context context) {
        String[] var2;
        try {
            String[] var10000;

                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),  PackageManager.GET_PERMISSIONS);
                String[] ps = info.requestedPermissions;
                if (ps != null) {

                    if (ps.length != 0) {
                        return ps;
                    }
                }
                var10000 = new String[0];

            var2 = var10000;
        } catch (Exception var8) {
            var2 = new String[0];
        }

        return var2;
    }

    public static final boolean isPortraitMode(@NotNull Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Generates a list of acceptable preview sizes. Preview sizes are not acceptable if there is not
     * a corresponding picture size of the same aspect ratio. If there is a corresponding picture size
     * of the same aspect ratio, the picture size is paired up with the preview size.
     *
     *
     * This is necessary because even if we don't use still pictures, the still picture size must
     * be set to a size that is the same aspect ratio as the preview size we choose. Otherwise, the
     * preview images may be distorted on some devices.
     */
    @NotNull
    public static final List<CameraSizePair> generateValidPreviewSizeList(@NotNull Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        ArrayList<CameraSizePair> validPreviewSizes = new ArrayList();
        // By looping through the picture sizes in order, we favor the higher resolutions.
        // We choose the highest resolution in order to support taking the full resolution
        // picture later.
            for(Camera.Size previewSize: supportedPreviewSizes) {
                float previewAspectRatio = (float)previewSize.width / (float)previewSize.height;

                for(Camera.Size pictureSize: supportedPictureSizes) {
                    float pictureAspectRatio = (float)pictureSize.width / (float)pictureSize.height;
                    float var12 = previewAspectRatio - pictureAspectRatio;
                    if (Math.abs(var12) < ASPECT_RATIO_TOLERANCE) {
                        validPreviewSizes.add(new CameraSizePair(previewSize, pictureSize));
                        break;
                    }
                }
            }
        // If there are no picture sizes with the same aspect ratio as any preview sizes, allow all of
        // the preview sizes and hope that the camera can handle it.  Probably unlikely, but we still
        // account for it.
            if (validPreviewSizes.isEmpty()) {
                Log.w("Utils", "No preview sizes have a corresponding same-aspect-ratio picture size.");

                for(Camera.Size previewSize: supportedPreviewSizes) {
                    validPreviewSizes.add(new CameraSizePair(previewSize, null));
                }
            }

            return (List)validPreviewSizes;
        }


    @NotNull
    public final Bitmap getCornerRoundedBitmap(@NotNull Bitmap srcBitmap, int cornerRadius) {
        Bitmap dstBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0.0F, 0.0F, (float)srcBitmap.getWidth(), (float)srcBitmap.getHeight());
        canvas.drawRoundRect(rectF, (float)cornerRadius, (float)cornerRadius, paint);
        paint.setXfermode((Xfermode)(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)));
        canvas.drawBitmap(srcBitmap, 0.0F, 0.0F, paint);
        return dstBitmap;
    }
    /** Convert NV21 format byte buffer to bitmap. */
    @Nullable
    public static final Bitmap convertToBitmap(@NotNull ByteBuffer data, int width, int height, int rotationDegrees) {
        data.rewind();
        byte[] imageInBuffer = new byte[data.limit()];
        data.get(imageInBuffer, 0, imageInBuffer.length);

        try {
            YuvImage image = new YuvImage(imageInBuffer, 17, width, height, (int[])null);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, width, height), 80, (OutputStream)stream);
            Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
            stream.close();
            Matrix matrix = new Matrix();
            matrix.postRotate((float)rotationDegrees);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        } catch (Exception var10) {
            Log.e(TAG, "Error: " + var10.getMessage());
            return null;
        }
    }

    public final void openImagePicker(@NotNull Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, REQUEST_CODE_PHOTO_LIBRARY);
    }

    @Nullable
    public final Bitmap loadImage(@NotNull Context context, @NotNull Uri imageUri, int maxImageDimension) throws IOException {
        InputStream inputStreamForSize = null;
        InputStream inputStreamForImage = null;

        try {
            inputStreamForSize = context.getContentResolver().openInputStream(imageUri);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStreamForSize, (Rect)null, opts);/* outPadding= */
            int inSampleSize = Math.max(opts.outWidth / maxImageDimension, opts.outHeight / maxImageDimension);
            opts = new BitmapFactory.Options();
            opts.inSampleSize = inSampleSize;
            inputStreamForImage = context.getContentResolver().openInputStream(imageUri);
            Bitmap decodedBitmap = BitmapFactory.decodeStream(inputStreamForImage, (Rect)null, opts);
            return this.maybeTransformBitmap(context.getContentResolver(), imageUri, decodedBitmap);
        } finally {
            if (inputStreamForSize != null) {
                inputStreamForSize.close();
            }

            if (inputStreamForImage != null) {
                inputStreamForImage.close();
            }

        }

    }

    private final Bitmap maybeTransformBitmap(ContentResolver resolver, Uri uri, Bitmap bitmap) {
        Matrix matrix;

        switch(this.getExifOrientationTag(resolver, uri)) {
            case ExifInterface.ORIENTATION_UNDEFINED:
            case ExifInterface.ORIENTATION_NORMAL:
                matrix = null;
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix = new Matrix();
                matrix. postScale(-1.0f, 1.0f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix = new Matrix();
                matrix.postRotate(90f) ;
                break;
            case   ExifInterface.ORIENTATION_TRANSPOSE :
                matrix = new Matrix();
                matrix.postScale(-1.0f, 1.0f);
                break;
            case  ExifInterface.ORIENTATION_ROTATE_180:
                matrix = new Matrix();
                matrix.postRotate(180.0f);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL :
                matrix = new Matrix();
                matrix.postScale(1.0f, -1.0f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270 :
                matrix = new Matrix();
                matrix.postRotate(-90.0F);

                break;
            case  ExifInterface.ORIENTATION_TRANSVERSE:
                matrix = new Matrix();
                matrix.postRotate(-90.0F);
                matrix.postScale(-1.0F, 1.0F);
                break;
            default:
                // Set the matrix to be null to skip the image transform.
                matrix = null;
        }
        if (matrix != null) {
           return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
           return bitmap;
        }
    }

    private final int getExifOrientationTag(ContentResolver resolver, Uri imageUri) {
        if (ContentResolver.SCHEME_CONTENT != imageUri.getScheme() && ContentResolver.SCHEME_FILE != imageUri.getScheme()) {
            return 0;
        } else {
            ExifInterface exif = null;

            try {
                InputStream var10000 = resolver.openInputStream(imageUri);
                if (resolver.openInputStream(imageUri) != null) {
                    try {
                        exif = new ExifInterface(resolver.openInputStream(imageUri));
                    } catch (Throwable e) {
                        throw e;
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to open file to read rotation meta data: " + imageUri,e);
            }
            if (exif != null) {

                return exif.getAttributeInt("Orientation", 1);
            } else {

                return  ExifInterface.ORIENTATION_UNDEFINED;
            }

        }
    }


}
