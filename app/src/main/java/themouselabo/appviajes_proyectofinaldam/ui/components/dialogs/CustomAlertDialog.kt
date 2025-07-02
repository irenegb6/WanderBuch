package themouselabo.appviajes_proyectofinaldam.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CustomAlertDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
    confirmButton: @Composable (() -> Unit)? = null,
    dismissButton: @Composable (() -> Unit)? = null
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                title?.let { Box(modifier = Modifier.padding(bottom = 2.dp)) { it() } }
                content?.let { Box(modifier = Modifier.padding(bottom = 2.dp)) { it() } }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    dismissButton?.let { Box(modifier = Modifier.padding(end = 8.dp)) { it() } }
                    confirmButton?.let { it() }
                }
            }
        }
    }
}
