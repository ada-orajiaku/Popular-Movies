package com.example.android.popularmovies;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by adaobifrank on 4/16/17.
 */

public class Helper{
        public static String getMetaData(Context context, String name) {
            try {
                ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = ai.metaData;
                return bundle.getString(name);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Unable to load meta-data: " + e.getMessage());
            }
            return null;
        }

        private static final String TAG = "HELPER";
}
