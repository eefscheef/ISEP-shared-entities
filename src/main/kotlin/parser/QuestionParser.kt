package parser

import parser.question.*
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
            return when (metadata) {
                is OpenFrontmatter -> parseOpenQuestion(body, metadata)
                is CodingFrontmatter -> {
                    val codeFiles = parseCodingDir(questionFile.parentFile, metadata)
                    parseCodingQuestion(codeFiles, body, metadata)
                }
                else -> { parseMultipleChoiceQuestion(body, metadata) }
            }
        } catch (e: Exception) {
            throw QuestionParsingException(
                message = "Failed to parse question.",
                context = "Input: ${questionFile.path}\nError: ${e.message}",
                cause = e
            )
        }
    }

    private fun parseCodingDir(codingDir: File, metadata: CodingFrontmatter): CodeQuestionFiles {
        val mdFiles: Array<File>? = codingDir.listFiles { file -> file.extension == "md" }
        requireNotNull(mdFiles) {
            "Failed to list files in directory: ${codingDir.absolutePath}"
        }
        require(mdFiles.size == 1) {
            "Must provide exactly one description markdown file per coding question directory. Found ${mdFiles.size} files."
        }
        val codeFile = CodingFile(metadata.codeFilename, codingDir.resolve(metadata.codeFilename).readText())
        val testFile = CodingFile(metadata.testFilename, codingDir.resolve(metadata.testFilename).readText())
        val secretTestFile =
            CodingFile(metadata.secretTestFilename, codingDir.resolve(metadata.secretTestFilename).readText())
        val referenceCode = metadata.referenceCodeFilename?.let {CodingFile(it, codingDir.resolve(it).readText())}
        val referenceTest = metadata.referenceTestFilename?.let {CodingFile(it, codingDir.resolve(it).readText())}
        return CodeQuestionFiles(codeFile, testFile, secretTestFile, referenceCode, referenceTest)
    }

    fun parseCodingQuestion(codeQuestionFiles: CodeQuestionFiles, body: String, metadata: CodingFrontmatter): CodingQuestion {
        try {
            return CodingQuestion(
                id = metadata.id,
                tags = metadata.tags,
                filePath = metadata.originalFilePath,
                availablePoints = metadata.availablePoints,
                availableSeconds = metadata.availableSeconds,
                language = metadata.language,
                description = body,
                files = codeQuestionFiles
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

    fun parseOpenQuestion(body: String, metadata: OpenFrontmatter): OpenQuestion {
        return OpenQuestion(
            id = metadata.id,
            tags = metadata.tags,
            filePath = metadata.originalFilePath,
            availablePoints = metadata.availablePoints,
            availableSeconds = metadata.availableSeconds,
            description = body,
            referenceAnswer = metadata.referenceAnswer
        )
    }
}
