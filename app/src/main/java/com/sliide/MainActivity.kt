package com.sliide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.sliide.navigation.AppNavGraph
import com.sliide.ui.MainContent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainContent {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}
