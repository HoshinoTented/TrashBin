import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.HashMap

@Suppress("MayBeConstant")
val configRoot = ".config"
val configPath = "$configRoot/config.properties"
val config : Properties
	get() {
		return Properties().apply {
			load(File(configPath).inputStream())
		}
	}

val templatePath get() = "$configRoot/template.json"
var template : JSONObject
	get() {
		return File(templatePath).readText().run(::JSONObject)
	}
	set(value) {
		File(templatePath).writeText(value.toString())
	}

val nodesPath get() = "$configRoot/nodes.json"
var nodes : Map<String, SSRNode>
	get() {
		return File(nodesPath).readText().run(::JSONObject).let { obj ->
			HashMap<String, SSRNode>().apply {
				obj.keys().forEach {
					this[it] = obj.getJSONObject(it).run(SSRNode.Companion::fromJson)
				}
			}
		}
	}
	set(value) {
		value.mapValues {
			it.value.json
		}.run(::JSONObject).toString().let {
			File(nodesPath).writeText(it)
		}
	}

fun SSRNode.toJson(tmplt : JSONObject = template) : JSONObject {
	return JSONObject(tmplt, tmplt.keySet().toTypedArray()).also {
		json.let { obj ->
			obj.keys().forEach { name ->
				it.put(name, obj[name])
			}
		}
	}
}