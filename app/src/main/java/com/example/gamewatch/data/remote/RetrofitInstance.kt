
package com.example.gamewatch.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL =
        "https://www.cheapshark.com/api/1.0/"

    private val clienteHttp = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val requisicao = chain.request()
                .newBuilder()
                .header("User-Agent", "GameWatch/1.0")
                .build()

            chain.proceed(requisicao)
        }
        .build()

    val api: CheapSharkApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CheapSharkApi::class.java)
    }
}
