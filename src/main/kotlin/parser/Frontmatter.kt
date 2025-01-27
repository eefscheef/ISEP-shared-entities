package parser

import com.fasterxml.jackson.annotation.*
import ut.isep.management.model.entity.AssignmentType

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = OpenFrontmatter::class, name = "open"),
    JsonSubTypes.Type(value = Frontmatter::class, name = "multiple-choice"),
    JsonSubTypes.Type(value = CodingFrontmatter::class, name = "coding")
)
open class Frontmatter @JsonCreator constructor(
    @JsonProperty("type", required = true) val type: AssignmentType,
    @JsonProperty("tags", required = true) val tags: List<String>,
    @JsonProperty("points", required = true) val availablePoints: Int,
    @JsonProperty("seconds", required = true) val availableSeconds: Long,
) {
    @JsonIgnore
    lateinit var originalFilePath: String
    val baseFilePath: String
        get() = QuestionIDUtil.removeQuestionIDIfPresent(originalFilePath)

    @JsonIgnore
    var id: Long? = null

    init {
        require(availablePoints > 0) { "Assignments must be worth at least 1 point" }
        require(availableSeconds > 0) { "Recommended assignment time must be at least 1 second" }
    }
}

class OpenFrontmatter @JsonCreator constructor(
    @JsonProperty("type", required = true) type: AssignmentType,
    @JsonProperty("tags", required = true) tags: List<String>,
    @JsonProperty("points", required = true) availablePoints: Int,
    @JsonProperty("seconds", required = true) availableSeconds: Long,
    @JsonProperty("referenceAnswer", required = false) val referenceAnswer: String? = null
) : Frontmatter(type, tags, availablePoints, availableSeconds) {
    init {
        require(type == AssignmentType.OPEN)
    }
}

class CodingFrontmatter @JsonCreator constructor(
    @JsonProperty("type", required = true) type: AssignmentType,
    @JsonProperty("tags", required = true) tags: List<String>,
    @JsonProperty("points", required = true) availablePoints: Int,
    @JsonProperty("seconds", required = true) availableSeconds: Long,
    @JsonProperty("language", required = true) val language: String,
    @JsonProperty("code", required = true) val codeFilename: String,
    @JsonProperty("test", required = true) val testFilename: String,
    @JsonProperty("secret-test", required = true) val secretTestFilename: String,
    @JsonProperty("reference-code", required = false) val referenceCodeFilename: String? = null,
    @JsonProperty("reference-test", required = false) val referenceTestFilename: String? = null,
) : Frontmatter(type, tags, availablePoints, availableSeconds) {
    init {
        require(type == AssignmentType.CODING)
    }
}
