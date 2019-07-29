package com.isolsgroup.rxmvvmdemo.di;

import android.content.Context;

import com.isolsgroup.rxmvvmdemo.BuildConfig;
import com.isolsgroup.rxmvvmdemo.data.remote.APIService;
import com.isolsgroup.rxmvvmdemo.util.Constants;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    private static final int CACHE_SIZE = 4;
    private static final int READ_TIMEOUT = 100 * 1000;
    private static final int CONNECTION_TIMEOUT = 100 * 1000;

    public NetworkModule() {
    }

    @Provides
    @Singleton
    APIService provideAPIService(Retrofit retrofit) {
        return retrofit.create(APIService.class);
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(OkHttpClient okHttpClient, RxJava2CallAdapterFactory rxJava2CallAdapterFactory, GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(Constants.API_ENDPOINT)
                .client(okHttpClient)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }


    @Singleton
    @Provides
    @Inject
    OkHttpClient getOkHttpClient(@AppContext Context context) {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

//        final String CERTIFICATE_DOMAIN = "www.trovo.in";
//        final String[] CERTIFICATE_SHA = {"sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA=", "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8=", "sha256/aWiOzygZ3nMzHD684iCIadyK3bSHs8ISVEPSd7pvk80="};
//        CertificatePinner.Builder certificatePinnerBuilder = new CertificatePinner.Builder();
//        for (String sha : CERTIFICATE_SHA) certificatePinnerBuilder.add(CERTIFICATE_DOMAIN, sha);

        int cacheSize = CACHE_SIZE * 1024 * 1024;

        return new OkHttpClient.Builder()
                .cache(new Cache(new File(context.getCacheDir(), "http"), cacheSize))
//                .certificatePinner(certificatePinnerBuilder.build())
                .retryOnConnectionFailure(false)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
//                .addInterceptor(chain -> {
//                    Request original = chain.request();
//                    Request request = original.newBuilder()
//                            .header("Content-type", "application/json")
//                            .header("Accept", "application/json;version=1")
//                            .method(original.method(), original.body())
//                            .build();
//
//                    return chain.proceed(request);
//                })
                .addInterceptor(logger)
                .build();
    }


    @Singleton
    @Provides
    RxJava2CallAdapterFactory providesRxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Singleton
    @Provides
    GsonConverterFactory providesGsonConverterFactory() {
        return GsonConverterFactory.create();
    }



}
