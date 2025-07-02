package themouselabo.appviajes_proyectofinaldam.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.PageButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.PageTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.items.NotesItem
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    userData: FirebaseUser?,
    onTripTabsClick: (String) -> Unit,
    navController: NavController
) {
    val dataViewModel: DataViewModel = viewModel()
    val notes = dataViewModel.notesState.value
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    val filteredNotes = if (searchQuery.isEmpty()) {
        notes
    } else {
        notes.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.note.contains(searchQuery, ignoreCase = true)
        }
    }

    LaunchedEffect(userData?.uid) {
        userData?.uid?.let { dataViewModel.loadNotes(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchVisible) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text(stringResource(id = R.string.search_notes)) },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null)
                            }
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.title_notes),
                            style = PageTittleTextStyle
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = null,
                            modifier = Modifier.size(PageButtonSize)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                        Icon(
                            if (isSearchVisible) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = if (isSearchVisible) "Close search" else "Search"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(filteredNotes) { note ->
                NotesItem(
                    note = note,
                    onTripTabsClick = onTripTabsClick,
                    dataViewModel = dataViewModel
                )
            }
        }
    }
}