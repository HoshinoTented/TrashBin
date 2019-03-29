import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class NodeFile(val file: File, nodes: MutableMap<String, SSRNode> = HashMap()) : MutableMap<String, SSRNode> by nodes {
	fun load() {
		clear()

		try {
			if (file.exists()) file.readText().run(::JSONObject).let { obj ->
				obj.keys().forEach {
					this[it] = obj.getJSONObject(it).run(SSRNode.Companion::fromJson)
				}
			}
		} catch (e: JSONException) {
			System.err.println("Can not parse ${file.name}")
		}
	}

	fun save() {
		mapValues {
			it.value.json
		}.run(::JSONObject).toString().let {
			file.writeText(it)
		}
	}
}

class NodeDir(root: String) : Map<String, NodeFile> {
	private val rootDir = File(root)

	override val entries: Set<Map.Entry<String, NodeFile>>
		get() = rootDir.listFiles().map { file ->
			object : Map.Entry<String, NodeFile> {
				override val key: String = file.nameWithoutExtension
				override val value: NodeFile = NodeFile(file)
			}
		}.toSet()
	override val keys: Set<String>
		get() = entries.map { it.key }.toSet()
	override val size: Int
		get() = entries.size
	override val values: Collection<NodeFile>
		get() = entries.map { it.value }.toSet()

	override fun containsKey(key: String): Boolean {
		return keys.contains(key)
	}

	override fun containsValue(value: NodeFile): Boolean {
		return values.contains(value)
	}

	override fun isEmpty(): Boolean {
		return entries.isEmpty()
	}

	override operator fun get(key: String): NodeFile {
		return NodeFile(rootDir.resolve("$key.json"))
	}
}

@Suppress("MayBeConstant")
val configRoot = ".config"
val configPath = "$configRoot/config.properties"
val config: Properties
	get() {
		return Properties().apply {
			load(File(configPath).inputStream())
		}
	}

val templatePath get() = "$configRoot/template.json"
var template: JSONObject
	get() {
		return File(templatePath).readText().run(::JSONObject)
	}
	set(value) {
		File(templatePath).writeText(value.toString())
	}

val nodesDirPath get() = "$configRoot/nodes"
val nodesDir: NodeDir get() = NodeDir(nodesDirPath)

fun SSRNode.toJson(tmplt: JSONObject = template): JSONObject {
	return JSONObject(tmplt, tmplt.keySet().toTypedArray()).also {
		json.let { obj ->
			obj.keys().forEach { name ->
				it.put(name, obj[name])
			}
		}
	}
}

fun node(group: String, name: String): SSRNode {
	val nodeFile = nodesDir[group].run { takeIf { it.file.exists() } ?: throw NoSuchFileException(file) }

	nodeFile.load()

	return nodeFile[name] ?: throw NoSuchElementException("No such node: $name")
}