package in.ac.siesgst.npl.remotecontrolbot;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vipul on 21/03/18.
 */

public class SharedPreferenceManager {
    public static final String PREF_NAME = "TML";
    public static final int PRIVATE_MODE = 0;
    public static final String LOG_TAG = SharedPreferenceManager.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public SharedPreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createIpSession(String ipAddr) {
        editor.putString("IP", ipAddr);
        editor.putBoolean("IP_SET", true);
        editor.apply();
    }

    public void closeIpSession() {
        editor.putBoolean("IP_SET", false);
        editor.apply();
    }

    public void defaultControls() {
        editor.putInt("rr", 1);
        editor.putInt("fr", 2);
        editor.putInt("ff", 3);
        editor.putInt("fl", 4);
        editor.putInt("ll", 5);
        editor.putInt("bl", 6);
        editor.putInt("bb", 7);
        editor.putInt("br", 8);
        editor.putInt("resetmotor", 9);
        editor.apply();
    }

    public void firstRun() {
        editor.putBoolean("firstrun", false);
        editor.apply();
    }

    public boolean isFirstRun() {
        return sharedPreferences.getBoolean("firstrun", true);
    }

    public String getIp() {
        return sharedPreferences.getString("IP", "IP_ADDRESS");
    }

    public void changeAllControls(int rr, int fr, int ff, int fl, int ll, int bl, int bb, int br, int resetmotor) {
        editor.putInt("rr", rr);
        editor.putInt("fr", fr);
        editor.putInt("ff", ff);
        editor.putInt("fl", fl);
        editor.putInt("ll", ll);
        editor.putInt("bl", bl);
        editor.putInt("bb", bb);
        editor.putInt("br", br);
        editor.putInt("resetmotor", resetmotor);
        editor.apply();
    }

    boolean isIpThere() {
        return sharedPreferences.getBoolean("IP_SET", false);
    }

    int getForward() {
        return sharedPreferences.getInt("ff", 0);
    }

    int getBackward() {
        return sharedPreferences.getInt("bb", 0);
    }

    int getLeft() {
        return sharedPreferences.getInt("ll", 0);
    }

    int getRight() {
        return sharedPreferences.getInt("rr", 0);
    }

    int getSoftLeftB() {
        return sharedPreferences.getInt("bl", 0);
    }

    int getSoftRightB() {
        return sharedPreferences.getInt("br", 0);
    }

    int getSoftLeftF() {
        return sharedPreferences.getInt("fl", 0);
    }

    int getSoftRightF() {
        return sharedPreferences.getInt("fr", 0);
    }

    int getMotorReset() {
        return sharedPreferences.getInt("resetmotor", 0);
    }
}
