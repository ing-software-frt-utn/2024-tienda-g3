package com.dshagapps.tfi_software.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ScreenBottomButtons(
    modifier: Modifier = Modifier,
    onPrimaryButton: () -> Unit,
    onSecondaryButton: (() -> Unit)? = null,
    primaryButtonEnabled: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onPrimaryButton,
            enabled = primaryButtonEnabled
        ) {
            Text("Siguiente")
        }

        onSecondaryButton?.let { onClick ->
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick
            ) {
                Text("Atras")
            }
        }
    }
}
