package themouselabo.appviajes_proyectofinaldam.ui.components.viewModel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Timestamp
import themouselabo.appviajes_proyectofinaldam.utils.DateFormat
import themouselabo.appviajes_proyectofinaldam.utils.HourFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = application.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _selectedLanguage = mutableStateOf(
        preferences.getString("language", Locale.getDefault().language)
            ?: Locale.getDefault().language
    )
    val selectedLanguage: State<String> get() = _selectedLanguage

    fun saveSelectedLanguage(language: String) {
        _selectedLanguage.value = language
        preferences.edit().putString("language", language).apply()
    }

    private val _hourFormat = mutableStateOf(
        HourFormat.valueOf(preferences.getString("hour_format", HourFormat.FORMAT_24H.name)!!)
    )
    val hourFormat: State<HourFormat> get() = _hourFormat

    private val _dateFormat = mutableStateOf(
        DateFormat.valueOf(preferences.getString("date_format", DateFormat.FORMAT_DDMMYYYY.name)!!)
    )
    val dateFormat: State<DateFormat> get() = _dateFormat

    fun setHourFormat(format: HourFormat) {
        _hourFormat.value = format
        preferences.edit().putString("hour_format", format.name).apply()
    }

    fun setDateFormat(format: DateFormat) {
        _dateFormat.value = format
        preferences.edit().putString("date_format", format.name).apply()
    }

    fun formatTime(timestamp: Timestamp): String {
        val dateTime = LocalDateTime.ofInstant(
            timestamp.toDate().toInstant(),
            ZoneId.systemDefault()
        )
        val formatter = when (_hourFormat.value) {
            HourFormat.FORMAT_12H -> DateTimeFormatter.ofPattern("hh:mm a") // 12-hour format
            HourFormat.FORMAT_24H -> DateTimeFormatter.ofPattern("HH:mm")  // 24-hour format
        }
        return dateTime.format(formatter)
    }

    fun formatDate(timestamp: Timestamp): String {
        val dateTime = LocalDateTime.ofInstant(
            timestamp.toDate().toInstant(),
            ZoneId.systemDefault()
        )
        val formatter = when (_dateFormat.value) {
            DateFormat.FORMAT_DDMMYYYY -> DateTimeFormatter.ofPattern("dd/MM/yyyy")
            DateFormat.FORMAT_MMDDYYYY -> DateTimeFormatter.ofPattern("MM/dd/yyyy")
            DateFormat.FORMAT_YYYYMMDD -> DateTimeFormatter.ofPattern("yyyy/MM/dd")
        }
        return dateTime.format(formatter)
    }
}

