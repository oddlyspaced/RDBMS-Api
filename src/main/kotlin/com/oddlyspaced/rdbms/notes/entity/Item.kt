package com.oddlyspaced.rdbms.notes.entity

data class Item(
    val id: Int,
    val content: String,
    val isDone: Boolean,
)