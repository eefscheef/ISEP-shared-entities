package parser

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import parser.question.CodingQuestion
import parser.question.MultipleChoiceQuestion
import parser.question.OpenQuestion
import java.io.StringReader
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class QuestionParserTest {

    @TempDir
    lateinit var tempDir: Path

    private lateinit var parser: QuestionParser

    @BeforeEach
    fun setUp() {
        val config = Config(
            tagOptions = listOf("Frontend Developer", "Backend Developer", "System Design", "Deezveloper", "Java"),
            questionOptions = listOf("multiple-choice", "open")
        )
        parser = QuestionParser(config)
    }

    @Test
    fun `test successfully parsing a coding question directory`() {
        // Arrange: create tempdir with coding question files
        val mdFile = tempDir.resolve("description.md").toFile().apply {
            writeText("""
                ---
                type: coding
                tags:
                  - Backend Developer
                  - Java
                code: HelloWorld.java
                test: HelloWorldTest.java
                secret-test: HelloWorldSecretTest.java
                ---
                This is a description for a coding question.
            """.trimIndent())
        }
        val code = "public class HelloWorld { public static void main(String[] args) { System.out.println(\"Hello World\"); } }"
        tempDir.resolve("HelloWorld.java").toFile().apply {
            writeText(code)
        }
        val testCode = "public class HelloWorldTest { /* Test code here */ }"
        tempDir.resolve("HelloWorldTest.java").toFile().apply {
            writeText(testCode)
        }
        val secretTestCode = "public class HelloWorldSecretTest { /* Secret test code here */ }"
        tempDir.resolve("HelloWorldSecretTest.java").toFile().apply {
            writeText(secretTestCode)
        }

        // Act: Parse the coding question dir
        val codingQuestion = parser.parseFile(mdFile) as CodingQuestion

        // Assert the parsed data equals the written data
        assertEquals("This is a description for a coding question.", codingQuestion.description)
        assertEquals(2, codingQuestion.tags.size)
        assertEquals("HelloWorld.java", codingQuestion.code.filename)
        assertEquals(code, codingQuestion.code.code)
        assertEquals("HelloWorldTest.java", codingQuestion.testCode.filename)
        assertEquals(testCode, codingQuestion.testCode.code)
        assertEquals("HelloWorldSecretTest.java", codingQuestion.secretTestCode.filename)
        assertEquals(secretTestCode, codingQuestion.secretTestCode.code)
    }

    @Test
    fun `test failure when there are multiple markdown files`() {
        // Arrange multiple markdown files in a single coding dir
        val mdFile1 = tempDir.resolve("description1.md").toFile().apply {
            writeText("""
                ---
                type: coding
                tags:
                  - Backend Developer
                code: HelloWorld.java
                test: HelloWorldTest.java
                secret-test: HelloWorldSecretTest.java
                ---
                First description.
            """.trimIndent())
        }
        val mdFile2 = tempDir.resolve("description2.md").toFile().apply {
            writeText("""
                ---
                type: coding
                tags:
                  - Backend Developer
                code: HelloWorld.java
                test: HelloWorldTest.java
                secret-test: HelloWorldSecretTest.java
                ---
                Second description.
            """.trimIndent())
        }

        // Assert failure when parsing this as a coding question
        assertFailsWith<QuestionParsingException> {
            parser.parseFile(mdFile1.parentFile)
        }
    }

    @Test
    fun `test failure when there are no markdown files`() {
        // Arrange a coding question directory without markdown filnes
        val codeFile = tempDir.resolve("HelloWorld.java").toFile().apply {
            writeText("public class HelloWorld { public static void main(String[] args) { System.out.println(\"Hello World\"); } }")
        }
        tempDir.resolve("HelloWorldTest.java").toFile().apply {
            writeText("public class HelloWorldTest { /* Test code here */ }")
        }
        tempDir.resolve("HelloWorldSecretTest.java").toFile().apply {
            writeText("public class HelloWorldSecretTest { /* Secret test code here */ }")
        }

        // Assert an error is thrown
        assertFailsWith<QuestionParsingException> {
            parser.parseFile(codeFile.parentFile)
        }
    }

    @Test
    fun `test parsing single answer multiple-choice question`() {
        val input = """
            ---
            type: multiple-choice
            tags:
              - Frontend Developer
              - Backend Developer
            ---
            What is the difference between a stack and a queue?
            
            - [ ] A stack is FIFO, a queue is LIFO.
            - [x] A stack is LIFO, a queue is FIFO.
            - [ ] vBoth are FIFO.
            - [ ] Both are LIFO.
        """.trimIndent()
        val mdFile1 = tempDir.resolve("mcQuestion_qid12.md").toFile().apply {
            writeText(input)
        }
        val question = parser.parseFile(mdFile1) as MultipleChoiceQuestion

        assertEquals("What is the difference between a stack and a queue?", question.description)
        assertEquals(4, question.options.size)
        assertEquals(12, question.id)
        assertTrue(question.options.any { it.text == "A stack is LIFO, a queue is FIFO." && it.isCorrect })
        assertTrue(question.options.filter { it.isCorrect }.size == 1)
    }

    @Test
    fun `test parsing multiple answers multiple-choice question`() {
        val input = """
            ---
            type: multiple-choice
            
            tags:
            - Backend Developer
            - System Design
            ---
            
            Why does the monolithic architecture not eat the microservice oriented architecture?
            
            - [x] Monolithic architecture is more scalable than microservices.
            - [X] Microservices architecture allows independent scaling of components, while monolithic does not.
            - [x] Microservices architecture is simpler to deploy than monolithic.
            - [ ] Monolithic architecture is easier to maintain than microservices.
            - [ ] Test.
        """.trimIndent()
        val inputFile = tempDir.resolve("testFileName").toFile().apply {
            writeText(input)
        }
        val question = parser.parseFile(inputFile) as MultipleChoiceQuestion

        assertEquals(
            "Why does the monolithic architecture not eat the microservice oriented architecture?",
            question.description
        )
        assertEquals(5, question.options.size)
        assertTrue(question.options.filter { it.isCorrect }.size == 3)
    }


    @Test
    fun `test parsing open question`() {
        val input = """
            ---
            type: open
            tags: 
              - Deezveloper
            ---
            What is the difference between a stack and a queue?
        """.trimIndent()
        val inputFile = tempDir.resolve("filename").toFile().apply {
            writeText(input)
        }
        val question = parser.parseFile(inputFile) as OpenQuestion

        assertEquals("What is the difference between a stack and a queue?", question.description)
    }

}
