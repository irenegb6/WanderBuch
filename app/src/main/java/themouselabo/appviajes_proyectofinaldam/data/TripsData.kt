package themouselabo.appviajes_proyectofinaldam.data

import com.google.firebase.Timestamp

data class Trip(
    val id: String,
    val destination: String,
    val startDate: Timestamp?,
    val endDate: Timestamp?,
    val color: String,
    val image: String,
    val userId: String
) {
    constructor() : this(
        id = "",
        destination = "",
        startDate = null,
        endDate = null,
        color = "",
        image = "",
        userId = ""
    )
}