package com.bangkit.nest.data.remote.response

import com.google.gson.annotations.SerializedName

data class AllMajorResponse(

	@field:SerializedName("majorsAll")
	val majorsAll: List<MajorItem>,

	@field:SerializedName("majorRecommended")
	val majorRecommended: List<MajorItem>
)

data class MajorItem(

	@field:SerializedName("major_description")
	val majorDescription: String? = null,

	@field:SerializedName("major_name")
	val majorName: String? = null,

	@field:SerializedName("major_id")
	val majorId: Int? = null,

	@field:SerializedName("major_cover")
	val majorCover: String? = null,
)

data class DetailMajorResponse(

	@field:SerializedName("major")
	val major: MajorItem? = null,

	@field:SerializedName("majorUniversity")
	val majorUniversity: List<MajorUniversityItem>,

	@field:SerializedName("majorJob")
	val majorJob: List<MajorJobItem>,

	@field:SerializedName("majorOpinion")
	val majorOpinion: List<MajorOpinionItem>
)

data class MajorUniversityItem(

	@field:SerializedName("university_id")
	val universityId: Int,

	@field:SerializedName("university_image")
	val universityImage: String? = null,

	@field:SerializedName("university_location")
	val universityLocation: String? = null,

	@field:SerializedName("university_name")
	val universityName: String,

	@field:SerializedName("university_acreditation")
	val universityAcreditation: String? = null,

	@field:SerializedName("university_link")
	val universityLink: String? = null
)

data class MajorOpinionItem(

	@field:SerializedName("opinions_content")
	val opinionsContent: String? = null,

	@field:SerializedName("opinion_id")
	val opinionId: Int? = null,

	@field:SerializedName("opinion_name")
	val opinionName: String? = null
)

data class MajorJobItem(

	@field:SerializedName("jobs_name")
	val jobsName: String? = null,

	@field:SerializedName("jobs_salary")
	val jobsSalary: String? = null,

	@field:SerializedName("jobs_id")
	val jobsId: Int? = null,

	@field:SerializedName("jobs_description")
	val jobsDescription: String? = null
)


data class FindMajorResponse(
	@field:SerializedName("status")
	val status: Boolean,

	@field:SerializedName("data")
	val majors: List<MajorItem>
)

