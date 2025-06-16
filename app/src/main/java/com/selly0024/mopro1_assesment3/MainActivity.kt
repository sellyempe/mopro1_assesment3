package com.selly0024.mopro1_assesment3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.selly0024.mopro1_assesment3.ui.screen.DetailScreen
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

                AppNavHost(
                    navController = navController,
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable(route = "main_screen") {
            MainScreen(navController = navController, viewModel = mainViewModel)
        }

        composable(
            route = "detail_screen/{ootdId}",
            arguments = listOf(navArgument("ootdId") { type = NavType.StringType }) // Tipe diubah ke String
        ) { backStackEntry ->
            val ootdId = backStackEntry.arguments?.getString("ootdId")
            if (ootdId != null) {
                // ViewModel diteruskan ke DetailScreen
                DetailScreen(
                    navController = navController,
                    viewModel = mainViewModel,
                    ootdId = ootdId
                )
            } else {
                Toast.makeText(LocalContext.current, "Error: OOTD ID tidak ditemukan", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    }
}