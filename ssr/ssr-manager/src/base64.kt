import java.util.*

fun fixBase64(code : String) : String {
    return (4 - code.length % 4).let {
        if (4 - code.length % 4 !in 1..3) code else
            code + "=".repeat(it)
    }.replace('-', '+').replace('_', '/')
}

fun String.decode() : String {
    return String(Base64.getDecoder().decode(fixBase64(this)))
}