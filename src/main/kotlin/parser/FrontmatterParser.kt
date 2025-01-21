package parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.Reader


class FrontmatterParser(private val validator: TagValidator? = null) {
    private val objectMapper = ObjectMapper(YAMLFactory())

    fun split(inputReader: Reader): Pair<String, String> {
        val input = inputReader.readText()
        val parts = input.split("---", limit = 3).map { it.trim() }.filter { it.isNotEmpty() }
        if (parts.size != 2) {
            throw QuestionParsingException("Invalid question format. Must contain frontmatter and body.")
        }
        val frontmatter = parts[0]
        val body = parts[1]
        return frontmatter to body
    }

    /**
     * Parses the input reader and returns the frontmatter metadata and body.
     * @throws QuestionParsingException
     */
    fun parse(inputReader: Reader, filePath: String): Pair<Frontmatter, String> {
        val (frontmatterPart, body) = split(inputReader)
        val metadata: Frontmatter = objectMapper.readValue(frontmatterPart)
        metadata.id = QuestionIDUtil.parseQuestionID(filePath)
        metadata.originalFilePath = filePath
        validator?.validate(metadata.tags) // Validate if a validator is provided
        return metadata to body
    }
}
