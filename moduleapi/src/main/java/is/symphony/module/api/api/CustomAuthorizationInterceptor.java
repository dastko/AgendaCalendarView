package is.symphony.module.api.api;

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import is.symphony.module.api.database.ApplicationSettings;
import is.symphony.module.api.models.SharedPreferencesKey;
import is.symphony.module.api.models.StatusCode;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by dastko on 10/24/16.
 */

    public class CustomAuthorizationInterceptor implements Authenticator {


    private static int mCounter = 1;
    private ApplicationSettings mApplicationSettings;
    private Context mContext;
    private HashMap<String, String> headerProperties;
    private Integer retryCounter = 1;
    private String newToken;


    /**
     * @param context Activity context
     */
    public CustomAuthorizationInterceptor(Context context) {
        this.mContext = context;
        this.mApplicationSettings = new ApplicationSettings(context);
    }

    /**
     * @param context
     * @param retryCounter number of retries for the authentication. (logout after)
     */
    public CustomAuthorizationInterceptor(Context context, Integer retryCounter) {
        this.mContext = context;
        this.mApplicationSettings = new ApplicationSettings(context);
        this.retryCounter = retryCounter;
    }


    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        final Request original = response.request();

        if (mCounter > retryCounter) {
            mCounter = 1;
            logout();
        } else {
            if (response.code() == StatusCode.CODE_UNAUTHORIZED_401) {
                newToken = refreshToken();
                mCounter++;
                return setAuthorizationHeader(original, newToken);
            } else {
                return setAuthorizationHeader(original, mApplicationSettings.getDefaultId(SharedPreferencesKey.ACCESS_TOKEN));
            }
        }

        return original;
    }

    private Request setAuthorizationHeader(Request original, String token) {
        Request.Builder builder;
        if (checkHeaderProperties()) {
            builder = original.newBuilder();
            for (Map.Entry<String, String> entry : setHeaderAttributes().entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
            return builder.build();
        } else {
            return original.newBuilder()
                    .addHeader("Authorization", "Bearer " + token).build();
        }
    }

    private boolean checkHeaderProperties() {
        return !setHeaderAttributes().isEmpty() || setHeaderAttributes().size() < 1;
    }

    /**
     * Refresh token API layer synchronize
     *
     * @return Fresh Token
     */
    public String refreshToken(){
        return null;
    }

    public void logout(){

    }

    public HashMap<String, String> setHeaderAttributes(){
        return null;
    }

}
