package ru.exemple.picsumtest.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import ru.exemple.picsumtest.data.model.PictureModel
import ru.exemple.picsumtest.data.model.weatherModel.WeatherModel
import rx.Observable

interface API {

    @GET("/v2/list")
    fun getPictures(
        @Query("page") page: String,
        @Query("limit") limit: String
    ): Observable<Response<List<PictureModel>>>

    @GET
    fun getWeather(
        @Url url: String,
        @Query("id") id: String,
        @Query("units") units: String,
        @Query("lang") lang: String,
    ): Observable<Response<WeatherModel>>
}