package themouselabo.appviajes_proyectofinaldam.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomGoogleSignInButton
import themouselabo.appviajes_proyectofinaldam.utils.GetSignInImageScreen

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GetSignInImageScreen()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier.padding(bottom = 370.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                CustomGoogleSignInButton(onClick = { onSignInClick() })
            }
        }
    }
}