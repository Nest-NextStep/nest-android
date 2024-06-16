package com.bangkit.nest.data.remote.request

import com.google.gson.annotations.SerializedName

data class ProfileRequest(

    @SerializedName("user_currentSchool")
    var userCurrentSchool: String? = null,

    @SerializedName("user_engNat")
    var userEngNat: String? = null,

    @SerializedName("user_religion")
    var userReligion: String? = null,

    @SerializedName("user_education")
    var userEducation: String? = null,

    @SerializedName("user_fullName")
    var userFullName: String? = null,

    @SerializedName("user_gender")
    var userGender: String? = null,

    @SerializedName("user_birthData")
    var userBirthData: String? = null,

    var username: String? = null,

    @SerializedName("user_voted")
    var userVoted: String? = null,
)

data class ChangePasswordRequest(
    val newPassword: String
)
