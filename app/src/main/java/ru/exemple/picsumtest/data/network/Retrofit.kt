package ru.exemple.picsumtest.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class Retrofit @Inject constructor(
        var okHttpClient: OkHttpClient,
        var gsonConverterFactory: GsonConverterFactory,
        var rxJavaCallAdapterFactory: RxJavaCallAdapterFactory
) {

    lateinit var api: API

    init {
        create()
    }

    private fun create(): API {
        val baseUrl = "https://picsum.photos"
        api = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .baseUrl(baseUrl)
                .build()
                .create(API::class.java)
        return api
    }
}