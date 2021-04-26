package com.oddlyspaced.notes.modal

import com.google.gson.annotations.SerializedName

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