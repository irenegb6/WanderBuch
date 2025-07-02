package themouselabo.appviajes_proyectofinaldam.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: Timestamp?,
    val startTime: Timestamp?,
    val endTime: Timestamp?,
    val place: GeoPoint?,
    val isMap: Boolean,
    val tripId: String,
    val userId: String
) {
    constructor() : this(
        id = "",
        title = "",
        description = "",
        date = null,
        startTime = null,
        endTime = null,
        place = null,
        isMap = true,
        tripId = "",
        userId = ""
    )
}