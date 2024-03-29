package net.inqer.autosearch.util;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class TokenInjectionInterceptor implements Interceptor {
    private static final String TAG = "TokenInjectionIntercept";

    private String sessionToken = "";

    @Inject
    TokenInjectionInterceptor() {
    }

    public void setSessionToken(String sessionToken) {
        Log.d(TAG, "setSessionToken: setting - " + sessionToken);
        this.sessionToken = sessionToken;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder requestBuilder = request.newBuilder();

        if (request.header(Config.NO_AUTHENTICATION_COOKIE) == null) {
            Log.d(TAG, "intercept: getting token...");
            // get token
            if (sessionToken == null) {
                throw new RuntimeException("Session token should be defined for auth APIs");
            } else {
                requestBuilder.addHeader("Authorization", "Token " + sessionToken);
            }
        }

        return chain.proceed(requestBuilder.build());
    }
}
