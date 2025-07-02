package themouselabo.appviajes_proyectofinaldam.utils

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.PageSettingsOptionsTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.SettingsCard
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.SettingsViewModel
import java.util.Locale

@Composable
fun LanguageSetting() {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel()

    val languages = listOf(
        stringResource(R.string.language_english) to Locale("en"),
        stringResource(R.string.language_spanish) to Locale("es")
    )

    var expanded by remember { mutableStateOf(false) }

    val savedLanguage = settingsViewModel.selectedLanguage.value
    val currentLocale = languages.find { it.second.language == savedLanguage } ?: languages[0]

    LaunchedEffect(savedLanguage) {
        val locale =
            languages.find { it.second.language == savedLanguage }?.second ?: Locale.getDefault()
        settingsViewModel.saveSelectedLanguage(locale.language)
        setLocale(locale, context)
    }

    SettingsCard(
        title = stringResource(R.string.settings_language_change),
        icon = Icons.Default.Language,
        content = {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currentLocale.first,
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(16.dp),
                    style = PageSettingsOptionsTextStyle
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    languages.forEach { language ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = language.first,
                                    style = PageSettingsOptionsTextStyle
                                )
                            },
                            onClick = {
                                expanded = false
                                Handler(Looper.getMainLooper()).postDelayed({
                                    settingsViewModel.saveSelectedLanguage(language.second.language)
                                    setLocale(language.second, context)
                                    (context as? Activity)?.recreate()
                                }, 100)
                            }
                        )
                    }
                }
            }
        }
    )
}

fun setLocale(locale: Locale, context: Context): Context {
    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return context.createConfigurationContext(config)
}

class TranslationTasks(
    private var text: String,
    private var toLang: String?,
    private var fromLang: String?,
    private var resultCallback: (translation: String) -> Unit,
) {
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("TranslatorTag", "$exception")
    }

    init {
        CoroutineScope(Dispatchers.IO + handler).launch {
            try {
                val translated = TranslationConnection.translateHttpURLConnection(
                    text,
                    toLang,
                    fromLang
                )

                withContext(Dispatchers.Main) {
                    resultCallback(translated)
                }
            } catch (ignored: Exception) {
            }
        }
    }
}