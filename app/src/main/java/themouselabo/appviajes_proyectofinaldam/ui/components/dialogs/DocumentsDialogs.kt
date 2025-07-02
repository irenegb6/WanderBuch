package themouselabo.appviajes_proyectofinaldam.ui.components.dialogs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Documents
import themouselabo.appviajes_proyectofinaldam.data.provideCategoryDocumentsDataMap
import themouselabo.appviajes_proyectofinaldam.data.provideDocumentCategory
import themouselabo.appviajes_proyectofinaldam.data.updateImageToFirebaseStorage
import themouselabo.appviajes_proyectofinaldam.data.uploadPdfToFirebaseStorage
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogDateTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.menus.CategoryDropdownMenu
import java.util.UUID

@Composable
fun AddImageDialog(
    userId: String,
    tripId: String,
    onDismiss: () -> Unit,
    onSave: (Documents) -> Unit
) {
    val documentId = remember { UUID.randomUUID().toString() }
    var title by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    var category by remember { mutableStateOf("") }
    var expandedMenu by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val categoryDataMap = provideCategoryDocumentsDataMap()
    val getContent =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUrl = it
            }
        }

    CustomAlertDialog(
        onDismiss = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.dialog_document),
                style = DialogTittleTextStyle
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.element),
                            style = DialogTextStyle
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextIconButton(
                    onClick = { getContent.launch("image/*") },
                    icon = Icons.Default.ImageSearch,
                    text = stringResource(id = R.string.select_image),
                    styleText = DialogDateTextStyle,
                    iconSize = DialogButtonSize
                )
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { expandedMenu = true }
                ) {
                    CategoryDropdownMenu(
                        expandedMenu = expandedMenu,
                        selectedOption = category.takeIf { it.isNotEmpty() },
                        options = provideDocumentCategory(),
                        onOptionSelected = { selectedCategory ->
                            category = selectedCategory
                            expandedMenu = false
                        },
                        onDismissRequest = { expandedMenu = false },
                        displayText = { option -> Text(text = option, style = DialogTextStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        onIconClick = { expandedMenu = true }
                    )

                }
                if (showError) {
                    Text(
                        text = stringResource(id = R.string.fill_all_fields),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            CustomTextIconButton(
                onClick = {
                    if (title.isNotBlank() && imageUrl != null && category.isNotBlank()) {
                        val categoryData = categoryDataMap[category] ?: ""
                        imageUrl?.let { uri ->
                            updateImageToFirebaseStorage(userId, tripId, uri) { storageUrl ->
                                val document = Documents(
                                    documentId,
                                    title,
                                    storageUrl,
                                    categoryData,
                                    true,
                                    false,
                                    tripId,
                                    userId
                                )
                                onSave(document)
                                onDismiss()
                            }
                        }
                    } else {
                        showError = true
                    }
                },
                icon = Icons.Default.Save,
                text = stringResource(id = R.string.save),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        },
        dismissButton = {
            CustomTextIconButton(
                onClick = {
                    onDismiss()
                },
                icon = Icons.Default.Cancel,
                text = stringResource(id = R.string.cancel),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    )
}

@Composable
fun AddFileDialog(
    userId: String,
    tripId: String,
    onDismiss: () -> Unit,
    onSave: (Documents) -> Unit
) {
    val documentId = remember { UUID.randomUUID().toString() }
    var title by remember { mutableStateOf("") }
    var pdfUrl by remember { mutableStateOf<Uri?>(null) }
    var category by remember { mutableStateOf("") }
    var expandedMenu by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val categoryDataMap = provideCategoryDocumentsDataMap()
    val getContent =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                pdfUrl = it
            }
        }

    CustomAlertDialog(
        onDismiss = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.dialog_document),
                style = DialogTittleTextStyle
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.element),
                            style = DialogTextStyle
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextIconButton(
                    onClick = { getContent.launch("application/pdf") },
                    icon = Icons.Default.UploadFile,
                    text = stringResource(id = R.string.select_document),
                    styleText = DialogDateTextStyle,
                    iconSize = DialogButtonSize
                )
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { expandedMenu = true }
                ) {
                    CategoryDropdownMenu(
                        expandedMenu = expandedMenu,
                        selectedOption = category.takeIf { it.isNotEmpty() },
                        options = provideDocumentCategory(),
                        onOptionSelected = { selectedCategory ->
                            category = selectedCategory
                            expandedMenu = false
                        },
                        onDismissRequest = { expandedMenu = false },
                        displayText = { option -> Text(text = option, style = DialogTextStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        onIconClick = { expandedMenu = true }
                    )

                }
                if (showError) {
                    Text(
                        text = stringResource(id = R.string.fill_all_fields),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            CustomTextIconButton(
                onClick = {
                    if (title.isNotBlank() && pdfUrl != null && category.isNotBlank()) {
                        val categoryData = categoryDataMap[category] ?: ""
                        pdfUrl?.let { uri ->
                            uploadPdfToFirebaseStorage(userId, tripId, uri) { storageUrl ->
                                val document = Documents(
                                    documentId,
                                    title,
                                    storageUrl,
                                    categoryData,
                                    false,
                                    true,
                                    tripId,
                                    userId
                                )
                                onSave(document)
                                onDismiss()
                            }
                        }
                    } else {
                        showError = true
                    }
                },
                icon = Icons.Default.Save,
                text = stringResource(id = R.string.save),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        },
        dismissButton = {
            CustomTextIconButton(
                onClick = { onDismiss() },
                icon = Icons.Default.Cancel,
                text = stringResource(id = R.string.cancel),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    )
}