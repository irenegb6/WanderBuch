package themouselabo.appviajes_proyectofinaldam.ui.components.dialogs

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import com.google.android.gms.maps.model.LatLng as GmsLatLng
import com.google.type.LatLng as TypeLatLng

@Composable
fun MarkerMapDialog(onLocationSelected: (GeoPoint) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var currentLocation by remember {
        mutableStateOf(
            GmsLatLng(
                40.4168,
                -3.7038
            )
        )
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 13f)
    }
    var selectedLocation by remember { mutableStateOf<GmsLatLng?>(null) }
    var placeSuggestions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var query by remember { mutableStateOf("") }
    val placesClient = Places.createClient(context)

    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (hasLocationPermission) {
        LaunchedEffect(Unit) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    currentLocation = GmsLatLng(it.latitude, it.longitude)
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(currentLocation, 13f)
                }
            }
        }
    } else {
        Toast.makeText(
            context,
            stringResource(id = R.string.location_permission_not_granted),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun getPlaceSuggestions(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                placeSuggestions = response.autocompletePredictions
            }
            .addOnFailureListener { exception ->
                Log.e("Places API", "Error fetching place suggestions", exception)
            }
    }

    CustomAlertDialog(
        onDismiss = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.dialog_event),
                style = DialogTittleTextStyle
            )
        },
        content = {
            Column(modifier = Modifier.padding(5.dp)) {
                TextField(
                    value = query,
                    onValueChange = {
                        query = it
                        getPlaceSuggestions(it)
                    },
                    label = { Text(stringResource(id = R.string.search_location)) },
                    modifier = Modifier.fillMaxWidth()
                )

                LazyColumn(modifier = Modifier.height(150.dp)) {
                    items(placeSuggestions.size) { index ->
                        val suggestion = placeSuggestions[index]
                        Text(
                            text = suggestion.getPrimaryText(null).toString(),
                            modifier = Modifier
                                .clickable {
                                    val placeId = suggestion.placeId
                                    val placeFields =
                                        listOf(Place.Field.LAT_LNG)
                                    val request =
                                        FetchPlaceRequest.newInstance(placeId, placeFields)

                                    placesClient
                                        .fetchPlace(request)
                                        .addOnSuccessListener { response ->
                                            val place = response.place
                                            val latLng = place.latLng
                                            latLng?.let {
                                                currentLocation =
                                                    GmsLatLng(it.latitude, it.longitude)
                                                cameraPositionState.position =
                                                    CameraPosition.fromLatLngZoom(
                                                        currentLocation,
                                                        15f
                                                    )
                                                selectedLocation = currentLocation
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Failed to get location: ${exception.message}",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                }
                                .padding(8.dp)
                        )
                    }
                }
                GoogleMap(
                    modifier = Modifier
                        .fillMaxHeight(0.6f)
                        .fillMaxWidth()
                        .padding(5.dp),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
                    onMapClick = { position ->
                        selectedLocation = position
                        currentLocation = position
                    }
                ) {
                    selectedLocation?.let {
                        Marker(
                            state = rememberMarkerState(position = it),
                            title = stringResource(id = R.string.selected_location)
                        )
                    }
                }
            }
        },
        confirmButton = {
            CustomTextIconButton(
                onClick = {
                    selectedLocation?.let {
                        val geoPoint = TypeLatLng.newBuilder()
                            .setLatitude(it.latitude)
                            .setLongitude(it.longitude)
                            .build()
                        onLocationSelected(GeoPoint(geoPoint.latitude, geoPoint.longitude))
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
                onClick = { onDismiss() },
                icon = Icons.Default.Cancel,
                text = stringResource(id = R.string.close),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    )
}


@Composable
fun LocationMapDialog(geoPoint: GeoPoint, onDismiss: () -> Unit) {
    val location = GmsLatLng(geoPoint.latitude, geoPoint.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 13f)
    }

    CustomAlertDialog(
        onDismiss = onDismiss,
        title = { Spacer(modifier = Modifier.size(0.dp)) },
        content = {
            GoogleMap(
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxWidth(),
                cameraPositionState = cameraPositionState,
                onMapClick = {}
            ) {
                Marker(
                    state = rememberMarkerState(position = location),
                    title = stringResource(id = R.string.selected_location)
                )
            }
        },
        confirmButton = { Spacer(modifier = Modifier.size(0.dp)) },
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
    )
}
