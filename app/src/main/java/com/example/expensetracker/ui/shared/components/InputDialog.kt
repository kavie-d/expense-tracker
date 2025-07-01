package com.example.expensetracker.ui.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.expensetracker.R

@Composable
fun InputDialog(
    dialogTitle: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
    confirmationText: String = stringResource(R.string.txt_ok),
    content: @Composable () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(modifier = modifier) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Text(text = dialogTitle, style = MaterialTheme.typography.headlineSmall)

                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    content()
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text(stringResource(R.string.btn_txt_cancel))
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text(confirmationText)
                    }
                }
            }
        }
    }
}