package themouselabo.appviajes_proyectofinaldam.utils

import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object TranslationConnection {

    private const val charset = "UTF-8"

    fun getShortText(msg: String): String? {
        var text = msg
        try {
            if (text.length > 180) {
                text = text.substring(0, 180)
                text = text.substring(0, text.lastIndexOf(' '))
            }
        } catch (ignored: Exception) {
        }
        try {
            return URLEncoder.encode(text, "UTF-8")
        } catch (ignored: Exception) {
        }
        return text
    }

    private fun getTextHttpURLConnection(url: String): String {
        var connection: HttpURLConnection? = null

        val response = StringBuilder()
        try {
            val string = "UTF-8"
            connection = URL(url).openConnection() as HttpURLConnection
            connection.setRequestProperty("Accept-Charset", "UTF-8")
            connection.addRequestProperty(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)"
            )
            val `in` = BufferedReader(
                InputStreamReader(
                    connection.inputStream, string
                )
            )
            while (true) {
                val inputLine = `in`.readLine()
                if (inputLine == null) {
                    `in`.close()
                    return response.toString()
                }
                response.append(inputLine)
            }
        } catch (e: java.lang.Exception) {
        } finally {
            connection?.disconnect()
        }
        return response.toString()
    }

    fun translateHttpURLConnection(
        to_translate: String?,
        to_language: String?,
        from_language: String?,
    ): String {
        try {
            val hl = URLEncoder.encode(to_language, charset)
            val sl = URLEncoder.encode(from_language, charset)
            val q = URLEncoder.encode(to_translate, charset)
            try {
                val sb = java.lang.StringBuilder()
                var text =
                    getTextHttpURLConnection(
                        String.format(
                            "https://translate.google.com/translate_a/single?&client=gtx&sl=%s&tl=%s&q=%s&dt=t",
                            sl,
                            hl,
                            q
                        )
                    )

                if (TextUtils.isEmpty(text)) {
                    text =
                        getTextHttpURLConnection(
                            String.format(
                                "https://clients4.google.com/translate_a/t?client=dict-chrome-ex&sl=%s&tl=%s&q=%s&dt=t",
                                sl,
                                hl,
                                q
                            )
                        )
                    if (TextUtils.isEmpty(text)) {
                        text =
                            getTextHttpURLConnection(
                                String.format(
                                    "https://translate.google.com/m?sl=%s&tl=%s&q=%s",
                                    sl,
                                    hl,
                                    q
                                )
                            )
                        if (TextUtils.isEmpty(text)) {
                            sb.append(translateURLConnection(sl, hl, q))
                        } else {
                            sb.append(getTranslationData(text))
                        }
                    } else {

                        val jsonObject = JSONObject(text)
                        if (jsonObject.has("sentences")) {
                            val jsonArray = jsonObject.getJSONArray("sentences")
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject1 = jsonArray.getJSONObject(i)
                                if (jsonObject1 != null && jsonObject1.has("trans")) {
                                    sb.append(jsonObject1.getString("trans"))
                                }
                            }
                        }
                    }
                } else {

                    val jSONArray = JSONArray(text).getJSONArray(0)
                    for (i in 0 until jSONArray.length()) {
                        val string = jSONArray.getJSONArray(i).getString(0)
                        if (!TextUtils.isEmpty(string) && string != "null") {
                            sb.append(string)
                        }
                    }
                }
                return sb.toString()
            } catch (ignored: java.lang.Exception) {
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun translateURLConnection(sl: String?, hl: String?, q: String?): String {
        try {
            try {
                val sb = java.lang.StringBuilder()
                var text: String? =
                    getTextUrlConnection(
                        String.format(
                            "https://translate.google.com/translate_a/single?&client=gtx&sl=%s&tl=%s&q=%s&dt=t",
                            sl,
                            hl,
                            q
                        )
                    )
                if (TextUtils.isEmpty(text)) {
                    text =
                        getTextUrlConnection(
                            String.format(
                                "https://clients4.google.com/translate_a/t?client=dict-chrome-ex&sl=%s&tl=%s&q=%s&dt=t",
                                sl,
                                hl,
                                q
                            )
                        )
                    if (TextUtils.isEmpty(text)) {
                        text =
                            getTextUrlConnection(
                                String.format(
                                    "https://translate.google.com/m?sl=%s&tl=%s&q=%s",
                                    sl,
                                    hl,
                                    q
                                )
                            )
                        if (TextUtils.isEmpty(text)) {
                            Log.d("ct_TAG", "translateURLConnection: ")
                        } else {
                            sb.append(getTranslationData(text))
                        }
                    } else {
                        val jsonObject = JSONObject(text)
                        if (jsonObject.has("sentences")) {
                            val jsonArray = jsonObject.getJSONArray("sentences")
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject1 = jsonArray.getJSONObject(i)
                                if (jsonObject1 != null && jsonObject1.has("trans")) {
                                    sb.append(jsonObject1.getString("trans"))
                                }
                            }
                        }
                    }
                } else {
                    val jSONArray = JSONArray(text).getJSONArray(0)
                    for (i in 0 until jSONArray.length()) {
                        val string = jSONArray.getJSONArray(i).getString(0)
                        if (!TextUtils.isEmpty(string) && string != "null") {
                            sb.append(string)
                        }
                    }
                }
                return sb.toString()
            } catch (ignored: java.lang.Exception) {
            }
        } catch (ignored: java.lang.Exception) {
        }
        return ""
    }

    private fun getTextUrlConnection(url: String): String {
        try {
            val connection = URL(url).openConnection()
            connection.setRequestProperty("Accept-Charset", "UTF-8")
            connection.addRequestProperty(
                "User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)"
            )
            val `in` = BufferedReader(InputStreamReader(connection.getInputStream(), charset))
            val response = java.lang.StringBuilder()
            while (true) {
                val inputLine = `in`.readLine()
                if (inputLine == null) {
                    `in`.close()
                    return response.toString()
                }
                response.append(inputLine)
            }
        } catch (e: java.lang.Exception) {
        }
        return ""
    }

    private fun getTranslationData(to_translate: String): String {
        try {

            var nativeText = "class=\"t0\">"
            val result =
                to_translate.substring(to_translate.indexOf(nativeText) + nativeText.length)
                    .split("<".toRegex()).toTypedArray()[0]
            return if (result == "html>") {
                nativeText = "class=\"result-container\">"
                to_translate.substring(to_translate.indexOf(nativeText) + nativeText.length)
                    .split("<".toRegex()).toTypedArray()[0] + "+" + ""
            } else {
                result
            }
        } catch (e: java.lang.Exception) {
        } catch (e: OutOfMemoryError) {
        }
        return ""
    }
}