package ut.isep.management.model.entity

import jakarta.persistence.*

@Entity
@DiscriminatorValue("SOLVED_CODING")
open class SolvedAssignmentCoding(
    id: SolvedAssignmentId = SolvedAssignmentId(),
    invite: Invite? = null,
    assignment: Assignment? = null,

    @Column(columnDefinition = "text")
    open var userCode: String? = null,

    @Column(columnDefinition = "text")
    open var testCode: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "solvedAssignmentCoding")
    open var testResults: MutableList<TestResult> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "solvedAssignmentCoding")
    open var secretTestResults: MutableList<TestResult> = mutableListOf(),
) : SolvedAssignment(id, invite, assignment) {

    override var scoredPoints: Int? = null
        get() {
            return if (testResults.count { it.passed } != testResults.size) {
                0
            } else {
                val secretTestPercentage = secretTestResults.count { it.passed }.toDouble() / secretTestResults.size
                (assignment!!.availablePoints!! * secretTestPercentage).toInt()
            }
        }

    fun addTestResults(testResults: List<TestResult>) {
    testResults.forEach { testResult ->
        testResult.solvedAssignmentCoding = this
        this.testResults.add(testResult)
        }
    }

    fun addSecretTestResults(testResults: List<TestResult>) {
        testResults.forEach { testResult ->
            testResult.solvedAssignmentCoding = this
            this.secretTestResults.add(testResult)
        }
    }
}
