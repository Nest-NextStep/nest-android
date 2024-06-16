package com.bangkit.nest.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("majorData")
	val majorData: List<MajorItem>,

	@field:SerializedName("profileData")
	val profileData: ProfileData
)

data class ProfileData(

	@field:SerializedName("user_birthDate")
	val userBirthDate: String? = null,

	@field:SerializedName("user_school")
	val userSchool: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("user_engNat")
	val userEngNat: String? = null,

	@field:SerializedName("user_religion")
	val userReligion: String? = null,

	@field:SerializedName("user_education")
	val userEducation: String? = null,

	@field:SerializedName("user_gender")
	val userGender: String? = null,

	@field:SerializedName("user_fullname")
	val userFullname: String? = null,

	@field:SerializedName("user_voted")
	val userVoted: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	var userEmail: String? = null
)

data class ProfileSuccessResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)