package themouselabo.appviajes_proyectofinaldam.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.provideCategoryLuggageDataMap
import themouselabo.appviajes_proyectofinaldam.theme.ButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.PageButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.PageTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.AddLuggageDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.lists.CategoryLuggageList
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuggageScreen(userData: FirebaseUser?, tripId: String?, navController: NavController) {

    val dataViewModel: DataViewModel = viewModel()
    val dialogShown = remember { mutableStateOf(false) }
    val categoryDataMap = provideCategoryLuggageDataMap()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_luggage),
                        style = PageTittleTextStyle
                    )
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
                    CustomTextIconButton(
                        onClick = {
                            dialogShown.value = true
                        },
                        icon = Icons.Default.Add,
                        text = stringResource(id = R.string.add_luggage),
                        styleText = ButtonTextStyle,
                        iconSize = 24.dp,
                        buttonColor = Color.Transparent
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            categoryDataMap.forEach { (luggage, category) ->
                item {
                    if (userData != null && tripId != null) {
                        CategoryLuggageList(
                            userId = userData.uid,
                            tripId = tripId,
                            category = category,
                            categoryName = luggage
                        )
                    }
                }
            }
        }
    }

    if (dialogShown.value) {
        if (userData != null && tripId != null) {
            AddLuggageDialog(
                userId = userData.uid,
                tripId = tripId,
                onDismiss = { dialogShown.value = false },
                onSave = { luggage ->
                    dataViewModel.addLuggage(luggage)
                    dialogShown.value = false
                }
            )
        }
    }
}

