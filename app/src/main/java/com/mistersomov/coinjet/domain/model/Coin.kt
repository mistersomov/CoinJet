package com.mistersomov.coinjet.domain.model

data class Coin(
    val id: String,
    val symbol: String,
    val fullName: String,
    val fromSymbol: String,
    val toSymbol: String,
    val price: Double,
    val lastUpdate: String,
    val volume24hour: Double,
    val volume24hourto: Double,
    val open24hour: Double,
    val high24hour: Double,
    val low24hour: Double,
    val changepct24hour: Double,
    val changepcthour: Double,
    val mktCap: Double,
    val imageUrl: String,
)