package hackbar.de.hackbardroid.utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class DeviceUtils {

    private static final long DEFAULT_DURATION = 500;

    private DeviceUtils() { }

    public static void vibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            v.vibrate(VibrationEffect.createOneShot(
                    DEFAULT_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(DEFAULT_DURATION);
        }


    }
}
