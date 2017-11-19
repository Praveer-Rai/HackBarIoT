package hackbar.de.hackbardroid.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.annotation.Nullable;

import java.util.Locale;

public class NfcUtils {

    private static final String MIME_TEXT_PLAIN = "text/plain";

    private NfcUtils() {}

    public static boolean checkNfcEnabled(Context context) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (nfcAdapter == null)
            return false;

        return nfcAdapter.isEnabled();
    }

    public static void setupForegroundDispatch(final Activity activity) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[3][1];
        techList[0][0] = "android.nfc.tech.NdefFormatable";
        techList[1][0] = "android.nfc.tech.NfcA";
        techList[2][0] = "android.nfc.tech.MifareClassic";

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        filters[0].addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(activity);
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList); //filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity) {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(activity);
        adapter.disableForegroundDispatch(activity);
    }

    private static String parseTag(Tag tag) {
        byte[] idData = tag.getId();

        StringBuilder sb = new StringBuilder();
        for (byte i : idData) {
            sb.append(String.format(Locale.ENGLISH, "%d", i + 128));
        }
        return sb.toString();
    }

    @Nullable
    public static String getTagId(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            return parseTag(tag);
        }
        return null;
    }
}
