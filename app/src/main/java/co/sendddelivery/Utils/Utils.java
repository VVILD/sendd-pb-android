package co.sendddelivery.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by harshkaranpuria on 7/24/15.
 */
public class Utils extends Activity {

    SharedPreferences sharedpreferences;
    Context context;

    public Utils(Context c) {
        context =c;
        sharedpreferences = context.getSharedPreferences("Registered", this.MODE_PRIVATE);
        sharedpreferences= PreferenceManager.getDefaultSharedPreferences(c); // here you get your prefrences by either of two methods

    }

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public boolean isSynced() {
        int check = sharedpreferences.getInt("Synced", 0);
        if (check == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean setvalue(String keyname,String value){
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putString(keyname, value);
        editor.apply();
        return true;
    }

    public boolean setvalueLong(String keyname,Long value){
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putLong(keyname,value);
        editor.apply();
        return true;
    }

    public String getvalue(String keyname){
        return sharedpreferences.getString(keyname, "");
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
}