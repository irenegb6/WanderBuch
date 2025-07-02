package themouselabo.appviajes_proyectofinaldam.ui.components.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import themouselabo.appviajes_proyectofinaldam.data.DeleteTrip
import themouselabo.appviajes_proyectofinaldam.data.Documents
import themouselabo.appviajes_proyectofinaldam.data.Event
import themouselabo.appviajes_proyectofinaldam.data.Luggage
import themouselabo.appviajes_proyectofinaldam.data.Note
import themouselabo.appviajes_proyectofinaldam.data.Trip
import themouselabo.appviajes_proyectofinaldam.data.deleteDocumentFirebase
import themouselabo.appviajes_proyectofinaldam.data.deleteEventsFirebase
import themouselabo.appviajes_proyectofinaldam.data.deleteLuggageFirebase
import themouselabo.appviajes_proyectofinaldam.data.deleteNoteFirebase
import themouselabo.appviajes_proyectofinaldam.data.getDocumentsByCategory
import themouselabo.appviajes_proyectofinaldam.data.getEventsByDateTrip
import themouselabo.appviajes_proyectofinaldam.data.getEventsByTrip
import themouselabo.appviajes_proyectofinaldam.data.getFutureTrips
import themouselabo.appviajes_proyectofinaldam.data.getLuggageByCategory
import themouselabo.appviajes_proyectofinaldam.data.getNextEvents
import themouselabo.appviajes_proyectofinaldam.data.getNotesByTrip
import themouselabo.appviajes_proyectofinaldam.data.getNotesByUser
import themouselabo.appviajes_proyectofinaldam.data.getPastTrips
import themouselabo.appviajes_proyectofinaldam.data.getTripsFromNowToNextTwoMonths
import themouselabo.appviajes_proyectofinaldam.data.saveDocumentFirebase
import themouselabo.appviajes_proyectofinaldam.data.saveEventFirebase
import themouselabo.appviajes_proyectofinaldam.data.saveLuggageFirebase
import themouselabo.appviajes_proyectofinaldam.data.saveNoteFirebase
import themouselabo.appviajes_proyectofinaldam.data.saveTripFirebase
import themouselabo.appviajes_proyectofinaldam.data.updateEventsFirebase
import themouselabo.appviajes_proyectofinaldam.data.updateLuggageCheckFirebase
import themouselabo.appviajes_proyectofinaldam.data.updateNoteFirebase
import themouselabo.appviajes_proyectofinaldam.data.updateTripFirebase

class DataViewModel : ViewModel() {

    // TRIPS
    private val _tripsState = mutableStateOf<List<Trip>>(emptyList())
    val tripsState: State<List<Trip>> = _tripsState
    fun loadNextTrips(userId: String) {
        viewModelScope.launch {
            val trips = getTripsFromNowToNextTwoMonths(userId)
            _tripsState.value = trips
        }
    }

    fun loadFutureTrips(userId: String) {
        viewModelScope.launch {
            val trips = getFutureTrips(userId)
            _tripsState.value = trips
        }
    }

    fun loadPastTrips(userId: String) {
        viewModelScope.launch {
            val trips = getPastTrips(userId)
            _tripsState.value = trips
        }
    }

    fun addTrip(trip: Trip) {
        viewModelScope.launch {
            saveTripFirebase(trip)
            loadNextTrips(trip.userId)
            loadFutureTrips(trip.userId)
            loadPastTrips(trip.userId)
        }
    }

    fun editTrip(trip: Trip) {
        viewModelScope.launch {
            updateTripFirebase(trip)
            loadNextTrips(trip.userId)
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            DeleteTrip(trip.id)
            loadNextTrips(trip.userId)
            loadFutureTrips(trip.userId)
            loadPastTrips(trip.userId)
        }
    }

    // LUGGAGE
    private val _luggageState = mutableStateOf<Map<String, List<Luggage>>>(emptyMap())
    val luggageState: State<Map<String, List<Luggage>>> = _luggageState
    fun loadLuggage(userId: String, tripId: String, category: String) {
        viewModelScope.launch {
            val luggage = getLuggageByCategory(userId, tripId, category)
            _luggageState.value = _luggageState.value.toMutableMap().apply {
                this[category] = luggage
            }
        }
    }

    fun addLuggage(luggage: Luggage) {
        viewModelScope.launch {
            saveLuggageFirebase(luggage)
            loadLuggage(luggage.userId, luggage.tripId, luggage.category)
        }
    }

    fun editLuggage(luggage: Luggage, checked: Boolean) {
        viewModelScope.launch {
            updateLuggageCheckFirebase(luggage, checked)
        }
    }

    fun deleteLuggage(luggage: Luggage) {
        viewModelScope.launch {
            deleteLuggageFirebase(luggage)
            loadLuggage(luggage.userId, luggage.tripId, luggage.category)
        }
    }

    // EVENTS
    private val _eventsState = mutableStateOf<List<Event>>(emptyList())
    val eventsState: State<List<Event>> get() = _eventsState
    fun loadNextEvents(userId: String) {
        viewModelScope.launch {
            val events = getNextEvents(userId)
            _eventsState.value = events
        }
    }

    fun loadEventsByTrip(userId: String, tripId: String) {
        viewModelScope.launch {
            val events = getEventsByTrip(userId, tripId)
            _eventsState.value = events
        }
    }

    fun loadEventsByDateTrip(userId: String, tripId: String, date: Timestamp) {
        viewModelScope.launch {
            val events = getEventsByDateTrip(userId, tripId, date)
            _eventsState.value = events.sortedBy { it.startTime }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            saveEventFirebase(event)
            loadEventsByTrip(event.userId, event.tripId)
            event.date?.let { loadEventsByDateTrip(event.userId, event.tripId, it) }
        }
    }

    fun editEvent(event: Event) {
        viewModelScope.launch {
            updateEventsFirebase(event)
            loadEventsByTrip(event.userId, event.tripId)
            event.date?.let { loadEventsByDateTrip(event.userId, event.tripId, it) }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            deleteEventsFirebase(event)
            loadEventsByTrip(event.userId, event.tripId)
            event.date?.let { loadEventsByDateTrip(event.userId, event.tripId, it) }
        }
    }

    // NOTES
    private val _notesState = mutableStateOf<List<Note>>(emptyList())
    val notesState: State<List<Note>> = _notesState
    fun loadNotes(userId: String) {
        viewModelScope.launch {
            val notes = getNotesByUser(userId)
            _notesState.value = notes.sortedBy { it.title }
        }
    }

    fun loadSimpleNotes(userId: String, tripId: String) {
        viewModelScope.launch {
            val notes = getNotesByTrip(userId, tripId)
            _notesState.value = notes.sortedBy { it.title }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            saveNoteFirebase(note)
            loadSimpleNotes(note.userId, note.tripId)
        }
    }

    fun editNote(note: Note) {
        viewModelScope.launch {
            updateNoteFirebase(note)
            loadNotes(note.userId)
            loadSimpleNotes(note.userId, note.tripId)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteFirebase(note)
            loadNotes(note.userId)
            loadSimpleNotes(note.userId, note.tripId)
        }
    }

    // DOCUMENTS
    private val _documentsState = mutableStateOf<Map<String, List<Documents>>>(emptyMap())
    val documentsState: State<Map<String, List<Documents>>> = _documentsState
    fun loadDocuments(userId: String, tripId: String, category: String) {
        viewModelScope.launch {
            val documents = getDocumentsByCategory(userId, tripId, category)
            _documentsState.value = _documentsState.value.toMutableMap().apply {
                this[category] = documents
            }
        }
    }

    fun addDocuments(documents: Documents) {
        viewModelScope.launch {
            saveDocumentFirebase(documents)
            loadDocuments(documents.userId, documents.tripId, documents.category)
        }
    }

    fun deleteDocuments(documents: Documents) {
        viewModelScope.launch {
            deleteDocumentFirebase(documents.id)
            loadDocuments(documents.userId, documents.tripId, documents.category)
        }
    }
}