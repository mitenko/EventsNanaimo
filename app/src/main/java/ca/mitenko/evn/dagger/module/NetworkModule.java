package ca.mitenko.evn.dagger.module;

import android.app.Application;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import javax.inject.Singleton;

import ca.mitenko.evn.BuildConfig;
import ca.mitenko.evn.network.ApiConstants;
import ca.mitenko.evn.network.interceptor.AuthInterceptor;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mitenko on 2017-04-22.
 */

@Module
public class NetworkModule {
    /**
     * Provides OkHttp cache
     * @param application
     * @return
     */
    @Provides
    @Singleton
    Cache providesOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(Cache cache) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // Network debugging
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            builder.addNetworkInterceptor(new StethoInterceptor());
            builder.addInterceptor(logging);
        }
        builder.cache(cache);
        return builder.build();
    }

    /**
     * Provides Retrofit
     * @param okHttpClient
     * @param authInterceptor
     * @param gson
     * @return
     */
    @Singleton
    @Provides
    Retrofit providesRetrofit(OkHttpClient okHttpClient,
                              AuthInterceptor authInterceptor,
                              Gson gson) {
        String baseUrl = ApiConstants.URL;
        OkHttpClient.Builder builder = okHttpClient.newBuilder();

        // Interceptors
        builder.addInterceptor(authInterceptor);
       // builder.addInterceptor(new JsonDataInterceptor());

        // Build
        okHttpClient = builder.build();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

}
