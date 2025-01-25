package parser

import parser.question.*
import ut.isep.management.model.entity.AssignmentType
import java.io.File


class QuestionParser(
    private val frontmatterParser: FrontmatterParser = FrontmatterParser()
) {

    /**
     * @throws QuestionParsingException
     */
    fun parseFile(questionFile: File): Question {
        try {
            val content = questionFile.readText()
            val (metadata, body) = frontmatterParser.parse(content, questionFile.path)
            return when (metadata.type) {
                AssignmentType.MULTIPLE_CHOICE -> parseMultipleChoiceQuestion(body, metadata)
                AssignmentType.OPEN -> parseOpenQuestion(body, metadata)
                AssignmentType.CODING -> {
                    val (code, test, secretTest) = parseCodingDir(questionFile.parentFile, metadata)
                    parseCodingQuestion(code, test, secretTest, body, metadata)
                }
            }
        } catch (e: Exception) {
            throw QuestionParsingException(
                message = "Failed to parse question.",
                context = "Input: ${questionFile.path}\nError: ${e.message}",
                cause = e
            )
        }
    }

    private fun parseCodingDir(codingDir: File, metadata: Frontmatter): Triple<CodingFile, CodingFile, CodingFile> {
        val mdFiles: Array<File>? = codingDir.listFiles { file -> file.extension == "md" }
        requireNotNull(mdFiles) {
            "Failed to list files in directory: ${codingDir.absolutePath}"
        }
        require(mdFiles.size == 1) {
            "Must provide exactly one description markdown file per coding question directory. Found ${mdFiles.size} files."
        }
        val codeFile = CodingFile(metadata.codeFilename!!, codingDir.resolve(metadata.codeFilename).readText())
        val testFile = CodingFile(metadata.testFilename!!, codingDir.resolve(metadata.testFilename).readText())
        val secretTestFile =
            CodingFile(metadata.secretTestFilename!!, codingDir.resolve(metadata.secretTestFilename).readText())
        return Triple(codeFile, testFile, secretTestFile)
    }

    fun parseCodingQuestion(code: CodingFile, test: CodingFile, secretTest: CodingFile, body: String, metadata: Frontmatter): CodingQuestion {
        try {
            return CodingQuestion(
                id = metadata.id,
                tags = metadata.tags,
                filePath = metadata.originalFilePath,
                availablePoints = metadata.availablePoints,
                availableSeconds = metadata.availableSeconds,
                description = body,
                code = code,
                testCode = test,
                secretTestCode = secretTest
            )
        } catch (e: Exception) {
            throw QuestionParsingException(
                message = "Failed to parse question.",
                context = "Input: ${metadata.originalFilePath}\nError: ${e.message}",
                cause = e
            )
        }
    }

    // Parsing logic for question types remains unchanged
    fun parseMultipleChoiceQuestion(body: String, metadata: Frontmatter): MultipleChoiceQuestion {
        val descriptionRegex = """^(.*?)(?=\n- |\$)""".toRegex(RegexOption.DOT_MATCHES_ALL)
        val optionsRegex = """-\s*\[([xX ])]\s*(.*?)\s*$""".toRegex(RegexOption.MULTILINE)

        val description = descriptionRegex.find(body)?.groupValues?.getOrNull(1)?.trim().orEmpty()
        val options = optionsRegex.findAll(body).map { matchResult ->
            val (isChecked, text) = matchResult.destructured
            MultipleChoiceQuestion.Option(
                text = text.trim(),
                isCorrect = isChecked.equals("x", ignoreCase = true)
            )
        }.toList()

        return MultipleChoiceQuestion(
            id = metadata.id,
            tags = metadata.tags,
            filePath = metadata.originalFilePath,
            availablePoints = metadata.availablePoints,
            availableSeconds = metadata.availableSeconds,
            description = description,
            options = options
        )
    }

    fun parseOpenQuestion(body: String, metadata: Frontmatter): OpenQuestion {
        return OpenQuestion(
            id = metadata.id,
            tags = metadata.tags,
            filePath = metadata.originalFilePath,
            availablePoints = metadata.availablePoints,
            availableSeconds = metadata.availableSeconds,
            description = body
        )
    }
}
