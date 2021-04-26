package com.oddlyspaced.rdbms.notes.entity

import com.google.gson.annotations.SerializedName
import com.oddlyspaced.rdbms.notes.entity.Item

data class Note(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("content")
    val content: List<Item>,
)