package themouselabo.appviajes_proyectofinaldam.utils

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun PdfViewer(pdfUrl: String) {
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(pdfUrl), "application/pdf")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
