package themouselabo.appviajes_proyectofinaldam.ui.components.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Documents
import themouselabo.appviajes_proyectofinaldam.theme.CardButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.CardTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.CategoryElementTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.CustomAlertDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.YesNoDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel
import themouselabo.appviajes_proyectofinaldam.utils.PdfViewer
import themouselabo.appviajes_proyectofinaldam.utils.getColorCardFromTrip

@Composable
fun DocumentItem(document: Documents) {

    val dataViewModel: DataViewModel = viewModel()
    val showDialog = remember { mutableStateOf(false) }
    val showEditDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = getColorCardFromTrip(document.tripId))
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                if (document.image) {
                    Icon(imageVector = Icons.Default.Image, contentDescription = "Image Icon")
                } else if (document.pdf) {
                    Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = "PDF Icon")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = document.title,
                    style = CategoryElementTextStyle,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                CustomIconButton(
                    onClick = { showDialog.value = true },
                    icon = Icons.Default.ZoomIn,
                    iconSize = CardButtonSize
                )
                Spacer(modifier = Modifier.width(5.dp))
                CustomIconButton(
                    onClick = { showEditDialog.value = true },
                    icon = Icons.Default.Edit,
                    iconSize = CardButtonSize
                )
                Spacer(modifier = Modifier.width(5.dp))
                CustomIconButton(
                    onClick = { showDeleteDialog.value = true },
                    icon = Icons.Default.Delete,
                    iconSize = CardButtonSize
                )
            }
        }

        if (showDialog.value) {
            if (document.image) {
                CustomAlertDialog(
                    onDismiss = { showDialog.value = false },
                    title = { Text(text = document.title) },
                    content = {
                        AsyncImage(
                            model = document.url,
                            contentDescription = document.title,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        CustomTextIconButton(
                            onClick = { showDialog.value = false },
                            icon = Icons.Default.Cancel,
                            text = stringResource(id = R.string.close),
                            styleText = CardTextStyle,
                            iconSize = CardButtonSize
                        )
                    })
            } else if (document.pdf) {
                PdfViewer(document.url)
            }
        }

        if (showEditDialog.value) {
            var newTitle by remember { mutableStateOf(document.title) }
            CustomAlertDialog(
                onDismiss = { showEditDialog.value = false },
                title = { Text(text = stringResource(id = R.string.edit_document)) },
                content = {
                    Column {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text(text = stringResource(id = R.string.title)) }
                        )
                    }
                },
                confirmButton = {
                    CustomTextIconButton(
                        onClick = {
                            val updatedDocument = document.copy(title = newTitle)
                            dataViewModel.addDocuments(updatedDocument)
                            showEditDialog.value = false
                        },
                        icon = Icons.Default.Save,
                        text = stringResource(id = R.string.save),
                        styleText = CardTextStyle,
                        iconSize = CardButtonSize
                    )
                },
                dismissButton = {
                    CustomTextIconButton(
                        onClick = { showEditDialog.value = false },
                        icon = Icons.Default.Cancel,
                        text = stringResource(id = R.string.cancel),
                        styleText = CardTextStyle,
                        iconSize = CardButtonSize
                    )
                }
            )
        }

        if (showDeleteDialog.value) {
            YesNoDialog(
                title = stringResource(id = R.string.confirm_delete_document),
                text = stringResource(id = R.string.are_you_sure_document),
                onConfirm = {
                    dataViewModel.deleteDocuments(document)
                    showDeleteDialog.value = false
                },
                onDismiss = { showDeleteDialog.value = false })
        }
    }
}