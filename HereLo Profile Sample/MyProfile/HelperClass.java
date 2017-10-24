package com.tv.herelo.MyProfile;

import com.tv.herelo.tab.settings.SettingsScreen;

/**
 * Created by user on 17/7/17.
 */

public class HelperClass {

    private static String TabOneFragmentOnScreen = "";
    public static SettingsScreen getTabOneFragmentOnScreen;

    public static String getTabOneFragmentOnScreen() {
        return TabOneFragmentOnScreen;
    }

    public static void setTabOneFragmentOnScreen(String tabOneFragmentOnScreen) {
        TabOneFragmentOnScreen = tabOneFragmentOnScreen;
    }
    /*   public static String getTabOneFragmentOnScreen () {
        return TabOneFragmentOnScreen ;
    }
    public static void setTabOneFragmentOnScreen(
            String TabOneFragmentOnScreen) {
        HelperClass.TabOneFragmentOnScreen = TabOneFragmentOnScreen;
    }*/
}

