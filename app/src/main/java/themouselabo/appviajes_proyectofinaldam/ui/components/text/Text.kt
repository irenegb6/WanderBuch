package themouselabo.appviajes_proyectofinaldam.ui.components.text

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.CardDateTextStyle

@Composable
fun CustomStartDateText(
    date: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.start_date),
            style = CardDateTextStyle,
        )
        Text(
            text = date,
            style = CardDateTextStyle,
        )
    }
}

@Composable
fun CustomEndDateText(
    date: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.end_date),
            style = CardDateTextStyle,
        )
        Text(
            text = date,
            style = CardDateTextStyle,
        )
    }
}