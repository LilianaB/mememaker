package com.teamtreehouse.mememaker;

import android.preference.PreferenceManager;

import com.teamtreehouse.mememaker.utils.FileUtilities;

/**
 * Created by Evan Anger on 7/28/14.
 */
public class MemeMakerApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //saving assets file as storageType
        FileUtilities.saveAssetImage(this, "dogmess.jpg");
        FileUtilities.saveAssetImage(this, "excitedcat.jpg");
        FileUtilities.saveAssetImage(this, "guiltypup.jpg");

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false); // false to not modify value set by user
    }
}
