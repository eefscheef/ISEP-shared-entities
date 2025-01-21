package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class TestResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,

    open var name: String = "",
    open var message: String? = null,
    open var passed: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "assignment_id"),
        JoinColumn(name = "invite_id")
    )
    open var solvedAssignmentCoding: SolvedAssignmentCoding? = null,
) : BaseEntity<Long> {

    companion object {
        fun createTestResult(name: String, message: String? = null, passed: Boolean, solvedAssignmentCoding: SolvedAssignmentCoding): TestResult {
            val testResult: TestResult = if (message != null) {
                TestResult(name = name, message = message, passed = passed, solvedAssignmentCoding = solvedAssignmentCoding)
            } else {
                TestResult(name = name, passed = passed, solvedAssignmentCoding = solvedAssignmentCoding)
            }
            return testResult
        }
    }
}