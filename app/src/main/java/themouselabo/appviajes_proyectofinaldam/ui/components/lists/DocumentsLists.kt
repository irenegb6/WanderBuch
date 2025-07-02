package themouselabo.appviajes_proyectofinaldam.ui.components.lists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.CategoryCard
import themouselabo.appviajes_proyectofinaldam.ui.components.items.DocumentItem
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@Composable
fun CategoryDocumentsList(
    userId: String,
    tripId: String,
    category: String,
    categoryName: String,
    icon: ImageVector
) {

    val dataViewModel: DataViewModel = viewModel()
    val document = dataViewModel.documentsState.value[category]

    LaunchedEffect(userId) {
        userId.let { dataViewModel.loadDocuments(it, tripId, category) }
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
        icon = icon,
        content = {
            if (document != null) {
                ColumnDocumentsListByUserTripCategory(
                    userId = userId,
                    fetchList = { dataViewModel.loadDocuments(userId, tripId, category) },
                    renderItem = { document -> DocumentItem(document) },
                    emptyMessage = stringResource(id = R.string.no_documents),
                    documents = document
                )
            }
        }
    )
}