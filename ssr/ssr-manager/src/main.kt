fun main(args: Array<String>) {
    if (args.isEmpty()) help() else {
        when(val c = args.first()) {
            "help" -> help()
            "list" -> list()
            "set" -> set(args.getOrNull(1) ?: throw IllegalArgumentException("Usage: ${commands.first { it.name == "set" }.fullUsage}"))
            "info" -> info(args.getOrNull(1) ?: throw IllegalArgumentException("Usage: ${commands.first { it.name == "info" }.fullUsage}"))
            "update" -> update()
            "init" -> init()

            else -> unknownCommand(c)
        }
    }
}