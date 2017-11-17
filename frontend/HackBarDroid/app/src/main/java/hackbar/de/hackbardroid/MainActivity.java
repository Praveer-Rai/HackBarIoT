package hackbar.de.hackbardroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import hackbar.de.hackbardroid.utils.NfcUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!NfcUtils.checkNfcEnabled(this)) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

        NfcUtils.setupForegroundDispatch(this);
    }

    @Override
    protected void onPause() {
        NfcUtils.stopForegroundDispatch(this);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Long id = NfcUtils.getTagId(intent);

        if (id != null) {
            Toast.makeText(MainActivity.this, "ID: " + id, Toast.LENGTH_LONG).show();
        }
    }
}
