import java.io.File

const val commandName = "ssrman"

data class Command(val name : String, val info : String, val usage : String = "") {
	val fullUsage : String get() = "$commandName $name${if (usage.isEmpty()) "" else " $usage"}"
	val helpString : String get() = "$fullUsage: $info"
}

val commands = listOf(
		"help" to Command("help", "Show help document"),
		"init" to Command("init", "Check configuration file and fix them"),
		"update" to Command("update", "Update nodes"),
		"groups" to Command("groups", "Show all ssr node groups"),
		"list" to Command("list", "Show specified group's ssr nodes", "<group name>"),
		"set" to Command("set", "Set target node to nodes.json", "<group name> <node name>"),
		"info" to Command("info", "Show node info", "<group name> <node name>")
).toMap()

fun help() {
//    """
//        $commandName groups: Show all ssr nodes
//        $commandName set <name>: Set target node to config.json
//        $commandName info <name>: Show node info
//    """.trimIndent().run(::println)

	commands.forEach {
		it.value.helpString.run(::println)
	}
}

fun unknownCommand(c : String) {
	"""
       Command '$c' not found, use '$commandName help' for more information
    """.trimIndent()
}

fun update() {
	fun updateNodes(code: String) {
		val ssrs = listOfSSR(code)
		ssrs.forEach { k, v ->
			nodesDir[k].also { file ->
				if (file.file.exists().not()) file.file.createNewFile()

				file.putAll(v)
			}.save()
		}
	}

	val url = config.getProperty("url") ?: throw NoSuchElementException("Not found property: url")
	println("Downloading...")
	val content = String(get(url))
	println("Updating...")
	updateNodes(content)
	println("Done.")
}

fun init() {
	mapOf<String, Function1<String, Unit>>(
			configRoot to { name ->
				File(name).mkdir()
			},
			configPath to { name ->
				File(name).writeText("# qwq")
			},
			templatePath to { path ->
				File(path).writeText("""{
    "server": "",
    "server_ipv6": "::",
    "server_port": 0,
    "local_address": "127.0.0.1",
    "local_port": 1080,

    "password": "",
    "method": "",
    "protocol": "",
    "protocol_param": "",
    "obfs": "",
    "obfs_param": "",
    "speed_limit_per_con": 0,
    "speed_limit_per_user": 0,

    "additional_ports" : {},
    "additional_ports_only" : false,
    "timeout": 120,
    "udp_timeout": 60,
    "dns_ipv6": false,
    "connect_verbose_info": 0,
    "redirect": "",
    "fast_open": false
}""")
			},
			nodesDirPath to { path ->
				File(path).mkdir()
			}
	).forEach { (name, init) ->
		println("Checking $name...")
		if (File(name).exists().not()) {
			println("Initializing $name")
			init(name)
		}

		println("Done.")
	}
}

fun groups() {
	nodesDir.forEach { k, f ->
		println("$k: Contains ${f.apply { load() }.size} nodes")
	}
}

fun list(group: String) {
	println("$group:")
	nodesDir[group].apply { load() }.forEach { name, _ ->
		println("$indent$name")
	}
}

fun set(group: String, name: String) {
	val path = config.getProperty("path") ?: throw NoSuchElementException("Please set ssr install path")
	val node = node(group, name)
	File(path).writeText(node.toJson().toString())
	println("Wrote node $name")
}

fun info(group: String, name: String) {
	val node = node(group, name)
	println("$name: ${node.copy(password = "*")}")
}