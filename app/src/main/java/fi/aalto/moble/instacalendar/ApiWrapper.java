package fi.aalto.moble.instacalendar;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by mohamed on 11/15/15.
 */
public class ApiWrapper {

    private static InstaCalAPI api = null;

    public static InstaCalAPI getApi() {
        if(api != null)
            return api;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://130.23.42.176:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(InstaCalAPI.class);
        return api;
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preferences_file_name), Context.MODE_PRIVATE);
        return sharedPref.getString("login_token", "");
    }

    public static void setToken(Context context, String token){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preferences_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("login_token", token);
        editor.commit();
    }
}
