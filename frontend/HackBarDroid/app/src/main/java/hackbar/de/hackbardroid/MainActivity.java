package hackbar.de.hackbardroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

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
    private FloatingActionButton serviceButton;

    private TextView salutationText;
    private TextView userHintText;
    private TextView numberOfSips;
    private TextView numberOfDrinks;
    private TextView drinkTempLabel;
    private TextView drinkTemp;

    private ViewGroup layoutUnpaired;
    private ViewGroup layoutPaired;

    private Handler handler;

    private User currentUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findGlassButton = (FloatingActionButton) findViewById(R.id.findGlassButton);
        newOrderButton = (FloatingActionButton) findViewById(R.id.newOrderButton);
        serviceButton = (FloatingActionButton) findViewById(R.id.serviceButton);
        layoutUnpaired = (ViewGroup) findViewById(R.id.layoutUnpaired);
        layoutPaired = (ViewGroup) findViewById(R.id.layoutPaired);
        salutationText = (TextView) findViewById(R.id.salutationText);
        userHintText = (TextView) findViewById(R.id.userHintText);
        numberOfDrinks = (TextView) findViewById(R.id.numberOfDrinks);
        numberOfSips = (TextView) findViewById(R.id.numberOfSips);
        drinkTempLabel = (TextView) findViewById(R.id.drinkTempLabel);
        drinkTemp = (TextView) findViewById(R.id.drinkTemp);

        nerdBarService = NerdBarService.getInstance();
        userSettings = new UserSettings(getApplicationContext());

        if (!NfcUtils.checkNfcEnabled(this)) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }

        if (!checkLoggedIn())
            return;

        updateViewState();

        handleIntent(getIntent());
    }

    private boolean updateViewState() {
        if (userSettings.getConnectedTagIdKey() != null) {
            findGlassButton.setVisibility(View.VISIBLE);
            newOrderButton.setVisibility(View.VISIBLE);
            layoutUnpaired.setVisibility(View.GONE);
            layoutPaired.setVisibility(View.VISIBLE);
            return true;
        } else {
            findGlassButton.setVisibility(View.GONE);
            newOrderButton.setVisibility(View.GONE);
            layoutUnpaired.setVisibility(View.VISIBLE);
            layoutPaired.setVisibility(View.GONE);
            return false;
        }
    }

    private void updateUserData(User user) {
        currentUserData = user;

        findGlassButton.setAlpha(user.getFindMyDrink() ? 0.66f : 1.0f);
        serviceButton.setAlpha(user.getNeedAssistance() ? 0.66f : 1.0f);
        serviceButton.setEnabled(!user.getNeedAssistance());

        updateSalutationText(user);
        updateUserHintText(user);
        updateCounters(user);
        updateDrinkTemp(user);
    }

    private void updateSalutationText(User user) {
        String name = user.getUserId();
        String drink = user.getCurrentDrink();

        String text;
        if (drink == null) {
            text = String.format(Locale.ENGLISH, "Hey %s!", name);
        } else {
            text = String.format(Locale.ENGLISH, "Hey %s,\nenjoy your %s!", name, drink);
        }

        salutationText.setText(text);
    }

    private void updateUserHintText(User user) {
        String drink = user.getCurrentDrink();

        if (drink != null) {
            String text = String.format(Locale.ENGLISH,
                    "The perfect temperature of your drink\nis between %d to %d °C.",
                    user.getMinTemp(),
                    user.getMaxTemp());
            userHintText.setText(text);
        } else {
            userHintText.setText("How about grabbing a drink?");
        }
    }

    private void updateCounters(User user) {
        numberOfSips.setText(user.getSipCount().toString());
        numberOfDrinks.setText(user.getDrinkCount().toString());
    }

    private void updateDrinkTemp(User user) {
        String drink = user.getCurrentDrink();
        Integer temp = user.getCurrentTemp();
        if (drink != null && temp != null) {
            drinkTemp.setText(temp + " °C");
            drinkTemp.setVisibility(View.VISIBLE);
            drinkTempLabel.setVisibility(View.VISIBLE);
        } else {
            drinkTemp.setVisibility(View.GONE);
            drinkTempLabel.setVisibility(View.GONE);
        }
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
        Call<User> logoutCall = nerdBarService.logout(userId);
        logoutCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    String tag = userSettings.getConnectedTagIdKey();

                    if (tag != null) {
                        int drinks = currentUserData.getDrinkCount();
                        String msg;
                        if (drinks >= 3) {
                            msg = String.format(Locale.ENGLISH,
                                    "You had %d drinks. Please consider to take a cap! Get home safe \uD83D\uDE42",
                                    drinks);
                        } else {
                            msg = "Hope you enjoyed your stay! \uD83D\uDE09";
                        }

                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }

                    navigateToLogin();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Sorry, couldn't log you out!", Toast.LENGTH_SHORT)
                    .show();
            }
        });
    }

    private void resetSipCounter(String userId) {
        Call<User> logoutCall = nerdBarService.resetSipCount(userId);
        logoutCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    updateUserData(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Sorry, couldn't do that!", Toast.LENGTH_SHORT)
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
            requestUserUpdate();

            handler.postDelayed(intervalRunner, 5000);
        }
    };

    private void requestUserUpdate() {
        Call<User> findCall = nerdBarService.getUser(userSettings.getUserId());
        findCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    updateUserData(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("", t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        NfcUtils.setupForegroundDispatch(this);

        handler = new Handler();
        handler.post(intervalRunner);
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
            case R.id.action_reset_sip_counter:
                resetSipCounter(userSettings.getUserId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void findGlassClicked(View view) {
        Call<User> findCall = nerdBarService.findMyDrink(userSettings.getUserId());
        findCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    updateUserData(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void serviceClicked(View view) {
        Call<User> findCall = nerdBarService.needAssistance(userSettings.getUserId());
        findCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    updateUserData(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("", t.getMessage());
            }
        });
    }

    public void newOrderClicked(View view) {
        navigateToOrders();
    }
}
