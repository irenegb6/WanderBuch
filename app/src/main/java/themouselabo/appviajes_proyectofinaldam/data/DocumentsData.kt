package themouselabo.appviajes_proyectofinaldam.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import themouselabo.appviajes_proyectofinaldam.R

data class Documents(
    val id: String,
    val title: String,
    val url: String,
    val category: String,
    val image: Boolean,
    val pdf: Boolean,
    val tripId: String,
    val userId: String
){
    constructor(): this(
        id = "",
        title = "",
        url = "",
        category = "",
        image = false,
        pdf = false,
        tripId = "",
        userId = ""
    )
}

@Composable
fun provideCategoryDocumentsDataMap(): Map<String, String> {
    return mapOf(
        stringResource(id = R.string.documents_documentation) to stringResource(id = R.string.category_documentation),
        stringResource(id = R.string.documents_transportation) to stringResource(id = R.string.category_transportation),
        stringResource(id = R.string.documents_accommodation) to stringResource(id = R.string.category_accommodation),
        stringResource(id = R.string.documents_entertainment) to stringResource(id = R.string.category_entertainment),
        stringResource(id = R.string.documents_miscellaneous) to stringResource(id = R.string.category_miscellaneous)
    )
}

@Composable
fun provideDocumentCategory(): List<String> {
    val categoryDocumentsDataMap = provideCategoryDocumentsDataMap()
    return categoryDocumentsDataMap.keys.toList()
}
