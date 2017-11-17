package hackbar.de.hackbardroid;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

import hackbar.de.hackbardroid.utils.NfcUtils;

public class MainActivity extends Activity {

    public static final String TAG = "NfcDemo";

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show();
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

        NfcUtils.setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        NfcUtils.stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        // TODO: handle Intent
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (type.equals(NfcUtils.MIME_TEXT_PLAIN)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new RFIDReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = NfcA.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new RFIDReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    private class RFIDReaderTask extends AsyncTask<Tag, Void, Long> {

        @Override
        protected Long doInBackground(Tag... params) {
            Tag tag = params[0];
            byte[] idData = tag.getId();

            StringBuilder sb = new StringBuilder();
            for (byte i : idData) {
                sb.append(String.format(Locale.ENGLISH, "%d", i + 128));
            }
            return Long.valueOf(sb.toString());
        }

        @Override
        protected void onPostExecute(Long result) {
            if (result != null) {
                Toast.makeText(MainActivity.this, "ID: " + result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
