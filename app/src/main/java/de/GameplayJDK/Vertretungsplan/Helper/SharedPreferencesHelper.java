/*
 *     Vertretungsplan Android App
 *     Copyright (C) 2017  GameplayJDK
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.GameplayJDK.Vertretungsplan.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by GameplayJDK on 11.01.2017.
 */

public final class SharedPreferencesHelper {

    private static final String SHARED_PREFERENCES_KEY = "Vertretungsplan.SharedPreferencesHelper";

    public static final String PREF_SELECTED_LIST = "pref_selected_list";
    public static final String PREF_TIMESTAMP_MS = "pref_timestamp_ms";
    public static final String PREF_SELECTED_TAB = "pref_selected_tab";

    private SharedPreferencesHelper() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public static void set(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void set(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void set(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void set(Context context, String key, float value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static void set(Context context, String key, long value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void set(Context context, String key, Set<String> value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static String get(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        return sharedPreferences.getString(key, defaultValue);
    }

    public static int get(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        return sharedPreferences.getInt(key, defaultValue);
    }

    public static boolean get(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static float get(Context context, String key, float defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static long get(Context context, String key, long defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        return sharedPreferences.getLong(key, defaultValue);
    }

    public static Set<String> get(Context context, String key, Set<String> defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        return sharedPreferences.getStringSet(key, defaultValue);
    }
}
