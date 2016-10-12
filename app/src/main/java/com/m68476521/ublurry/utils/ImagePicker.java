package com.m68476521.ublurry.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.m68476521.ublurry.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to ask for an image from storage or camera.
 */
public class ImagePicker {
    private static final String TEMP_IMAGE_NAME = "tempImage";

    /**
     * Private constructor.
     */
    private ImagePicker() {

    }

    public static
    @Nullable
    Intent getPickImageIntent(@NonNull Context context) {
        Intent chooserIntent = null;

        List<Intent> intents = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        addIntentsToList(context, intents, pickIntent);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("return-data", true);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(context)));

        addIntentsToList(context, intents, cameraIntent);

        if (!intents.isEmpty()) {
            chooserIntent = Intent.createChooser(intents.remove(intents.size() - 1),
                    context.getString(R.string.dlg_img_picker));

            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }


    private static List<Intent> addIntentsToList(@NonNull Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }

        return list;
    }

    public static Uri getUriFromResult(@NonNull Context context, int resultCode, Intent imageReturnedIntent) {
        File imageFile = getTempFile(context);

        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage;

            boolean isCamera = (imageReturnedIntent == null || imageReturnedIntent.getData() == null);

            if (isCamera) {
                selectedImage = Uri.fromFile(imageFile);
            } else {
                selectedImage = imageReturnedIntent.getData();
            }

            return selectedImage;
        }
        return null;
    }

    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
        //noinspection ResultOfMethodCallIgnored
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }
}
