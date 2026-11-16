package com.example.gamewatch.data.remote

import com.example.gamewatch.data.model.CheapSharkDeal
import retrofit2.http.GET
import retrofit2.http.Query

interface CheapSharkApi {

    @GET("deals")
    suspend fun buscarOfertas(
        @Query("pageSize") quantidade: Int = 20,
        @Query("onSale") emPromocao: Int = 1,
        @Query("sortBy") ordenarPor: String = "DealRating"
    ): List<CheapSharkDeal>

}
