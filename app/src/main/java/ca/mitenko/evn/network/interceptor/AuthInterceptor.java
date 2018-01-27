package ca.mitenko.evn.network.interceptor;

import java.io.IOException;

import ca.mitenko.evn.network.ApiConstants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mitenko on 2017-04-22.
 */

public class AuthInterceptor implements Interceptor {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        requestBuilder.addHeader(ApiConstants.API_KEY_NAME, ApiConstants.API_KEY_VALUE);

        request = requestBuilder.build();
        return chain.proceed(request);
    }
}
