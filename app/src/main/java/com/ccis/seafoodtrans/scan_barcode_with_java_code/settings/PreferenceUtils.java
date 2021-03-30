package com.ccis.seafoodtrans.scan_barcode_with_java_code.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.preference.PreferenceManager;

import androidx.annotation.StringRes;

import com.google.android.gms.common.images.Size;
import com.google.mlkit.vision.barcode.Barcode;
import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.CameraSizePair;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.GraphicOverlay;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/** Utility class to retrieve shared preferences.  */
    public final class PreferenceUtils {

        public static final boolean isAutoSearchEnabled(@NotNull Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return getBooleanPref(context, R.string.pref_key_enable_auto_search, true);
        }

        public static final boolean isMultipleObjectsMode(@NotNull Context context) {
            return getBooleanPref(context, R.string.pref_key_object_detector_enable_multiple_objects, false);
        }

        public static final boolean isClassificationEnabled(@NotNull Context context) {
            return getBooleanPref(context, R.string.pref_key_object_detector_enable_classification, false);
        }

        public static final void saveStringPreference(@NotNull Context context, @StringRes int prefKeyId, @Nullable String value) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getString(prefKeyId), value).apply();
        }

        public static final int getConfirmationTimeMs(@NotNull Context context) {
            return isMultipleObjectsMode(context) ? 300 : (isAutoSearchEnabled(context) ? getIntPref(context,  R.string.pref_key_confirmation_time_in_auto_search, 1500) : getIntPref(context, R.string.pref_key_confirmation_time_in_manual_search, 500));
        }

        public static final float getProgressToMeetBarcodeSizeRequirement(@NotNull GraphicOverlay overlay, @NotNull Barcode barcode) {
            Context context = overlay.getContext();
            float var10000;
            if (getBooleanPref(context, R.string.pref_key_enable_barcode_size_check, false)) {
                float reticleBoxWidth = getBarcodeReticleBox(overlay).width();
                float barcodeWidth = overlay.translateX(barcode.getBoundingBox() != null ? (float)barcode.getBoundingBox().width() : 0.0F);
                float requiredWidth = reticleBoxWidth * (float)getIntPref(context, R.string.pref_key_minimum_barcode_width, 50) / (float)100;
                var10000 = RangesKt.coerceAtMost(barcodeWidth / requiredWidth, 1.0F);
            } else {
                var10000 = 1.0F;
            }
            return var10000;
        }

        @NotNull
        public static final RectF getBarcodeReticleBox(@NotNull GraphicOverlay overlay) {
            Context context = overlay.getContext();
            float overlayWidth = (float)overlay.getWidth();
            float overlayHeight = (float)overlay.getHeight();
            float boxWidth = overlayWidth * (float)getIntPref(context, R.string.pref_key_barcode_reticle_width, 80) / (float)100;
            float boxHeight = overlayHeight * (float)getIntPref(context,  R.string.pref_key_barcode_reticle_height, 35) / (float)100;
            float cx = overlayWidth / (float)2;
            float cy = overlayHeight / (float)2;
            return new RectF(cx - boxWidth / (float)2, cy - boxHeight / (float)2, cx + boxWidth / (float)2, cy + boxHeight / (float)2);
        }

        public static final boolean shouldDelayLoadingBarcodeResult(@NotNull Context context) {
            return getBooleanPref(context, R.string.pref_key_delay_loading_barcode_result, true);
        }

        private static final int getIntPref(Context context, @StringRes int prefKeyId, int defaultValue) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String prefKey =  context.getString(prefKeyId);;
            return sharedPreferences.getInt(prefKey, defaultValue);
        }

        @Nullable
        public static final CameraSizePair getUserSpecifiedPreviewSize(@NotNull Context context) {
            CameraSizePair var2;
            try {
                String previewSizePrefKey  = context.getString(R.string.pref_key_rear_camera_preview_size);

                String pictureSizePrefKey =  context.getString(R.string.pref_key_rear_camera_picture_size);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                var2 = new CameraSizePair(Size.parseSize(sharedPreferences.getString(previewSizePrefKey, null)), Size.parseSize(sharedPreferences.getString(pictureSizePrefKey, null)));
            } catch (Exception var5) {
                var2 = null;
            }

            return var2;
        }

        private static final boolean getBooleanPref(Context context, @StringRes int prefKeyId, boolean defaultValue) {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(prefKeyId), defaultValue);
        }

        private PreferenceUtils() {
        }
    }

