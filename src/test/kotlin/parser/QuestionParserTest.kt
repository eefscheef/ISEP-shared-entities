package parser

import org.junit.jupiter.api.Test
import parser.question.MultipleChoiceQuestion
import parser.question.OpenQuestion
import java.io.StringReader
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QuestionParserTest {

    private val parser = QuestionParser(
        Config(
            tagOptions = listOf("Frontend Developer", "Backend Developer", "System Design", "Deezveloper"),
            questionOptions = listOf("multiple-choice", "open")
        )
    )

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
        val inputReader = StringReader(input)

        val question = parser.parse(inputReader, "questionFileWithId_qid12.md") as MultipleChoiceQuestion

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
        val inputReader = StringReader(input)
        val question = parser.parse(inputReader, "exampleQuestionWithNoID") as MultipleChoiceQuestion

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
        val inputReader = StringReader(input)

        val question = parser.parse(inputReader, "sampleQuestionWithNoId") as OpenQuestion

        assertEquals("What is the difference between a stack and a queue?", question.description)
    }

}
