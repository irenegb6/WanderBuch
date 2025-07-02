package themouselabo.appviajes_proyectofinaldam.ui.components.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import themouselabo.appviajes_proyectofinaldam.data.Luggage
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.LuggageElementCard
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@Composable
fun LuggageItem(luggage: Luggage) {

    val dataViewModel: DataViewModel = viewModel()
    var checked by remember { mutableStateOf(luggage.checked) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LuggageElementCard(
            element = luggage.element,
            checked = checked,
            onCheckElement = {
                checked = !checked
                dataViewModel.editLuggage(luggage.copy(checked = checked), checked)
            },
            onDelete = { dataViewModel.deleteLuggage(luggage) }
        )
    }
}
