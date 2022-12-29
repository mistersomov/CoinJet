package com.mistersomov.coinjet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.mistersomov.coinjet.core_ui.MainTheme
import com.mistersomov.coinjet.screen.coin.CoinScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme() {
                CoinScreen(navController = rememberNavController())
            }
        }
    }
}