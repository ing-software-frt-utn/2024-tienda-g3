package com.dshagapps.tfi_software.presentation.activities

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dshagapps.tfi_software.data.repositories.SaleRepositoryImpl
import com.dshagapps.tfi_software.data.service.ServiceProvider
import com.dshagapps.tfi_software.data.service.ServiceProvider.Companion.PREF_COOKIES
import com.dshagapps.tfi_software.data.service.api.Api
import com.dshagapps.tfi_software.domain.repositories.SaleRepository
import com.dshagapps.tfi_software.presentation.navigation.Navigation
import com.dshagapps.tfi_software.presentation.ui.theme.TfisoftwareTheme
import com.dshagapps.tfi_software.presentation.viewmodel.SaleViewModel

class MainActivity : BaseActivity() {

    private lateinit var repository: SaleRepository

    private lateinit var navController: NavHostController

    private val viewModel: SaleViewModel by viewModel {
        SaleViewModel(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clearPreferences()

        repository = SaleRepositoryImpl(ServiceProvider(applicationContext).createService(Api::class.java))

        setContent {

            navController = rememberNavController()

            TfisoftwareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        navController = navController,
                        viewModel = viewModel,
                        onBack = {
                            finish()
                        },
                        onClearPreferences = { clearPreferences() },
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        clearPreferences()
        navController.graph.startDestinationRoute?.let { navController.popBackStack(it, false) } ?: finish()
    }

    private fun clearPreferences() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.edit().remove(PREF_COOKIES).apply()
    }
}