package themouselabo.appviajes_proyectofinaldam.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Note
import themouselabo.appviajes_proyectofinaldam.data.getTripDestination
import themouselabo.appviajes_proyectofinaldam.theme.CategoryElementTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.CategoryTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.PageButtonSize
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomHashtagTextButton
import themouselabo.appviajes_proyectofinaldam.ui.components.menus.CustomEditDeleteMenu
import themouselabo.appviajes_proyectofinaldam.utils.getColorCardFromTrip

@Composable
fun NoteCard(
    note: Note,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onTripTabsClick: (String) -> Unit
) {
    val loadingText = stringResource(R.string.loading)
    var tripDestination by remember { mutableStateOf(loadingText) }
    val tripId = note.tripId
    val colorNote = getColorCardFromTrip(note.tripId)

    LaunchedEffect(tripId) {
        tripId.let {
            val destination = getTripDestination(it)
            tripDestination = destination ?: loadingText
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = colorNote)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.StickyNote2,
                    contentDescription = null,
                    modifier = Modifier.size(PageButtonSize)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = note.title, style = CategoryTittleTextStyle)
                Spacer(modifier = Modifier.weight(1f))
                CustomEditDeleteMenu(onEdit = { onEdit() }, onDelete = onDelete)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = note.note, style = CategoryElementTextStyle)
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                CustomHashtagTextButton(
                    onClick = { onTripTabsClick(tripId) },
                    text = tripDestination,
                    color = colorNote.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}

@Composable
fun NoteSimpleCard(
    note: Note,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = getColorCardFromTrip(note.tripId))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.StickyNote2,
                    contentDescription = null,
                    modifier = Modifier.size(PageButtonSize)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = note.title, style = CategoryTittleTextStyle)
                Spacer(modifier = Modifier.weight(1f))
                CustomEditDeleteMenu(onEdit = { onEdit() }, onDelete = onDelete)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = note.note, style = CategoryElementTextStyle)
        }
    }
}