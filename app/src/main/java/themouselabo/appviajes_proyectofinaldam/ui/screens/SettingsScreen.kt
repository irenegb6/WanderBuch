package themouselabo.appviajes_proyectofinaldam.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.PageButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.PageButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.PageTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomRowTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.YesNoDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.SettingsViewModel
import themouselabo.appviajes_proyectofinaldam.utils.DateFormatSetting
import themouselabo.appviajes_proyectofinaldam.utils.HourFormatSetting
import themouselabo.appviajes_proyectofinaldam.utils.InfoDialog
import themouselabo.appviajes_proyectofinaldam.utils.LanguageSetting
import themouselabo.appviajes_proyectofinaldam.utils.LogOutSetting
import themouselabo.appviajes_proyectofinaldam.utils.sendEmail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userData: FirebaseUser?,
    onSignOut: () -> Unit,
    navController: NavController
) {
    var logoutDialogShown by remember { mutableStateOf(false) }
    val infoDialogShown = remember { mutableStateOf(false) }
    val subject = stringResource(id = R.string.app_name)
    val body = stringResource(id = R.string.email_content)
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_settings),
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
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth(0.1f)
                    )
                    Text(
                        text = stringResource(id = R.string.settings_language),
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }
            item { LanguageSetting() }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth(0.1f)
                    )
                    Text(
                        text = stringResource(id = R.string.settings_data_format),
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }
            item {
                HourFormatSetting(onHourFormatSelected = { selectedFormat ->
                    settingsViewModel.setHourFormat(selectedFormat)
                })
            }
            item {
                DateFormatSetting(onDateFormatSelected = { selectedFormat ->
                    settingsViewModel.setDateFormat(selectedFormat)
                })
            }
            item {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
            item { LogOutSetting(onSignOut) }
            item {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                ) {
                    CustomRowTextIconButton(
                        onClick = { infoDialogShown.value = true },
                        icon = Icons.Default.Info,
                        text = stringResource(id = R.string.info_button),
                        styleText = PageButtonTextStyle,
                        iconSize = PageButtonSize
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    CustomRowTextIconButton(
                        onClick = { sendEmail(context, subject, body) },
                        icon = Icons.Default.Email,
                        text = stringResource(id = R.string.email_button),
                        styleText = PageButtonTextStyle,
                        iconSize = PageButtonSize
                    )
                }
            }
        }
    }
    if (infoDialogShown.value) {
        InfoDialog(infoDialogShown)
    }

    if (logoutDialogShown) {
        YesNoDialog(
            title = stringResource(R.string.sign_out),
            text = stringResource(R.string.sign_out_ask),
            onConfirm = { onSignOut() },
            onDismiss = { logoutDialogShown = false })
    }
}
