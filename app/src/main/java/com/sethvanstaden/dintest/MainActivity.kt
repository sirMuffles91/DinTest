package com.sethvanstaden.dintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sethvanstaden.dintest.navigation.AppNavGraph
import com.sethvanstaden.dintest.ui.components.toolbar.DinTopAppBar
import com.sethvanstaden.dintest.ui.components.toolbar.LocalTopBarController
import com.sethvanstaden.dintest.ui.components.toolbar.TopBarController
import com.sethvanstaden.dintest.ui.theme.DinTestTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DinTestTheme {
                val navController = rememberNavController()
                val topBarController = remember { TopBarController() }
                CompositionLocalProvider(LocalTopBarController provides topBarController) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            DinTopAppBar(
                                state = topBarController.state,
                                onNavigateUp = navController::navigateUp
                            )
                        }
                    ) { innerPadding ->
                        AppNavGraph(
                            navController = navController,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}