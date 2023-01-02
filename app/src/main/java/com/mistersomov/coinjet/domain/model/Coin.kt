package com.mistersomov.coinjet.domain.model

data class Coin(
    val id: String,
    val name: String,
    val fullName: String,
    val fromSymbol: String,
    val toSymbol: String,
    val price: String,
    val lastUpdate: String,
    val volume24hour: String,
    val volume24hourto: String,
    val open24hour: String,
    val high24hour: String,
    val low24hour: String,
    val changepct24hour: String,
    val changepcthour: String,
    val mktCap: String,
    val imageUrl: String,
)