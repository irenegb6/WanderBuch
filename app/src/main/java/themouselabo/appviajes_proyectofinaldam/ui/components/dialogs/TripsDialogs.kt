package themouselabo.appviajes_proyectofinaldam.ui.components.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Trip
import themouselabo.appviajes_proyectofinaldam.theme.BasicTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.TittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.AddDateButton
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.ColorCard
import themouselabo.appviajes_proyectofinaldam.utils.endDateAfterStartDate
import themouselabo.appviajes_proyectofinaldam.utils.getNameByThemeColorCard
import themouselabo.appviajes_proyectofinaldam.utils.getRandomImageString
import themouselabo.appviajes_proyectofinaldam.utils.getThemeColorCardByName
import java.util.UUID

@Composable
fun AddTripDialog(
    userId: String,
    onDismiss: () -> Unit,
    onSave: (Trip) -> Unit
) {
    val tripId = UUID.randomUUID().toString()
    var destination by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf<Timestamp?>(null) }
    var endDate by remember { mutableStateOf<Timestamp?>(null) }
    val initialColor = getThemeColorCardByName("CardColor1")
    var selectedColor by remember { mutableStateOf(initialColor) }
    var showColorDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showColorDialog) {
        ColorsDialog(
            title = stringResource(id = R.string.choose_color),
            onDismiss = { showColorDialog = false },
            onSelectColor = {
                selectedColor = it
                showColorDialog = false
            },
        )
    }
    CustomAlertDialog(
        onDismiss = onDismiss,
        title = {
            Row {
                Text(text = stringResource(id = R.string.dialog_trip_add), style = TittleTextStyle)
                Spacer(modifier = Modifier.weight(1f))
                ColorCard(color = selectedColor, onClick = { showColorDialog = true })
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.destination),
                            style = BasicTextStyle
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AddDateButton(
                        onDateSelected = { selectedDate -> startDate = selectedDate },
                        buttonText = stringResource(id = R.string.start_date_time_button)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AddDateButton(
                        onDateSelected = { selectedDate -> endDate = selectedDate },
                        buttonText = stringResource(id = R.string.end_date_time_button)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.start_date),
                        style = BasicTextStyle
                    )
                    Spacer(modifier = Modifier.width(35.dp))
                    Text(
                        text = stringResource(id = R.string.end_date),
                        style = BasicTextStyle
                    )
                }
            }
        },
        confirmButton = {
            val colorName = getNameByThemeColorCard(selectedColor)
            val image = getRandomImageString()
            CustomTextIconButton(
                onClick = {
                    if (destination.isNotEmpty() && startDate != null && endDate != null) {
                        if (endDateAfterStartDate(startDate!!, endDate!!)) {
                            val trip = Trip(
                                tripId, destination, startDate!!, endDate!!,
                                colorName, image, userId
                            )
                            onSave(trip)
                            onDismiss()
                        } else {
                            Toast.makeText(
                                context,
                                R.string.error_end_date_before_start_date,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            R.string.error_all_fields_required,
                            Toast.LENGTH_SHORT
                        )
                            .show()
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
                text = stringResource(id = R.string.cancel),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    )
}

@Composable
fun EditTripDialog(
    trip: Trip,
    onDismiss: () -> Unit,
    onSave: (Trip) -> Unit
) {
    var destination by remember { mutableStateOf(trip.destination) }
    var startDate by remember { mutableStateOf(trip.startDate) }
    var endDate by remember { mutableStateOf(trip.endDate) }
    val cardColor = getThemeColorCardByName(trip.color)
    var selectedColor by remember { mutableStateOf(cardColor) }
    var showColorDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showColorDialog) {
        ColorsDialog(
            title = stringResource(id = R.string.choose_color),
            onDismiss = { showColorDialog = false },
            onSelectColor = {
                selectedColor = it
                showColorDialog = false
            },
        )
    }
    CustomAlertDialog(
        onDismiss = onDismiss,
        title = {
            Row {
                Text(text = stringResource(id = R.string.dialog_trip_edit), style = TittleTextStyle)
                Spacer(modifier = Modifier.weight(1f))
                ColorCard(color = selectedColor, onClick = { showColorDialog = true })
            }
        },
        content = {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.destination),
                            style = BasicTextStyle
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AddDateButton(
                        onDateSelected = { selectedDate -> startDate = selectedDate },
                        buttonText = stringResource(id = R.string.start_date_time_button)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AddDateButton(
                        onDateSelected = { selectedDate -> endDate = selectedDate },
                        buttonText = stringResource(id = R.string.end_date_time_button)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.start_date),
                        style = BasicTextStyle
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = stringResource(id = R.string.end_date),
                        style = BasicTextStyle
                    )
                }
            }
        },
        confirmButton = {
            val colorName = getNameByThemeColorCard(selectedColor)
            CustomTextIconButton(
                onClick = {
                    if (destination.isNotEmpty() && startDate != null && endDate != null && endDateAfterStartDate(
                            startDate!!,
                            endDate!!
                        )
                    ) {
                        if (endDateAfterStartDate(startDate!!, endDate!!)) {
                            val updatedTrip = trip.copy(
                                destination = destination,
                                startDate = startDate,
                                endDate = endDate,
                                color = colorName
                            )
                            onSave(updatedTrip)
                            onDismiss()
                        } else {
                            Toast.makeText(
                                context,
                                R.string.error_end_date_before_start_date,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            R.string.error_all_fields_required,
                            Toast.LENGTH_SHORT
                        )
                            .show()
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
                text = stringResource(id = R.string.cancel),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    )
}