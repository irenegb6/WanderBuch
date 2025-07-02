package themouselabo.appviajes_proyectofinaldam.data

import android.annotation.SuppressLint
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Calendar

@SuppressLint("StaticFieldLeak")
private val firestore = FirebaseFirestore.getInstance()
private val storageRef = Firebase.storage.reference
const val tripsCollection: String = "TRIPS"
const val notesCollection: String = "NOTES"
const val luggageCollection: String = "LUGGAGE"
const val eventsCollection: String = "EVENTS"
const val documentsCollection: String = "DOCUMENTS"

// TRIPS
suspend fun getTripsFromNowToNextTwoMonths(userId: String): List<Trip> {
    val currentDate = Timestamp.now()
    val twoMonthsLater = Calendar.getInstance().apply { add(Calendar.MONTH, 2) }
    val twoMonthsLaterTimestamp = Timestamp(twoMonthsLater.time)
    return try {
        val userResponse = firestore.collection(tripsCollection)
            .whereEqualTo("userId", userId)
            .whereGreaterThanOrEqualTo("endDate", currentDate)
            .whereLessThanOrEqualTo("endDate", twoMonthsLaterTimestamp)
            .get()
            .await()
        userResponse.documents.mapNotNull {
            it.toObject(Trip::class.java)
        }.sortedBy { it.startDate }
    } catch (e: Exception) {
        emptyList()
    }
}

suspend fun getFutureTrips(userId: String): List<Trip> {
    val currentDate = Timestamp.now()
    return try {
        val userResponse = firestore.collection(tripsCollection)
            .whereEqualTo("userId", userId)
            .whereGreaterThanOrEqualTo("endDate", currentDate)
            .get()
            .await()
        userResponse.documents.mapNotNull {
            it.toObject(Trip::class.java)
        }.sortedBy { it.startDate }
    } catch (e: Exception) {
        emptyList()
    }
}

suspend fun getPastTrips(userId: String): List<Trip> {
    val currentDate = Timestamp.now()
    return try {
        val userResponse = firestore.collection(tripsCollection)
            .whereEqualTo("userId", userId)
            .whereLessThan("endDate", currentDate)
            .get()
            .await()
        userResponse.documents.mapNotNull {
            it.toObject(Trip::class.java)
        }.sortedBy { it.startDate }
    } catch (e: Exception) {
        emptyList()
    }
}

suspend fun getTripDestination(tripId: String): String? {
    return try {
        val tripDoc = firestore.collection(tripsCollection).document(tripId).get().await()
        val destination = tripDoc.getString("destination")
        destination
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun getTripColor(tripId: String): String? {
    return try {
        val tripDoc = firestore.collection(tripsCollection).document(tripId).get().await()
        val color = tripDoc.getString("color")
        color
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun getStartDateFromFirebase(tripId: String): Timestamp? {
    return try {
        val tripDoc = firestore.collection(tripsCollection).document(tripId).get().await()
        val startDate = tripDoc.getTimestamp("startDate")
        startDate
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun getEndDateFromFirebase(tripId: String): Timestamp? {
    return try {
        val tripDoc = firestore.collection(tripsCollection).document(tripId).get().await()
        val endDate = tripDoc.getTimestamp("endDate")
        endDate
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun saveTripFirebase(trip: Trip) {
    try {
        firestore.collection(tripsCollection)
            .document(trip.id)
            .set(trip)
            .await()
    } catch (e: Exception) {
        throw e
    }
}

suspend fun updateTripFirebase(trip: Trip) {
    withContext(Dispatchers.IO) {
        val tripRef = firestore.collection(tripsCollection).document(trip.id)
        tripRef.set(trip).await()
    }
}

suspend fun deleteTripByIdFirebase(tripId: String) {
    firestore.collection(tripsCollection).document(tripId).delete().await()
}

suspend fun DeleteTrip(tripId: String) {
    withContext(Dispatchers.IO) {
        deleteNotesByTripIdFirebase(tripId)
        deleteLuggageByTripIdFirebase(tripId)
        deleteDocumentsByTripIdFirebase(tripId)
        deleteEventsByTripIdFirebase(tripId)
        deleteTripByIdFirebase(tripId)
    }
}

// NOTES
suspend fun getNotesByUser(userId: String): List<Note> {
    return try {
        val userResponse = firestore.collection(notesCollection)
            .whereEqualTo("userId", userId)
            .get()
            .await()
        userResponse.documents.mapNotNull {
            it.toObject(Note::class.java)
        }
    } catch (e: Exception) {
        emptyList()
    }
}

suspend fun getNotesByTrip(userId: String, tripId: String): List<Note> {
    return try {
        val userResponse = firestore.collection(notesCollection)
            .whereEqualTo("userId", userId)
            .whereEqualTo("tripId", tripId)
            .get()
            .await()
        userResponse.documents.mapNotNull {
            it.toObject(Note::class.java)
        }
    } catch (e: Exception) {
        emptyList()
    }
}

suspend fun updateNoteFirebase(note: Note) {
    withContext(Dispatchers.IO) {
        val tripRef = firestore.collection(notesCollection).document(note.id)
        tripRef.set(note).await()
    }
}

suspend fun saveNoteFirebase(note: Note) {
    try {
        firestore.collection(notesCollection)
            .document(note.id)
            .set(note)
            .await()
    } catch (e: Exception) {
        throw e
    }
}

suspend fun deleteNoteFirebase(note: Note) {
    try {
        firestore.collection(notesCollection)
            .document(note.id)
            .delete()
            .await()
    } catch (e: Exception) {
        throw e
    }
}

suspend fun deleteNotesByTripIdFirebase(tripId: String) {
    val notes = firestore.collection(notesCollection).whereEqualTo("tripId", tripId).get().await()
    for (document in notes.documents) {
        firestore.collection(notesCollection).document(document.id).delete().await()
    }
}

// LUGGAGE
suspend fun getLuggageByCategory(userId: String, tripId: String, category: String): List<Luggage> {
    try {
        val userResponse = firestore.collection(luggageCollection)
            .whereEqualTo("userId", userId)
            .whereEqualTo("tripId", tripId)
            .whereEqualTo("category", category)
            .get()
            .await()
        return userResponse.documents.mapNotNull {
            it.toObject(Luggage::class.java)
        }
    } catch (e: Exception) {
        throw e
    }
}

suspend fun saveLuggageFirebase(luggage: Luggage) {
    try {
        firestore.collection(luggageCollection)
            .document(luggage.id)
            .set(luggage)
            .await()
    } catch (e: Exception) {
        throw e
    }
}

suspend fun updateLuggageCheckFirebase(luggage: Luggage, checked: Boolean) {
    try {
        firestore.collection(luggageCollection)
            .document(luggage.id)
            .update("checked", checked)
            .await()
    } catch (e: Exception) {
        throw e
    }
}

suspend fun deleteLuggageFirebase(luggage: Luggage) {
    try {
        firestore.collection(luggageCollection)
            .document(luggage.id)
            .delete()
            .await()
    } catch (e: Exception) {
        throw e
    }
}

suspend fun deleteLuggageByTripIdFirebase(tripId: String) {
    val luggage =
        firestore.collection(luggageCollection).whereEqualTo("tripId", tripId).get().await()
    for (document in luggage.documents) {
        firestore.collection(luggageCollection).document(document.id).delete().await()
    }
}

// EVENTS
suspend fun saveEventFirebase(event: Event) {
    try {
        firestore.collection(eventsCollection)
            .document(event.id)
            .set(event)
            .await()
    } catch (e: Exception) {
        throw e
    }
}

suspend fun getNextEvents(userId: String): List<Event> {
    val currentDate = Timestamp.now()
    return try {
        val userResponse = firestore.collection(eventsCollection)
            .whereEqualTo("userId", userId)
            .get()
            .await()
        userResponse.documents.mapNotNull {
            it.toObject(Event::class.java)
        }.sortedBy { it.startTime }
    } catch (e: Exception) {
        emptyList()
    }
}

suspend fun getEventsByTrip(userId: String, tripId: String): List<Event> {
    return try {
        val userResponse = firestore.collection(eventsCollection)
            .whereEqualTo("userId", userId)
            .whereEqualTo("tripId", tripId)
            .get()
            .await()
        userResponse.documents.mapNotNull {
            it.toObject(Event::class.java)
        }
    } catch (e: Exception) {
        emptyList()
    }
}

suspend fun getEventsByDateTrip(userId: String, tripId: String, date: Timestamp): List<Event> {
    return try {
        val startOfDay = Calendar.getInstance().apply {
            time = date.toDate()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val endOfDay = Calendar.getInstance().apply {
            time = date.toDate()
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.time

        val startTimestamp = Timestamp(startOfDay)
        val endTimestamp = Timestamp(endOfDay)

        val querySnapshot = firestore.collection(eventsCollection)
            .whereEqualTo("userId", userId)
            .whereEqualTo("tripId", tripId)
            .whereGreaterThanOrEqualTo("date", startTimestamp)
            .whereLessThanOrEqualTo("date", endTimestamp)
            .get()
            .await()

        val events = querySnapshot.documents.mapNotNull { document ->
            document.toObject(Event::class.java)
        }
        events
    } catch (e: Exception) {
        emptyList()
    }
}


suspend fun updateEventsFirebase(event: Event) {
    withContext(Dispatchers.IO) {
        val eventRef = firestore.collection(eventsCollection).document(event.id)
        eventRef.set(event).await()
    }
}

suspend fun deleteEventsFirebase(event: Event) {
    try {
        firestore.collection(eventsCollection)
            .document(event.id)
            .delete()
            .await()
    } catch (e: Exception) {
        throw e
    }
}

suspend fun deleteEventsByTripIdFirebase(tripId: String) {
    val events = firestore.collection(eventsCollection).whereEqualTo("tripId", tripId).get().await()
    for (document in events.documents) {
        firestore.collection(eventsCollection).document(document.id).delete().await()
    }
}

// DOCUMENTS
fun updateImageToFirebaseStorage(
    userId: String,
    tripId: String,
    imageUri: Uri,
    onComplete: (String) -> Unit
) {
    val imagesRef =
        storageRef.child("images").child(userId).child(tripId).child(imageUri.lastPathSegment ?: "")
    val uploadTask = imagesRef.putFile(imageUri)
    uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let { throw it }
        }
        imagesRef.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUri = task.result
            onComplete(downloadUri.toString())
        }
    }
}

fun uploadPdfToFirebaseStorage(
    userId: String,
    tripId: String,
    pdfUri: Uri,
    onComplete: (String) -> Unit
) {
    val pdfRef =
        storageRef.child("pdfs").child(userId).child(tripId).child(pdfUri.lastPathSegment ?: "")
    val uploadTask = pdfRef.putFile(pdfUri)
    uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let { throw it }
        }
        pdfRef.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUri = task.result
            onComplete(downloadUri.toString())
        }
    }
}

suspend fun getDocumentsByCategory(
    userId: String,
    tripId: String,
    category: String
): List<Documents> {
    try {
        val userResponse = firestore.collection(
            documentsCollection
        )
            .whereEqualTo("userId", userId)
            .whereEqualTo("tripId", tripId)
            .whereEqualTo("category", category)
            .get()
            .await()
        return userResponse.documents.mapNotNull {
            it.toObject(Documents::class.java)
        }
    } catch (e: Exception) {
        throw e
    }
}

suspend fun saveDocumentFirebase(documents: Documents) {
    try {
        firestore.collection(
            documentsCollection
        )
            .document(documents.id)
            .set(documents)
            .await()
    } catch (e: Exception) {
        throw e
    }
}

suspend fun deleteDocumentFirebase(documentId: String) {
    val documents = firestore.collection(
        documentsCollection
    ).whereEqualTo("id", documentId).get().await()
    for (document in documents.documents) {
        val url = document.getString("url")
        url?.let {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(it)
            storageRef.delete().await()
        }
        firestore.collection(
            documentsCollection
        ).document(document.id).delete().await()
    }
}

suspend fun deleteDocumentsByTripIdFirebase(tripId: String) {
    val documents = firestore.collection(
        documentsCollection
    ).whereEqualTo("tripId", tripId).get().await()
    for (document in documents.documents) {
        val url = document.getString("url")
        url?.let {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(it)
            storageRef.delete().await()
        }
        firestore.collection(
            documentsCollection
        ).document(document.id).delete().await()
    }
}