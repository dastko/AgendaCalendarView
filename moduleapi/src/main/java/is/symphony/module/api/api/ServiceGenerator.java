package is.symphony.module.api.api;

import android.content.Context;
import android.support.annotation.NonNull;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dastko on 10/24/16.
 */

public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient;
    private final Class serviceClass;
    private final String url;
    private final Context context;
    protected final Boolean isConnectionPoolEnabled, isTimeoutConfigured, isLoggingEnabled, isInterceptorEnabled;
    protected final Integer maxConnection, keepAliveConnection, readTimeout, connectTimeout;

    private static Retrofit.Builder builder =
            new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

    private ServiceGenerator(ServiceGeneratorBuilder builder) {
        this.serviceClass = builder.getServiceClass();
        this.url = builder.getUrl();
        this.context = builder.getContext();
        this.isConnectionPoolEnabled = builder.getConnectionPoolEnabled();
        this.isTimeoutConfigured = builder.getTimeoutConfigured();
        this.isLoggingEnabled = builder.getLoggingEnabled();
        this.maxConnection = builder.getMaxConnection();
        this.keepAliveConnection = builder.getKeepAliveConnection();
        this.readTimeout = builder.getReadTimeout();
        this.connectTimeout = builder.getConnectTimeout();
        this.isInterceptorEnabled = builder.getInterceptorEnabled();
    }

    public static class ServiceGeneratorBuilder {

        private final Class serviceClass;
        private final String url;
        private final Context context;
        private Boolean isConnectionPoolEnabled, isTimeoutConfigured, isLoggingEnabled, isInterceptorEnabled;
        private Integer maxConnection, keepAliveConnection, readTimeout, connectTimeout;


        public ServiceGeneratorBuilder(final Class serviceClass, String url, Context context) {
            this.serviceClass = serviceClass;
            this.url = url;
            this.context = context;
        }

        public ServiceGeneratorBuilder setTimeout(Boolean isTimeoutConfigured, Integer connectTimeout, Integer readTimeout) {
            this.isTimeoutConfigured = isTimeoutConfigured;
            this.connectTimeout = connectTimeout;
            this.readTimeout = readTimeout;
            return this;
        }

        public ServiceGeneratorBuilder setConnectionPool(Boolean isConnectionPoolEnabled, Integer maxConnection, Integer keepAliveConnection) {
            this.isConnectionPoolEnabled = isConnectionPoolEnabled;
            this.maxConnection = maxConnection;
            this.keepAliveConnection = keepAliveConnection;
            return this;
        }

        public ServiceGeneratorBuilder setLogging(Boolean isLoggingEnabled) {
            this.isLoggingEnabled = isLoggingEnabled;
            return this;
        }

        public ServiceGeneratorBuilder setInterceptor(Boolean isInterceptorEnabled) {
            this.isInterceptorEnabled = isInterceptorEnabled;
            return this;
        }


        public <S> S build() {
            ServiceGenerator serviceGenerator = new ServiceGenerator(this);
            if (serviceGenerator.connectTimeout > 180 || serviceGenerator.connectTimeout < 0) {
                throw new IllegalStateException("Age out of range");
            }
            return createService(serviceGenerator);
        }

        @SuppressWarnings("unchecked")
        private Class getServiceClass() {
            return serviceClass;
        }

        private String getUrl() {
            return url;
        }

        private Context getContext() {
            return context;
        }

        private Boolean getConnectionPoolEnabled() {
            return isConnectionPoolEnabled;
        }

        private Boolean getLoggingEnabled() {
            return isLoggingEnabled;
        }

        private Boolean getTimeoutConfigured() {
            return isTimeoutConfigured;
        }

        private Integer getMaxConnection() {
            return maxConnection;
        }

        private Integer getKeepAliveConnection() {
            return keepAliveConnection;
        }

        private Integer getReadTimeout() {
            return readTimeout;
        }

        private Integer getConnectTimeout() {
            return connectTimeout;
        }

        private Boolean getInterceptorEnabled() {
            return isInterceptorEnabled;
        }
    }


    private static <S> S createService(ServiceGenerator serviceGenerator) {
        Class<S> apiInterface = serviceGenerator.serviceClass;
        Retrofit retrofit = getRetrofitInstance(serviceGenerator);
        return retrofit.create(apiInterface);

    }

    @NonNull
    private static Retrofit getRetrofitInstance(ServiceGenerator serviceGenerator) {
        httpClient = new OkHttpClient.Builder();

        setUpInterceptors(serviceGenerator.context, serviceGenerator.isInterceptorEnabled);
        setUpConnectionPool(serviceGenerator.isConnectionPoolEnabled, serviceGenerator.maxConnection, serviceGenerator.keepAliveConnection);
        loggingEnabled(serviceGenerator.isLoggingEnabled);
        setUpTimeout(serviceGenerator.readTimeout, serviceGenerator.connectTimeout, serviceGenerator.isTimeoutConfigured);

        return builder.baseUrl(serviceGenerator.url).client(httpClient.build()).build();
    }

    private static void setUpInterceptors(Context context, boolean isInterceptorsEnabled) {
        if (isInterceptorsEnabled) {
            CustomInterceptor unauthorizedInterceptor = new CustomInterceptor(context);
            CustomAuthorizationInterceptor authorizationInterceptor = new CustomAuthorizationInterceptor(context);
            httpClient.networkInterceptors().add(unauthorizedInterceptor);
            httpClient.authenticator(authorizationInterceptor);
        }
    }

    private static void setUpTimeout(int readTimeout, int connectTimeout, boolean isTimeoutConfigured) {
        if (isTimeoutConfigured) {
            httpClient.readTimeout(readTimeout, TimeUnit.SECONDS);
            httpClient.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        }
    }

    private static void loggingEnabled(boolean isLoggingEnabled) {
        if (isLoggingEnabled) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
    }

    private static void setUpConnectionPool(boolean isConnectionPoolEnabled, int maxConnection, int keepAliveConnection) {
        if (isConnectionPoolEnabled) {
            ConnectionPool connectionPool = new ConnectionPool(maxConnection, keepAliveConnection, TimeUnit.SECONDS);
            httpClient.connectionPool(connectionPool);
        }
    }

    private static Retrofit createServiceRx(String url, CustomInterceptor unauthorizedInterceptor,
                                           CustomAuthorizationInterceptor customAuthorizationInterceptor) {

        httpClient = new OkHttpClient.Builder();
        //if (BuildConfig.TEST_LOGIN)
        acceptingSelfSignedSSL(httpClient);
        //ConnectionPool pool = new ConnectionPool(5, 30, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        //httpClient.connectionPool(pool);
        httpClient.addInterceptor(logging);
        httpClient.networkInterceptors().add(unauthorizedInterceptor);
        httpClient.authenticator(customAuthorizationInterceptor);
        return builder.baseUrl(url)
                .client(httpClient.build()).build();
    }


    private static <S> S createLoginService(Class<S> serviceClass, String url) {
        httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        Retrofit restAdapter = builder.baseUrl(url).client(httpClient.build()).build();
        return restAdapter.create(serviceClass);
    }

    private static Retrofit retrofit() {
        OkHttpClient client = httpClient.build();
        return builder.client(client).build();
    }

    private static void acceptingSelfSignedSSL(OkHttpClient.Builder httpClients) {
        boolean allowUntrusted = true;
        SSLContext sslContext = null;

        if (allowUntrusted) {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] cArrr = new X509Certificate[0];
                    return cArrr;
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain,
                                               final String authType) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(final X509Certificate[] chain,
                                               final String authType) throws CertificateException {
                }
            }};

            try {
                sslContext = SSLContext.getInstance("SSL");

                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            httpClients.sslSocketFactory(sslContext.getSocketFactory());

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            httpClients.hostnameVerifier(hostnameVerifier);
        }
    }
}
