package com.sethvanstaden.dintest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sethvanstaden.dintest.ui.features.home.HomeScreen
import com.sethvanstaden.dintest.ui.features.results.ResultsScreen
import com.sethvanstaden.dintest.ui.features.test.TestScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Home> {
            HomeScreen(
                onStartTest = { navController.navigate(Test) },
                onViewResults = { navController.navigate(Results) }
            )
        }

        composable<Test> {
            TestScreen (
                onFinished = {
                    navController.popBackStack()
                }
            )
        }

        composable<Results> { backStackEntry ->
            ResultsScreen()
        }
    }

}