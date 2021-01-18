package fr.umlv.test_confroid.utils;

import android.content.Context;

import java.util.List;
import java.util.function.Consumer;

public interface ConfroidUtils {

    void saveConfiguration(Context context, String name, Object value, String versionName);
    <T> void loadConfiguration(Context context, String name, String version, Consumer<T> callback);
    <T> void subscribeConfiguration(Context context, String name, Consumer<T> callback);
    <T> void cancelConfigurationSubscription(Context context, Consumer<T> callback);
    void getConfigurationVersions(Context context, String name, Consumer<List<Version>> callback);
}
