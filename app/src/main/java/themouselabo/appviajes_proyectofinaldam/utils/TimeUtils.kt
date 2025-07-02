package themouselabo.appviajes_proyectofinaldam.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Timestamp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.PageSettingsOptionsTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.SettingsCard
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.SettingsViewModel

enum class HourFormat {
    FORMAT_24H,
    FORMAT_12H
}

@Composable
fun HourFormatSetting(onHourFormatSelected: (HourFormat) -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel()
    val selectedHourFormat by settingsViewModel.hourFormat
    val options = listOf(HourFormat.FORMAT_24H to "24h", HourFormat.FORMAT_12H to "12h")

    SettingsCard(
        title = stringResource(R.string.settings_hour_format),
        icon = Icons.Default.AccessTime,
        content = {
            options.forEach { (format, label) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedHourFormat == format,
                        onClick = {
                            settingsViewModel.setHourFormat(format)
                            onHourFormatSelected(format)
                        }
                    )
                    Text(
                        text = label,
                        modifier = Modifier.padding(start = 8.dp),
                        style = PageSettingsOptionsTextStyle
                    )
                }
            }
        }
    )
}

fun EndTimeAfterStartTime(startTime: Timestamp, endTime: Timestamp): Boolean {
    return endTime.toDate().after(startTime.toDate())
}
