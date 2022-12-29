package com.mistersomov.coinjet.utils

fun Double.asPercentage(): String = when {
    this < 0 -> String.format("%.2f", this).split("-")[1]
    this == 0.0 -> String.format("%.1f", this)
    else -> String.format("%.2f", this)
}