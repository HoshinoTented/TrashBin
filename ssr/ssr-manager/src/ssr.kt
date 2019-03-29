import org.json.JSONObject

data class SSRNode(
    val ip: String,            // IP
    val port: String,          // 端口
    val protocol: String,      // 协议
    val method: String,        // 加密方式
    val obfs: String,          // 混淆方式
    val obfsp: String,         // 混淆参数
    val password: String       // 密码
) {
    companion object {
        fun fromJson(json : JSONObject) : SSRNode {
            return json.toMap().let { obj ->
                val server : String by obj
                val server_port : String by obj
                val password : String by obj
                val method : String by obj
                val protocol : String by obj
                val obfs : String by obj
                val obfs_param : String by obj

                SSRNode(server, server_port, protocol, method, obfs, obfs_param, password)
            }
        }
    }

    val json: JSONObject
        get() {
            return mapOf(
                "server" to ip,
                "server_port" to port,
                "password" to password,
                "method" to method,
                "protocol" to protocol,
                "obfs" to obfs,
                "obfs_param" to obfsp
            ).run(::JSONObject)
        }

    override fun toString(): String {
        return json.toString()
    }
}

fun listOfSSR(code: String): Map<String, Map<String, SSRNode>> {
    return mutableMapOf<String, MutableMap<String, SSRNode>>().also { groups ->
        code.decode().split('\n').dropLast(1).forEach { ssrs ->
            ssrs.drop(6).decode().split("/?").let { (data, params) ->
                data.split(':').let { datas ->
                    val (ip, port, protocol, method, obfs) = datas
                    val password = datas[5].decode()

                    val ps = params.split('&').map {
                        val (key, encoded) = it.split('=')

                        key to encoded.decode()
                    }.toMap()

                    val groupName = ps.getValue("group")

                    if (groups.containsKey(groupName).not()) groups[groupName] = HashMap()

                    groups.getValue(groupName)[ps.getValue("remarks")] =
                            SSRNode(ip, port, protocol, method, obfs, ps.getValue("obfsparam"), password)
                }
            }
        }
    }
}