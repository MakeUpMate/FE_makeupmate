package com.example.makeupmate.data

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("recom2")
	val recom2: Int? = null,

	@field:SerializedName("bestMatch")
	val bestMatch: Int? = null,

	@field:SerializedName("recom3")
	val recom3: Int? = null,

	@field:SerializedName("hex1")
	val hex1: String? = null,

	@field:SerializedName("hex2")
	val hex2: String? = null,

	@field:SerializedName("hex3")
	val hex3: String? = null,

	@field:SerializedName("prediction")
	val prediction: Int? = null,

	@field:SerializedName("skintone")
	val skintone: String? = null
)
