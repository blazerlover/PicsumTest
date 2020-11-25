package ru.exemple.picsumtest.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.exemple.picsumtest.data.network.Retrofit
import ru.exemple.picsumtest.data.network.RetrofitService
import javax.inject.Singleton

@Module(includes = [OkHttpClientModule::class])
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory,
            rxJavaCallAdapterFactory: RxJavaCallAdapterFactory): Retrofit {
        return Retrofit(okHttpClient, gsonConverterFactory, rxJavaCallAdapterFactory)
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRxJavaCallAdapterFactory(): RxJavaCallAdapterFactory {
        return RxJavaCallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun providesRetrofitService(retrofit: Retrofit): RetrofitService {
        return RetrofitService(retrofit)
    }
}