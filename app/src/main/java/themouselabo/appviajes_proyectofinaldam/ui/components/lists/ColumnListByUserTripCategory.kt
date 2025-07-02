package themouselabo.appviajes_proyectofinaldam.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.data.Documents
import themouselabo.appviajes_proyectofinaldam.data.Luggage
import themouselabo.appviajes_proyectofinaldam.theme.PageSettingsOptionsTextStyle

@Composable
fun ColumnLuggageListByUserTripCategory(
    userId: String,
    fetchList: () -> Unit,
    renderItem: @Composable (Luggage) -> Unit,
    emptyMessage: String,
    luggages: List<Luggage>
) {
    LaunchedEffect(userId) {
        fetchList()
    }

    if (luggages.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = emptyMessage, style = PageSettingsOptionsTextStyle)
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            luggages.forEach { luggage ->
                renderItem(luggage)
            }
        }
    }
}

@Composable
fun ColumnDocumentsListByUserTripCategory(
    userId: String,
    fetchList: () -> Unit,
    renderItem: @Composable (Documents) -> Unit,
    emptyMessage: String,
    documents: List<Documents>?
) {
    LaunchedEffect(userId) {
        fetchList()
    }

    if (documents != null) {
        if (documents.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = emptyMessage, style = PageSettingsOptionsTextStyle)
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                documents.forEach { document ->
                    renderItem(document)
                }
            }
        }
    }
}