package com.dshagapps.tfi_software.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dshagapps.tfi_software.presentation.screen.AuthScreen
import com.dshagapps.tfi_software.presentation.screen.CardPaymentFormScreen
import com.dshagapps.tfi_software.presentation.screen.ClientDetailScreen
import com.dshagapps.tfi_software.presentation.screen.MainScreen
import com.dshagapps.tfi_software.presentation.screen.SaleLinesDetailScreen
import com.dshagapps.tfi_software.presentation.screen.ReceiptScreen
import com.dshagapps.tfi_software.presentation.screen.StockDetailsScreen
import com.dshagapps.tfi_software.presentation.viewmodel.SaleViewModel

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    viewModel: SaleViewModel,
    onBack: () -> Unit,
    onClearPreferences: () -> Unit
) {
    NavHost(navController = navController, startDestination = "authScreen") {
        composable("authScreen") {

            AuthScreen(
                onLogin = {
                    navController.navigate("mainScreen")
                },
                onBack = {
                    if (!navController.popBackStack()) onBack()
                },
                viewModel = viewModel
            )
        }

        composable("mainScreen") {
            MainScreen(
                onBack = {
                    onBack()
                },
                onStartSale = {
                    navController.navigate("stockDetailsScreen")
                },
                onLogout = {
                    navController.popBackStack()
                    onClearPreferences()
                },
                viewModel = viewModel
            )
        }

        composable("stockDetailsScreen") {
            StockDetailsScreen(
                onBack = {
                    navController.popBackStack()
                    viewModel.cleanStates()
                },
                onContinue = {
                    navController.navigate("saleLinesDetailScreen")
                },
                viewModel = viewModel
            )
        }

        composable("saleLinesDetailScreen") {
            SaleLinesDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                onContinue = {
                    navController.navigate("clientDetailScreen")
                },
                viewModel = viewModel
            )
        }

        composable("clientDetailScreen") {
            ClientDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                onContinue = { clientName, cuit ->
                    navController.navigate("cardPaymentFormScreen?clientName=$clientName&cuit=$cuit")
                },
                onSale = {
                    navController.navigate("receiptScreen")
                },
                viewModel = viewModel
            )
        }

        composable(
            route = "cardPaymentFormScreen?clientName={clientName}&cuit={cuit}",
            arguments = listOf(
                navArgument("clientName") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("cuit") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            CardPaymentFormScreen(
                onBack = {
                    navController.popBackStack()
                },
                onSale = {
                    navController.navigate("receiptScreen")
                },
                viewModel = viewModel,
                clientFullName = it.arguments?.getString("clientName") ?: "",
                clientCuit = it.arguments?.getString("cuit") ?: ""
            )
        }

        composable("receiptScreen") {
            ReceiptScreen(
                onBack = {
                    navController.popBackStack("mainScreen", false)
                    viewModel.cleanStates()
                },
                viewModel = viewModel
            )
        }
    }
}