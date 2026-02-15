package io.github.lunex_app.data.model

data class TagQuery(val name: String, val type: QueryType) {
    companion object {
        fun parse(input: String): List<TagQuery> {
            val tokens = input.split(Regex("\\s+")).filter { it.isNotEmpty() }
            return tokens.map {
                when {
                    it.startsWith("!+") -> TagQuery(it.drop(2), QueryType.NOT_POSITIVE)
                    it.startsWith("!-") -> TagQuery(it.drop(2), QueryType.NOT_NEGATIVE)
                    it.startsWith("!?") -> TagQuery(it.drop(2), QueryType.DEFINED)
                    it.startsWith("+") -> TagQuery(it.drop(1), QueryType.POSITIVE)
                    it.startsWith("-") -> TagQuery(it.drop(1), QueryType.NEGATIVE)
                    it.startsWith("?") -> TagQuery(it.drop(1), QueryType.UNDEFINED)
                    it.startsWith("!") -> TagQuery(it.drop(1), QueryType.NOT_POSITIVE)
                    else -> TagQuery(it, QueryType.POSITIVE)
                }
            }
        }
    }
}
