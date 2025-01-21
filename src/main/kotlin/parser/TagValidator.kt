package parser

class TagValidator(private val config: Config) {
    fun validate(tags: List<String>) {
        tags.forEach { tag ->
            if (tag.lowercase() !in config.tagOptions.map(String::lowercase))
            { throw QuestionParsingException("Invalid tag provided: $tag is not present in config file")
            }
        }
    }
}
