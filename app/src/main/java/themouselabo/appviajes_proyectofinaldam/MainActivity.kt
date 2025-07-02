package themouselabo.appviajes_proyectofinaldam

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.places.api.Places
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import themouselabo.appviajes_proyectofinaldam.theme.ProyectoFinalDAMTheme
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.SettingsViewModel
import themouselabo.appviajes_proyectofinaldam.ui.screens.HomeScreen
import themouselabo.appviajes_proyectofinaldam.ui.screens.LuggageScreen
import themouselabo.appviajes_proyectofinaldam.ui.screens.NotesScreen
import themouselabo.appviajes_proyectofinaldam.ui.screens.SettingsScreen
import themouselabo.appviajes_proyectofinaldam.ui.screens.SignInScreen
import themouselabo.appviajes_proyectofinaldam.ui.screens.TranslationScreen
import themouselabo.appviajes_proyectofinaldam.ui.screens.TripsListScreen
import themouselabo.appviajes_proyectofinaldam.ui.screens.tabs.TripTabs
import themouselabo.appviajes_proyectofinaldam.utils.DateFormat
import themouselabo.appviajes_proyectofinaldam.utils.HourFormat
import java.util.Locale

const val default_web_client_id = ""
const val LOCATION_PERMISSION_REQUEST_CODE = 1

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var settingsViewModel: SettingsViewModel
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("settings", Context.MODE_PRIVATE)
    }
    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                "language" -> {
                    val newLanguage =
                        preferences.getString("language", Locale.getDefault().language)
                    applyLocale(newLanguage ?: Locale.getDefault().language)
                    recreate()
                }

                "hour_format" -> {
                    val newHourFormat =
                        preferences.getString("hour_format", HourFormat.FORMAT_24H.name)
                    settingsViewModel.setHourFormat(
                        HourFormat.valueOf(
                            newHourFormat ?: HourFormat.FORMAT_24H.name
                        )
                    )
                    recreate()
                }

                "date_format" -> {
                    val newDateFormat =
                        preferences.getString("date_format", DateFormat.FORMAT_DDMMYYYY.name)
                    settingsViewModel.setDateFormat(
                        DateFormat.valueOf(
                            newDateFormat ?: DateFormat.FORMAT_DDMMYYYY.name
                        )
                    )
                    recreate()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }

        enableEdgeToEdge()
        auth = Firebase.auth

        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        if (!hasLocationPermission()) {
            requestLocationPermissions()
        }

        setContent {
            ProyectoFinalDAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    val credentialManager = CredentialManager.create(context)

                    NavHost(navController = navController, startDestination = "sign_in") {

                        composable("sign_in") {

                            SignInScreen(
                                onSignInClick = {
                                    val googleIdOption: GetGoogleIdOption =
                                        GetGoogleIdOption.Builder()
                                            .setFilterByAuthorizedAccounts(false)
                                            .setServerClientId(default_web_client_id)
                                            .setAutoSelectEnabled(true)
                                            .build()
                                    val request = GetCredentialRequest.Builder()
                                        .addCredentialOption(googleIdOption)
                                        .build()

                                    scope.launch {
                                        try {
                                            val result =
                                                credentialManager.getCredential(context, request)
                                            val credential = result.credential
                                            val googleIdTokenCredential =
                                                GoogleIdTokenCredential.createFrom(credential.data)
                                            val googleIdToken = googleIdTokenCredential.idToken
                                            val firebaseCredential =
                                                GoogleAuthProvider.getCredential(
                                                    googleIdToken,
                                                    null
                                                )
                                            auth.signInWithCredential(firebaseCredential)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        navController.navigate("home")
                                                    }
                                                }
                                        } catch (e: GetCredentialCancellationException) {
                                            Toast.makeText(
                                                context,
                                                "Operación cancelada por el usuario",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                context,
                                                "Error: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            )
                        }

                        composable("home") {
                            HomeScreen(
                                userData = auth.currentUser,
                                onSettingsClick = { navController.navigate("settings") },
                                onNotesClick = { navController.navigate("notes") },
                                onTranslationClick = { navController.navigate("translation") },
                                onLuggageClick = { tripId -> navController.navigate("luggage/$tripId") },
                                onTripListClick = { navController.navigate("tripsList") },
                                onTripTabsClick = { tripId -> navController.navigate("tripTab/$tripId") }
                            )
                        }

                        composable("notes") {
                            NotesScreen(
                                userData = auth.currentUser,
                                onTripTabsClick = { tripId -> navController.navigate("tripTab/$tripId") },
                                navController = navController
                            )
                        }

                        composable("tripsList") {
                            TripsListScreen(
                                userData = auth.currentUser,
                                onTripTabsClick = { tripId -> navController.navigate("tripTab/$tripId") },
                                onLuggageClick = { tripId -> navController.navigate("luggage/$tripId") },
                                navController = navController
                            )
                        }

                        composable("tripTab/{tripId}") { backStackEntry ->
                            val tripId = backStackEntry.arguments?.getString("tripId")
                            TripTabs(
                                userData = auth.currentUser,
                                tripId = tripId,
                                navController = navController
                            )
                        }

                        composable("luggage/{tripId}") { backStackEntry ->
                            val tripId = backStackEntry.arguments?.getString("tripId")
                            LuggageScreen(
                                userData = auth.currentUser,
                                tripId = tripId,
                                navController = navController
                            )
                        }

                        composable("translation") {
                            TranslationScreen(navController = navController)
                        }

                        composable("settings") {
                            SettingsScreen(
                                userData = auth.currentUser,
                                onSignOut = {
                                    auth.signOut()
                                    scope.launch {
                                        credentialManager.clearCredentialState(
                                            ClearCredentialStateRequest()
                                        )
                                    }
                                    navController.popBackStack()
                                    navController.navigate("sign_in")
                                },
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

}