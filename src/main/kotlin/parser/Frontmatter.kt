package parser

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import ut.isep.management.model.entity.AssignmentType


data class Frontmatter @JsonCreator constructor(
    @JsonProperty("type", required = true) val type: AssignmentType,
    @JsonProperty("tags", required = true) val tags: List<String>,
    @JsonProperty("points", required = true) val availablePoints: Int,
    @JsonProperty("seconds", required = true) val availableSeconds: Long,
    @JsonProperty("language") val language: String?,
    @JsonProperty("code") val codeFilename: String?,
    @JsonProperty("test") val testFilename: String?,
    @JsonProperty("secret-test") val secretTestFilename: String?
) {
    @JsonIgnore
    lateinit var originalFilePath: String
    val baseFilePath: String
        get() = QuestionIDUtil.removeQuestionIDIfPresent(originalFilePath)
    @JsonIgnore
    var id: Long? = null


    init {
        require(availablePoints > 0) {"Assignments must be worth at least 1 point"}
        require(availableSeconds > 0) {"Recommended assignment time must be at least 1 second"}

        if (type == AssignmentType.CODING) {
            require(codeFilename != null && testFilename != null && secretTestFilename != null && language != null) {
                "For a coding assignment .md file, it is necessary to provide the language, code, test, and secret-test properties"
            }
        } else {
            require(codeFilename == null && testFilename == null && secretTestFilename == null && language == null) {
                "for a non-coding assignment, there should be no language, code, test, or secret-test properties provided"
            }
        }
    }
}