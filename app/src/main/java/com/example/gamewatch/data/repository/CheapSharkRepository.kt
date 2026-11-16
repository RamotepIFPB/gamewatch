package com.example.gamewatch.data.repository

import com.example.gamewatch.data.model.CheapSharkDeal
import com.example.gamewatch.data.remote.RetrofitInstance

class CheapSharkRepository {

    suspend fun buscarOfertas(): List<CheapSharkDeal> {
        return RetrofitInstance.api.buscarOfertas()
    }
}
