fun main(args: Array<String>) {
    fun exception(command: Command): Nothing {
        throw IllegalArgumentException("Usage: ${command.fullUsage}")
    }

    fun argv(index: Int, command: Command): String {
        return args.getOrNull(index) ?: exception(command)
    }

    if (args.isEmpty()) help() else {
        when(val c = args.first()) {
            "help" -> help()
            "groups" -> groups()
            "list" -> list(argv(1, commands.getValue("list")))
            "set" -> commands.getValue("set").let { set(argv(1, it), argv(2, it)) }
            "info" -> commands.getValue("info").let { info(argv(1, it), argv(2, it)) }
            "update" -> update()
            "init" -> init()

            else -> unknownCommand(c)
        }
    }
}