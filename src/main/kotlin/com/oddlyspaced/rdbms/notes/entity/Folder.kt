package com.oddlyspaced.rdbms.notes.entity

import com.google.gson.annotations.SerializedName

data class Folder(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
)