package com.bangkit.nest.data.remote.response

import com.google.gson.annotations.SerializedName

data class QuestionsResponse(

	@field:SerializedName("optionData")
	val optionData: List<OptionDataItem?>? = null,

	@field:SerializedName("questionsData")
	val questionsData: List<QuestionsDataItem?>? = null
)

data class QuestionsDataItem(

	@field:SerializedName("question_text")
	val questionText: String? = null,

	@field:SerializedName("question_id")
	val questionId: String? = null
)

data class OptionDataItem(

	@field:SerializedName("option_text")
	val optionText: String? = null,

	@field:SerializedName("option_code")
	val optionCode: Int? = null
)

data class ResultsResponse(

	@field:SerializedName("ResultResponse")
	val resultResponse: List<ResultsResponseItem?>? = null
)

data class ResultsResponseItem(

	@field:SerializedName("user_major_date")
	val userMajorDate: String? = null,

	@field:SerializedName("major_name")
	val majorName: String? = null
)

data class TestResultResponse(

	@field:SerializedName("major_description")
	val majorDescription: String? = null,

	@field:SerializedName("major_name")
	val majorName: String? = null
)
