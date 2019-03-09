import java.net.HttpURLConnection
import java.net.URL

fun get(url : String) : ByteArray {
    return (URL(url).openConnection() as HttpURLConnection).inputStream.readBytes()
}