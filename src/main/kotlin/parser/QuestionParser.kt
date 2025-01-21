package parser

import parser.question.*
import ut.isep.management.model.entity.AssignmentType
import java.io.File
import java.io.FileInputStream
import java.io.Reader


class QuestionParser(
    private val config: Config,
    private val frontmatterParser: FrontmatterParser = FrontmatterParser(config)
) {

    /**
     * @throws QuestionParsingException
     */
    fun parse(input: Reader, filePath: String): Question {
        try {
            val (metadata, body) = frontmatterParser.parse(input, filePath)
            return when (metadata.type) {
                AssignmentType.MULTIPLE_CHOICE -> parseMultipleChoiceQuestion(body, metadata)
                AssignmentType.OPEN -> parseOpenQuestion(body, metadata)
                AssignmentType.CODING -> throw QuestionParsingException(
                    message = "Trying to parse coding question in theoretical questions directory.",
                    context = "Input: $filePath\n",
                )
            }
        } catch (e: Exception) {
            throw QuestionParsingException(
                message = "Failed to parse question.",
                context = "Input: ${filePath}\nError: ${e.message}",
                cause = e
            )
        }
    }

    fun parseCodingDirectory(directory: File): CodingQuestion {
        try {
            val mdFiles: Array<File>? = directory.listFiles { file -> file.extension == "md" }
            requireNotNull(mdFiles) {
                "Failed to list files in directory: ${directory.absolutePath}"
            }
            require(mdFiles.size == 1) {
                "Must provide exactly one description markdown file per coding question directory. Found ${mdFiles.size} files."
            }
            val mdFile: File = mdFiles[0]
            FileInputStream(mdFile).bufferedReader().use { br ->
                val (metadata, body) = frontmatterParser.parse(br, mdFile.name)
                val codeFile = CodingFile(metadata.codeFilename!!, directory.resolve(metadata.codeFilename).readText())
                val testFile = CodingFile(metadata.testFilename!!, directory.resolve(metadata.testFilename).readText())
                val secretTestFile =
                    CodingFile(metadata.secretTestFilename!!, directory.resolve(metadata.secretTestFilename).readText())
                return CodingQuestion(
                    id = metadata.id,
                    tags = metadata.tags,
                    filePath = metadata.originalFilePath,
                    description = body,
                    code = codeFile,
                    testCode = testFile,
                    secretTestCode = secretTestFile
                )
            }
        } catch (e: Exception) {
            throw QuestionParsingException(
                message = "Failed to parse question.",
                context = "Input: ${directory.path}\nError: ${e.message}",
                cause = e
            )
        }
    }


    // Parsing logic for question types remains unchanged
    private fun parseMultipleChoiceQuestion(body: String, metadata: Frontmatter): MultipleChoiceQuestion {
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
            description = description,
            options = options
        )
    }

    private fun parseOpenQuestion(body: String, metadata: Frontmatter): OpenQuestion {
        return OpenQuestion(
            id = metadata.id,
            tags = metadata.tags,
            filePath = metadata.originalFilePath,
            description = body
        )
    }
}
