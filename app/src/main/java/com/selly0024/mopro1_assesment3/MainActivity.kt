package com.selly0024.mopro1_assesment3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.selly0024.mopro1_assesment3.ui.screen.MainScreen
import com.selly0024.mopro1_assesment3.ui.screen.MainViewModel
import com.selly0024.mopro1_assesment3.ui.theme.Mopro1_assesment3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mopro1_assesment3Theme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel()
                MainScreen(navController = navController, viewModel = mainViewModel)
            }
        }
    }
}
