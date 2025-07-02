package themouselabo.appviajes_proyectofinaldam.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.PageButtonSize
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.SettingsCard
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.CustomAlertDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.YesNoDialog

@Composable
fun InfoDialog(openDialog: MutableState<Boolean>) {
    val context = LocalContext.current
    CustomAlertDialog(
        onDismiss = { openDialog.value = false },
        title = {
            Text(
                text = stringResource(id = R.string.info_app_tittle),
                style = DialogTittleTextStyle
            )
        },
        content = {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(stringResource(id = R.string.info_app), style = DialogTextStyle)
                Spacer(modifier = Modifier.height(15.dp))
                Text(stringResource(id = R.string.info_participate), style = DialogTextStyle)
                Spacer(modifier = Modifier.height(10.dp))
                ClickableText(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    text = AnnotatedString(stringResource(id = R.string.info_survey)),
                    style = DialogTextStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                    onClick = {
                        val uri = Uri.parse("https://forms.gle/y1Hj9HTv9PeXVrDr6")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        },
        confirmButton = {
            CustomTextIconButton(
                onClick = { openDialog.value = false },
                icon = Icons.Default.Cancel,
                text = stringResource(id = R.string.close),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    )
}

fun sendEmail(context: Context, subject: String, body: String) {
    val emailIntent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, arrayOf("themouselabo@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    context.startActivity(emailIntent)
}

@Composable
fun LogOutSetting(onSignOut: () -> Unit) {
    var logoutDialogShown by remember { mutableStateOf(false) }

    SettingsCard(
        title = stringResource(R.string.sign_out),
        icon = Icons.AutoMirrored.Filled.ExitToApp,
        content = {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                CustomIconButton(
                    onClick = { logoutDialogShown = true },
                    icon = Icons.AutoMirrored.Filled.Logout,
                    iconSize = PageButtonSize
                )
            }
        }
    )

    if (logoutDialogShown) {
        YesNoDialog(
            title = stringResource(R.string.sign_out),
            text = stringResource(R.string.sign_out_ask),
            onConfirm = {
                logoutDialogShown = false
                onSignOut()
            },
            onDismiss = { logoutDialogShown = false }
        )
    }
}
