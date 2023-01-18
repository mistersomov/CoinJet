package com.mistersomov.coinjet.data.network.api

import com.mistersomov.coinjet.data.network.model.AssetsDto
import retrofit2.http.GET

interface CoinApiService {

    @GET(ENDPOINT_ASSETS)
    suspend fun getTopCoinList(): AssetsDto

    companion object {
        //Endpoints
        private const val ENDPOINT_ASSETS = "/v2/assets"
    }
}