package themouselabo.appviajes_proyectofinaldam.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.CardHashtagTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.SignInButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.utils.GoogleIcon


@Composable
fun CustomIconButton(onClick: () -> Unit, icon: ImageVector, iconSize: Dp) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(iconSize)
            .background(Color.Transparent)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun CustomTextButton(
    onClick: () -> Unit,
    text: String,
    styleText: TextStyle,
    buttonColor: Color = Color.Transparent
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor.copy(alpha = 1f),
            contentColor = Color.White
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, style = styleText)
        }
    }
}


@Composable
fun CustomDateButton(onClick: () -> Unit, text: @Composable (() -> Unit)? = null) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
            contentColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        text?.let { Box(modifier = Modifier.padding(bottom = 8.dp)) { it() } }
    }
}

@Composable
fun CustomTextIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    styleText: TextStyle,
    iconSize: Dp,
    buttonColor: Color = Color.Transparent
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor.copy(alpha = 1f),
            contentColor = Color.White
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Text(text = text, style = styleText)
        }
    }
}

@Composable
fun CustomRowTextIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    styleText: TextStyle,
    iconSize: Dp,
    buttonColor: Color = Color.Transparent
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor.copy(alpha = 1f),
            contentColor = Color.White
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Text(text = text, style = styleText)
        }
    }
}

@Composable
fun CustomHashtagTextButton(onClick: () -> Unit, text: String, color: Color) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .background(
                color = color.copy(alpha = 0.8f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = CardHashtagTextStyle.toSpanStyle()) {
                    append("#$text")
                }
            }, onClick = { onClick() })
    }
}

@Composable
fun CustomGoogleSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = GoogleIcon,
                contentDescription = stringResource(id = R.string.continue_with_google),
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Text(
                text = stringResource(id = R.string.continue_with_google),
                style = SignInButtonTextStyle
            )
        }
    }
}
