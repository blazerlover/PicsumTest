package ru.exemple.picsumtest.di

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.exemple.picsumtest.BuildConfig


@Module
class OkHttpClientModule {

    @Provides
    fun okHttpClient(
        requestInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(httpLoggingInterceptor).build()
    }

    @Provides
    fun interceptor(): Interceptor {
        return Interceptor {
            var original = it.request()
            Log.d("TAG", it.request().url.toString())
            if (original.url.toString().contains("openweathermap")) {
                val url = original.url.newBuilder()
                    .addQueryParameter("appid", "c35880b49ff95391b3a6d0edd0c722eb")
                    .build()
                original = original.newBuilder().url(url).build()
            }
            return@Interceptor it.proceed(original)
        }
    }

    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().also {
            it.level =
                if (BuildConfig.BUILD_TYPE == "debug") HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            return@also
        }
    }
}