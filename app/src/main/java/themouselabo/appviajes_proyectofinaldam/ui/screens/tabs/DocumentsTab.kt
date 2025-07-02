package themouselabo.appviajes_proyectofinaldam.ui.screens.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AirplaneTicket
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseUser
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.provideCategoryDocumentsDataMap
import themouselabo.appviajes_proyectofinaldam.theme.ButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.AddFileDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.AddImageDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.lists.CategoryDocumentsList
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DocumentsTab(userData: FirebaseUser?, tripId: String) {

    val dataViewModel: DataViewModel = viewModel()
    val categoryDataMap = provideCategoryDocumentsDataMap()
    val imageDialogShown = remember { mutableStateOf(false) }
    val fileDialogShown = remember { mutableStateOf(false) }

    val categoryIcons = mapOf(
        stringResource(R.string.category_documentation) to Icons.Default.Badge,
        stringResource(R.string.category_transportation) to Icons.AutoMirrored.Filled.AirplaneTicket,
        stringResource(R.string.category_accommodation) to Icons.Default.Hotel,
        stringResource(R.string.category_entertainment) to Icons.Default.ConfirmationNumber,
        stringResource(R.string.category_miscellaneous) to Icons.Default.AttachFile
    )

    Scaffold {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
        ) {
            item { Spacer(modifier = Modifier.height(5.dp)) }
            item {
                Row {
                    CustomTextIconButton(
                        onClick = {
                            imageDialogShown.value = true
                        },
                        icon = Icons.Default.AddAPhoto,
                        text = stringResource(id = R.string.update_photo),
                        styleText = ButtonTextStyle,
                        iconSize = 24.dp,
                        buttonColor = Color.Transparent
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    CustomTextIconButton(
                        onClick = {
                            fileDialogShown.value = true
                        },
                        icon = Icons.Default.UploadFile,
                        text = stringResource(id = R.string.update_file),
                        styleText = ButtonTextStyle,
                        iconSize = 24.dp,
                        buttonColor = Color.Transparent
                    )
                }
            }

            categoryDataMap.forEach { (document, category) ->
                item {
                    if (userData != null) {
                        val icon = categoryIcons[document] ?: Icons.Default.FileCopy
                        CategoryDocumentsList(
                            userId = userData.uid,
                            tripId = tripId,
                            category = category,
                            categoryName = document,
                            icon = icon
                        )
                    }
                }
            }
        }
    }

    if (imageDialogShown.value) {
        if (userData != null) {
            AddImageDialog(
                userId = userData.uid, tripId = tripId,
                onDismiss = { imageDialogShown.value = false },
                onSave = { document ->
                    dataViewModel.addDocuments(document)
                    imageDialogShown.value = false
                }
            )
        }
    }

    if (fileDialogShown.value) {
        if (userData != null) {
            AddFileDialog(
                userId = userData.uid, tripId = tripId,
                onDismiss = { fileDialogShown.value = false },
                onSave = { document ->
                    dataViewModel.addDocuments(document)
                    fileDialogShown.value = false
                }
            )
        }
    }
}