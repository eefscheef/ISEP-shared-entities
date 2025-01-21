package parser

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import ut.isep.management.model.entity.AssignmentType


data class Frontmatter @JsonCreator constructor(
    @JsonProperty("type") val type: AssignmentType,
    @JsonProperty("tags") val tags: List<String>,
    @JsonProperty("points") val availablePoints: Int = 1,
    @JsonProperty("seconds") val availableSeconds: Int = 240,
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