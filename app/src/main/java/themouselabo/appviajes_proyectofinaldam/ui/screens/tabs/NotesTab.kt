package themouselabo.appviajes_proyectofinaldam.ui.screens.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseUser
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.ui.components.items.NotesSimpleItem
import themouselabo.appviajes_proyectofinaldam.ui.components.lists.ColumnNotesListByUserTrip
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesTab(userData: FirebaseUser?, tripId: String) {

    val dataViewModel: DataViewModel = viewModel()
    val notes = dataViewModel.notesState.value

    LaunchedEffect(userData?.uid, tripId) {
        userData?.uid?.let { dataViewModel.loadSimpleNotes(it, tripId) }
    }

    Scaffold {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                if (userData != null) {
                    ColumnNotesListByUserTrip(
                        userId = userData.uid,
                        fetchList = { dataViewModel.loadSimpleNotes(userData.uid, tripId) },
                        renderItem = { note ->
                            NotesSimpleItem(
                                note = note,
                                dataViewModel = dataViewModel
                            )
                        },
                        emptyMessage = stringResource(id = R.string.no_notes),
                        notes = notes
                    )
                }
            }
        }
    }
}