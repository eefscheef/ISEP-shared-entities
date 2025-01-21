package parser

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty


data class Frontmatter @JsonCreator constructor(
    @JsonProperty("type") val type: String,
    @JsonProperty("tags") val tags: List<String>,
    @JsonProperty("availablePoints") val availablePoints: Int = 1
) {
    @JsonIgnore
    lateinit var originalFilePath: String
    val baseFilePath: String
        get() = QuestionIDUtil.removeQuestionIDIfPresent(originalFilePath)
    @JsonIgnore
    var id: Long? = null
}