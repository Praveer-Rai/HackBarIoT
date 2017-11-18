package hackbar.de.hackbardroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hackbar.de.hackbardroid.settings.UserSettings;

public class LoginActivity extends Activity {

    private EditText usernameText;

    private UserSettings userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.usernameText);
        userSettings = new UserSettings(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // when we arrive here, delete the user (from prev login)
        userSettings.setUserId(null);
    }

    public void loginClicked(View view) {
        String username = usernameText.getText().toString();

        if (!login(username)) {
            Toast.makeText(this, "Dude! You just had one fucking job...", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean login(String username) {
        if (!username.isEmpty()) {
            // save login data
            userSettings.setUserId(username);

            // navigate to main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
