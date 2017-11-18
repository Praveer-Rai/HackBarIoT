package hackbar.de.hackbardroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import hackbar.de.hackbardroid.model.User;
import hackbar.de.hackbardroid.service.INerdBarService;
import hackbar.de.hackbardroid.service.NerdBarService;
import hackbar.de.hackbardroid.settings.UserSettings;
import hackbar.de.hackbardroid.utils.DeviceUtils;
import hackbar.de.hackbardroid.utils.NfcUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private INerdBarService nerdBarService;

    private UserSettings userSettings;

    private FloatingActionButton findGlassButton;
    private FloatingActionButton newOrderButton;

    private ViewGroup layoutUnpaired;
    private ViewGroup layoutPaired;

    private Handler handler;

    private User currentUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findGlassButton = (FloatingActionButton)findViewById(R.id.findGlassButton);
        newOrderButton = (FloatingActionButton)findViewById(R.id.newOrderButton);
        layoutUnpaired = (ViewGroup) findViewById(R.id.layoutUnpaired);
        layoutPaired = (ViewGroup) findViewById(R.id.layoutPaired);

        userSettings = new UserSettings(getApplicationContext());

        if (!NfcUtils.checkNfcEnabled(this)) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }

        if (!checkLoggedIn())
            return;

        updateViewState();

        nerdBarService = NerdBarService.getInstance();

        handleIntent(getIntent());
    }

    private void updateViewState() {
        if (userSettings.getConnectedTagIdKey() != null) {
            findGlassButton.setVisibility(View.VISIBLE);
            newOrderButton.setVisibility(View.VISIBLE);
            layoutUnpaired.setVisibility(View.GONE);
            layoutPaired.setVisibility(View.VISIBLE);
        } else {
            findGlassButton.setVisibility(View.GONE);
            newOrderButton.setVisibility(View.GONE);
            layoutUnpaired.setVisibility(View.VISIBLE);
            layoutPaired.setVisibility(View.GONE);
        }
    }

    private void updateUserData(User user) {
        currentUserData = user;
    }

    private boolean checkLoggedIn() {

        if (userSettings.getUserId() == null) {
            navigateToLogin();
            return false;
        }
        return true;
    }

    private void logout() {
        String userId = userSettings.getUserId();
        Call<Void> logoutCall = nerdBarService.logout(userId);
        logoutCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    navigateToLogin();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Sorry, couldn't log you out!", Toast.LENGTH_SHORT)
                    .show();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void navigateToOrders() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    private final Runnable intervalRunner = new Runnable() {
        @Override
        public void run() {
            // update user status here via REST call
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        NfcUtils.setupForegroundDispatch(this);

        handler = new Handler();
        handler.postDelayed(intervalRunner, 10000);
    }

    @Override
    protected void onPause() {
        NfcUtils.stopForegroundDispatch(this);

        super.onPause();

        handler.removeCallbacks(intervalRunner);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        final String tagId = NfcUtils.getTagId(intent);

        if (tagId != null) {
            DeviceUtils.vibrate(this);

            Call<User> logoutCall = nerdBarService.register(userSettings.getUserId(), tagId);
            logoutCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User userData = response.body();
                        userSettings.setConnectedTagIdKey(tagId);
                        updateViewState();
                        updateUserData(userData);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Sorry, couldn't pair!", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void findGlassClicked(View view) {

    }

    public void serviceClicked(View view) {

    }

    public void newOrderClicked(View view) {
        navigateToOrders();
    }
}
