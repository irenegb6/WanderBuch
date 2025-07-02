package themouselabo.appviajes_proyectofinaldam.ui.components.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Trip
import themouselabo.appviajes_proyectofinaldam.theme.CardButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.CardButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DestinationTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.menus.CustomEditDeleteMenu
import themouselabo.appviajes_proyectofinaldam.ui.components.text.CustomEndDateText
import themouselabo.appviajes_proyectofinaldam.ui.components.text.CustomStartDateText
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.SettingsViewModel
import themouselabo.appviajes_proyectofinaldam.utils.GetTripCardImage
import themouselabo.appviajes_proyectofinaldam.utils.getThemeColorCardByName

@Composable
fun TripCard(
    trip: Trip,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onTripTabsClick: (String) -> Unit,
    onLuggageClick: (String) -> Unit,
    type: String
) {
    val settingsViewModel: SettingsViewModel = viewModel()
    val borderColor = getThemeColorCardByName(trip.color)

    Box(
        modifier = Modifier
            .padding(0.dp)
            .then(
                if (type == "row") {
                    Modifier.width(350.dp)
                } else {
                    Modifier.fillMaxWidth(0.9f)
                }
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(3.dp, borderColor, shape = RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            onClick = { onTripTabsClick(trip.id) }
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        CustomEditDeleteMenu(onEdit = { onEdit() }, onDelete = { onDelete() })
                    }
                    GetTripCardImage(trip.image)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = trip.destination,
                            style = DestinationTextStyle,
                            color = borderColor
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CustomStartDateText("${
                                trip.startDate?.let {
                                    settingsViewModel.formatDate(
                                        it
                                    )
                                }
                            }")
                            Spacer(modifier = Modifier.width(20.dp))
                            CustomEndDateText("${trip.endDate?.let { settingsViewModel.formatDate(it) }}")
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 15.dp)
        ) {
            CustomTextIconButton(
                onClick = { onLuggageClick(trip.id) },
                icon = Icons.Default.Luggage,
                text = stringResource(id = R.string.title_luggage),
                styleText = CardButtonTextStyle,
                iconSize = CardButtonSize,
                buttonColor = borderColor
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}