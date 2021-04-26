package com.oddlyspaced.rdbms.notes.entity

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id")
    val id: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("isDone")
    val isDone: Boolean,
)