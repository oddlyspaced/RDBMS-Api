package com.oddlyspaced.rdbms.notes.entity

data class CompleteNote(
    val note: Note,
    val content: List<Item>,
)