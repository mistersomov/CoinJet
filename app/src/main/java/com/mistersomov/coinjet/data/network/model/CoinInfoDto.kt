package com.mistersomov.coinjet.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoinInfoDto(
    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("rank")
    @Expose
    val rank: String? = null,

    @SerializedName("symbol")
    @Expose
    val symbol: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("supply")
    @Expose
    val supply: String? = null,

    @SerializedName("marketCapUsd")
    @Expose
    val marketCapUsd: String? = null,

    @SerializedName("volumeUsd24Hr")
    @Expose
    val volumeUsd24Hr: String? = null,

    @SerializedName("priceUsd")
    @Expose
    val priceUsd: String? = null,

    @SerializedName("changePercent24Hr")
    @Expose
    val changePercent24Hr: String? = null,

    @SerializedName("vwap24Hr")
    @Expose
    val vwap24Hr: String? = null,
)