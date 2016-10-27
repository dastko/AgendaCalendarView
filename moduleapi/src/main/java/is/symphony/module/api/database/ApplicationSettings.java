package is.symphony.module.api.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dastko on 10/24/16.
 */

public class ApplicationSettings {

    public static final String DEFAULT_KEY = "PREF_KEY";
    public static final String DEFAULT_VALUE = "PREF_VALUE";
    public static final String DefaultBoolean = "FIRST_RUN";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final Integer defaultInteger = -1;

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences secureSharedPrefs;
    private SharedPreferences.Editor securePrefsEditor;

    //TODO use user password instead of default value
    public ApplicationSettings(Context context) {
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.prefsEditor = sharedPrefs.edit();
    }

    /**
     * @param lists - we should save to the memory (shared preferences) - caching purposes.
     * @param tag   - list of objects should have unique ID (tag)
     * @param <T>   objects
     */
    public <T> void saveListGeneric(List<T> lists, String tag) {
        Gson gson = new Gson();
        String listInJson = gson.toJson(lists);
        prefsEditor.putString(tag, listInJson);
        prefsEditor.commit();
    }

    /**
     * We must provide an class TypeBase, because the parses can't fetch the TypeBase T at runtime.
     *
     * @param tag   - unique
     * @param clazz - for example - Client[].class
     * @param <T>
     * @return
     */
    public <T> List<T> loadListGeneric(String tag, final Class<T[]> clazz) {
        List<T> lists = null;
        Gson gson = new Gson();
        String listInJson = sharedPrefs.getString(tag, "");
        if (listInJson.isEmpty()) {
            lists = new ArrayList<>();
        } else {
            final T[] jsonToObject = gson.fromJson(listInJson, clazz);
            if(jsonToObject != null) {
                if (jsonToObject.length == 0) {
                    lists = new ArrayList<>();
                }else {
                    lists = Arrays.asList(jsonToObject);
                }
            }
        }
        return lists;
    }

    /**
     * Delete the list from the storage by Tag
     *
     * @param tag unique identifier
     * @return
     */
    public boolean deleteByTag(String tag) {
        return prefsEditor.remove(tag).commit();
    }

    public void putDefaultId(String type, String value) {
        prefsEditor.putString(type, value);
        prefsEditor.commit();
    }

    public void putDefaultSecure(String type, String value) {
        securePrefsEditor.putString(type, value);
        securePrefsEditor.commit();
    }

    public String getDefaultSecure(String type) {
        return secureSharedPrefs.getString(type, DEFAULT_VALUE);
    }

    public void putDefaultBoolean(boolean status) {
        prefsEditor.putBoolean(DefaultBoolean, status);
        prefsEditor.commit();
    }

    public void putDefaultInteger(String type, Integer value) {
        prefsEditor.putInt(type, value);
        prefsEditor.commit();
    }

    public Integer getDefaultInteger(String type) {
        return sharedPrefs.getInt(type, defaultInteger);
    }

    public boolean getDefaultBoolean() {
        return sharedPrefs.getBoolean(DefaultBoolean, false);
    }

    public String getDefaultId(String type) {
        return sharedPrefs.getString(type, DEFAULT_VALUE);
    }

    public String getDefaultId() {
        return sharedPrefs.getString(ACCESS_TOKEN, DEFAULT_VALUE);
    }

    public void putId(String value) {
        prefsEditor.putString(ApplicationSettings.DEFAULT_KEY, value);
        prefsEditor.commit();
    }

    public void deletePreferences() {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public void deletePreferencesSecure() {
        securePrefsEditor.clear();
        securePrefsEditor.commit();
    }


    public String getId(String type) {
        String id = sharedPrefs.getString(type, DEFAULT_VALUE);
        if (id.length() == 0)
            return getDefaultId(type);
        else
            return id;
    }

}
