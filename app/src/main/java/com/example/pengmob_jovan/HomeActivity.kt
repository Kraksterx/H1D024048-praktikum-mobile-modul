package com.example.pengmob_jovan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.example.pengmob_jovan.ui.screen.DaftarProductScreen
import com.example.pengmob_jovan.ui.theme.PengMob_JovanTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PengMob_JovanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = androidx.navigation.compose.rememberNavController()
                    val productViewModel: com.example.pengmob_jovan.ui.viewmodel.ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                    androidx.navigation.compose.NavHost(navController = navController, startDestination = "daftar_produk") {
                        composable("daftar_produk") {
                            DaftarProductScreen(
                                navController = navController,
                                viewModel = productViewModel
                            )
                        }
                        composable(
                            route = "detail/{productId}",
                            arguments = listOf(androidx.navigation.navArgument("productId") {
                                type = androidx.navigation.NavType.IntType
                            })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                            com.example.pengmob_jovan.ui.screen.DetailProductScreen(
                                productId = productId,
                                navController = navController,
                                viewModel = productViewModel
                            )
                        }
                        composable("hubungi_kami") {
                            com.example.pengmob_jovan.ui.screen.HubungiKamiScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
