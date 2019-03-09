import java.io.File

const val commandName = "ssrman"

data class Command(val name : String, val info : String, val usage : String = "") {
	val fullUsage : String get() = "$commandName $name${if (usage.isEmpty()) "" else " $usage"}"
	val helpString : String get() = "$fullUsage: $info"
}

val commands = listOf(
		Command("help", "Show help document"),
		Command("init", "Check configuration file and fix them"),
		Command("update", "Update nodes"),
		Command("list", "Show all ssr nodes"),
		Command("set", "Set target node to nodes.json", "<name>"),
		Command("info", "Show node info", "<name>")
)

fun help() {
//    """
//        $commandName list: Show all ssr nodes
//        $commandName set <name>: Set target node to config.json
//        $commandName info <name>: Show node info
//    """.trimIndent().run(::println)

	commands.forEach {
		it.helpString.run(::println)
	}
}

fun unknownCommand(c : String) {
	"""
       Command '$c' not found, use '$commandName help' for more information
    """.trimIndent()
}

fun update() {
	val url = config.getProperty("url") ?: throw NoSuchElementException("Not found property: url")
	println("Downloading...")
	val content = String(get(url))
	println("Updating...")
	nodes = content.run(::listOfSSR)
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
			nodesPath to { path ->
				File(path).writeText("{}")
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

fun list() {
	nodes.forEach { k, _ ->
		println(k)
	}
}

fun set(name : String) {
	val path = config.getProperty("path") ?: throw NoSuchElementException("Please set ssr install path")
	val node = nodes[name] ?: throw NoSuchElementException("No such node name: $name")
	File(path).writeText(node.toJson().toString())
	println("Wrote node $name")
}

fun info(name : String) {
	val node = nodes[name] ?: throw NoSuchElementException("No such node name: $name")
	println("$name: ${node.copy(password = "*")}")
}