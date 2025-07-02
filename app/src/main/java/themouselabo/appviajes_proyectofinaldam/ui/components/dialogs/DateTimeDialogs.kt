package themouselabo.appviajes_proyectofinaldam.ui.components.dialogs

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.firebase.Timestamp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDateDialog(
    onDateSelected: (Timestamp) -> Unit,
    buttonText: String,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val context = LocalContext.current

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            CustomTextIconButton(
                onClick = {
                    val dateMillis = datePickerState.selectedDateMillis
                    if (dateMillis != null) {
                        val selectedDate = Timestamp(Date(dateMillis))
                        onDateSelected(selectedDate)
                        onDismiss()
                    } else {
                        Toast.makeText(
                            context,
                            R.string.error_all_fields_required,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                icon = Icons.Default.Save,
                text = stringResource(id = R.string.save),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        },
        dismissButton = {
            CustomTextIconButton(
                onClick = {
                    onDismiss()
                },
                icon = Icons.Default.Cancel,
                text = stringResource(id = R.string.close),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun AddTimeDialog(
    onTimeSelected: (Timestamp) -> Unit,
    buttonText: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var time by remember { mutableStateOf<Timestamp?>(null) }

    val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        time = Timestamp(calendar.time)
        onTimeSelected(time!!)
        onDismiss()
    }

    LaunchedEffect(Unit) {
        TimePickerDialog(
            context,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}
