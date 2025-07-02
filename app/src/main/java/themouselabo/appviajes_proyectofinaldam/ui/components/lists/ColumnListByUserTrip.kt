package themouselabo.appviajes_proyectofinaldam.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.data.Note
import themouselabo.appviajes_proyectofinaldam.theme.EmptyTextStyle

@Composable
fun ColumnNotesListByUserTrip(
    userId: String,
    fetchList: () -> Unit,
    renderItem: @Composable (Note) -> Unit,
    emptyMessage: String,
    notes: List<Note>
) {
    LaunchedEffect(userId) {
        fetchList()
    }

    if (notes.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = emptyMessage, style = EmptyTextStyle)
        }
    } else {
        notes.forEach { note ->
            renderItem(note)
        }
    }
}
