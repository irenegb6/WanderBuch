package themouselabo.appviajes_proyectofinaldam.data

data class Note(
    val id: String,
    val title: String,
    val note: String,
    val tripId: String,
    val userId: String
) {
    constructor() : this(
        id = "",
        title = "",
        note = "",
        tripId = "",
        userId = ""
    )
}