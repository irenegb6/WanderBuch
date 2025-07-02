package themouselabo.appviajes_proyectofinaldam.ui.screens

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.DialogTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.PageButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.PageTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.TranslateButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.TranslateButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.TranslateTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextButton
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.utils.TranslationTasks
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(navController: NavController) {

    val languages = listOf(
        stringResource(R.string.english) to Locale("en"),
        stringResource(R.string.spanish) to Locale("es"),
        stringResource(R.string.french) to Locale("fr"),
        stringResource(R.string.german) to Locale("de"),
        stringResource(R.string.italian) to Locale("it"),
        stringResource(R.string.portuguese) to Locale("pt"),
        stringResource(R.string.chinese) to Locale("zh"),
        stringResource(R.string.japanese) to Locale("ja"),
        stringResource(R.string.korean) to Locale("ko")
    )

    // Estados para manejar el idioma seleccionado
    var fromLanguage by remember { mutableStateOf(languages.first()) }
    var toLanguage by remember { mutableStateOf(languages[1]) }
    var inputText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }

    var expandedFrom by remember { mutableStateOf(false) }
    var expandedTo by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_translation),
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
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text(text = stringResource(R.string.enter_translation)) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(30.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                textStyle = DialogTextStyle,
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.width(15.dp))
                Box {
                    CustomTextButton(
                        onClick = { expandedFrom = !expandedFrom },
                        text = fromLanguage.first,
                        styleText = TranslateButtonTextStyle
                    )
                    DropdownMenu(
                        expanded = expandedFrom,
                        onDismissRequest = { expandedFrom = false },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(y = 1.dp)
                    ) {
                        languages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(text = language.first, style = DialogTextStyle) },
                                onClick = {
                                    fromLanguage = language
                                    expandedFrom = false
                                }
                            )
                        }
                    }
                }
                Text(text = stringResource(R.string.to_translation))
                Box {
                    CustomTextButton(
                        onClick = { expandedTo = !expandedTo },
                        text = toLanguage.first,
                        styleText = TranslateButtonTextStyle
                    )
                    DropdownMenu(
                        expanded = expandedTo,
                        onDismissRequest = { expandedTo = false },
                        modifier = Modifier
                            .align(Alignment.TopStart) // Alineación para el menú
                            .offset(y = 1.dp) // Ajuste de la posición en el eje Y
                    ) {
                        languages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(text = language.first, style = DialogTextStyle) },
                                onClick = {
                                    toLanguage = language
                                    expandedTo = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(15.dp))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.7f)
                    .padding(30.dp)
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.onBackground,
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = translatedText,
                    style = TranslateTextStyle
                )
            }
            CustomTextIconButton(
                onClick = {
                    if (inputText.isNotEmpty()) {
                        TranslationTasks(
                            inputText, toLanguage.second.toString(), fromLanguage.second.toString()
                        ) { result -> translatedText = result }
                    } else {
                        Toast.makeText(context, R.string.enter_translation, Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                icon = Icons.Default.Translate,
                text = stringResource(R.string.translate),
                styleText = TranslateButtonTextStyle,
                iconSize = TranslateButtonSize
            )
        }
    }
}