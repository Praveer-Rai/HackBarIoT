package hackbar.de.hackbardroid;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import hackbar.de.hackbardroid.settings.UserSettings;

public class LoginActivity extends Activity {

    private EditText usernameText;
    private VideoView backgroundVideo;

    private UserSettings userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.usernameText);
        usernameText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                loginClicked(textView);
                return true;
            }
        });
        backgroundVideo = findViewById(R.id.backgroundVideo);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.barteaser);

        backgroundVideo.setDrawingCacheEnabled(true);
        backgroundVideo.setVideoURI(uri);
        backgroundVideo.requestFocus();
        backgroundVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setVolume(0, 0);
            }
        });

        userSettings = new UserSettings(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        backgroundVideo.start();

        // when we arrive here, delete the user (from prev login)
        userSettings.setUserId(null);
        userSettings.setConnectedTagIdKey(null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        backgroundVideo.pause();
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
