package com.mistersomov.coinjet.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mistersomov.coinjet.screen.coin.CoinScreen
import com.mistersomov.coinjet.screen.coin.CoinViewModel
import com.mistersomov.coinjet.screen.search.SearchScreen
import com.mistersomov.coinjet.screen.search.SearchViewModel

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationTree.Root.Main.name) {
        composable(NavigationTree.Root.Main.name) {
            val coinViewModel = hiltViewModel<CoinViewModel>()
            CoinScreen(navController = navController, viewModel = coinViewModel)
        }
        composable(NavigationTree.Root.Search.name) {
            val searchViewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(navController = navController, viewModel = searchViewModel)
        }
    }
}