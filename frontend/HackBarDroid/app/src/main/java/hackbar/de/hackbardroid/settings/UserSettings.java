package hackbar.de.hackbardroid.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {
    private SharedPreferences prefs;

    private static final String USER_ID_KEY = "user-id";

    public UserSettings(Context appContext) {
        prefs = appContext.getSharedPreferences("hackbar.de.hackbardroid.settings.user",
                Context.MODE_PRIVATE);
    }

    public String getUserId() {
        return prefs.getString(USER_ID_KEY, null);
    }

    public void setUserId(String userId) {
        prefs.edit().putString(USER_ID_KEY, userId).apply();
    }
}
