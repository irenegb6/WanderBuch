package themouselabo.appviajes_proyectofinaldam.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.Timestamp
import themouselabo.appviajes_proyectofinaldam.theme.DialogDateButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogDateTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.AddDateDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.AddTimeDialog

@Composable
fun AddDateButton(
    onDateSelected: (Timestamp) -> Unit,
    buttonText: String
) {
    var showDateDialog by remember { mutableStateOf(false) }

    CustomTextIconButton(
        onClick = { showDateDialog = true },
        icon = Icons.Default.CalendarMonth,
        text = buttonText,
        styleText = DialogDateTextStyle,
        iconSize = DialogDateButtonSize
    )

    if (showDateDialog) {
        AddDateDialog(
            buttonText = buttonText,
            onDateSelected = { dateSelected ->
                onDateSelected(dateSelected)
                showDateDialog = false
            },
            onDismiss = { showDateDialog = false }
        )
    }
}

@Composable
fun AddTimeButton(
    onTimeSelected: (Timestamp) -> Unit,
    buttonText: String
) {
    var showTimeDialog by remember { mutableStateOf(false) }

    CustomTextIconButton(
        onClick = { showTimeDialog = true },
        icon = Icons.Default.AccessTime,
        text = buttonText,
        styleText = DialogDateTextStyle,
        iconSize = DialogDateButtonSize
    )

    if (showTimeDialog) {
        AddTimeDialog(
            buttonText = buttonText,
            onTimeSelected = { timeSelected ->
                onTimeSelected(timeSelected)
                showTimeDialog = false
            },
            onDismiss = { showTimeDialog = false }
        )
    }
}
