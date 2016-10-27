package is.symphony.module.api.api;

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import is.symphony.module.api.database.ApplicationSettings;
import is.symphony.module.api.models.SharedPreferencesKey;
import is.symphony.module.api.models.StatusCode;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dastko on 10/24/16.
 */

public class CustomInterceptor implements Interceptor {

    private ApplicationSettings mApplicationSettings;
    private Request request;
    private Context mContext;
    private HashMap<String, String> headerProperties;

    public CustomInterceptor(Context context) {
        this.mApplicationSettings = new ApplicationSettings(context);
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        request = chain.request();
        Request.Builder builder = request.newBuilder();
        Request intercept = builder.build();

        setAuthorizationHeader(intercept, token());

        Response response = chain.proceed(intercept);
        if(response.code() == StatusCode.CODE_GONE_410){
            responseCode410();
        }

        return response;
    }

    private boolean checkHeaderProperties() {
        return !setHeaderAttributes().isEmpty() || setHeaderAttributes().size() < 1;
    }

    private Request setAuthorizationHeader(Request original, String token) {
        Request.Builder builder;
        if (checkHeaderProperties()) {
            builder = original.newBuilder();
            for (Map.Entry<String, String> entry : headerProperties.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
            return builder.method(request.method(), request.body()).build();
        } else {
            return original.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .method(request.method(), request.body()).build();
        }
    }

    private String token() {
        return mApplicationSettings.getDefaultId(SharedPreferencesKey.ACCESS_TOKEN);
    }

    private void responseCode410(){

    }

    private HashMap<String, String> setHeaderAttributes(){
        return null;

    }
}
