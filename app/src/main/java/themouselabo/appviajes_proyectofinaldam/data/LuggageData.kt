package themouselabo.appviajes_proyectofinaldam.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import themouselabo.appviajes_proyectofinaldam.R

data class Luggage(
    val id: String,
    val element: String,
    val category: String,
    val checked: Boolean,
    val tripId: String,
    val userId: String
){
    constructor(): this(
        id = "",
        element = "",
        category = "",
        checked = false,
        tripId = "",
        userId = ""
    )
}

@Composable
fun provideCategoryLuggageDataMap(): Map<String, String> {
    return mapOf(
        stringResource(id = R.string.luggage_tops) to stringResource(id = R.string.category_tops),
        stringResource(id = R.string.luggage_bottoms) to stringResource(id = R.string.category_bottoms),
        stringResource(id = R.string.luggage_outerwear) to stringResource(id = R.string.category_outerwear),
        stringResource(id = R.string.luggage_underwear) to stringResource(id = R.string.category_underwear),
        stringResource(id = R.string.luggage_shoes) to stringResource(id = R.string.category_shoes),
        stringResource(id = R.string.luggage_accessories) to stringResource(id = R.string.category_accessories),
        stringResource(id = R.string.luggage_miscellaneous) to stringResource(id = R.string.category_miscellaneous)
    )
}

@Composable
fun provideLuggageCategory(): List<String> {
    val categoryLuggageDataMap = provideCategoryLuggageDataMap()
    return categoryLuggageDataMap.keys.toList()
}