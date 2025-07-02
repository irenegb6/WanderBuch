package themouselabo.appviajes_proyectofinaldam.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.google.firebase.firestore.GeoPoint
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.CardButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogDateButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogDateTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.LocationMapDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.MarkerMapDialog

@Composable
fun AddMapButton(onGeoPointSelected: (GeoPoint) -> Unit) {
    var showMapDialog by remember { mutableStateOf(false) }

    CustomTextIconButton(
        onClick = { showMapDialog = true },
        icon = Icons.Default.AddLocation,
        text = stringResource(id = R.string.add_location),
        styleText = DialogDateTextStyle,
        iconSize = DialogDateButtonSize
    )

    if (showMapDialog) {
        MarkerMapDialog(
            onLocationSelected = { geoPoint ->
                onGeoPointSelected(geoPoint)
                showMapDialog = false
            },
            onDismiss = { showMapDialog = false }
        )
    }
}

@Composable
fun LocationMapButton(geoPoint: GeoPoint) {
    var showMapDialog by remember { mutableStateOf(false) }

    CustomIconButton(
        onClick = { showMapDialog = true },
        icon = Icons.Default.LocationOn,
        iconSize = CardButtonSize
    )

    if (showMapDialog) {
        LocationMapDialog(
            geoPoint = geoPoint,
            onDismiss = { showMapDialog = false }
        )
    }
}
