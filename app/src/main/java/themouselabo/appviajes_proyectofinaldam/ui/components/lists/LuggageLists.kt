package themouselabo.appviajes_proyectofinaldam.ui.components.lists

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.CategoryCard
import themouselabo.appviajes_proyectofinaldam.ui.components.items.LuggageItem
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel


@Composable
fun CategoryLuggageList(
    userId: String,
    tripId: String,
    category: String,
    categoryName: String,
) {

    val dataViewModel: DataViewModel = viewModel()
    val luggage = dataViewModel.luggageState.value[category]

    LaunchedEffect(userId) {
        userId.let { dataViewModel.loadLuggage(it, tripId, category) }
    }

    val optionsOpened = remember { mutableStateListOf<String>() }
    fun openOptions(option: String) {
        if (optionsOpened.contains(option)) {
            optionsOpened.remove(option)
        } else {
            optionsOpened.add(option)
        }
    }

    CategoryCard(
        onExpand = { openOptions(categoryName) },
        expanded = optionsOpened.contains(categoryName),
        category = categoryName,
        icon = Icons.Default.Checkroom,
        content = {
            if (luggage != null) {
                ColumnLuggageListByUserTripCategory(
                    userId = userId,
                    fetchList = { dataViewModel.loadLuggage(userId, tripId, category) },
                    renderItem = { luggage -> LuggageItem(luggage) },
                    emptyMessage = stringResource(id = R.string.no_luggage),
                    luggages = luggage
                )
            }
        }
    )
}

