package com.oddlyspaced.rdbms.notes.controller

import com.oddlyspaced.rdbms.notes.db.DbConnection
import com.oddlyspaced.rdbms.notes.entity.Note
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class NoteController {

    @GetMapping("/note/get")
    fun getNotesFromFolder(@RequestParam(value = "folderId", required = false) folderId: Int?) = fetchNotesFromFolder(folderId?: 1)

    private fun fetchNotesFromFolder(folderId: Int): MutableList<Note> {
        val notes = mutableListOf<Note>()
        try {
            val statement = DbConnection.connection.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM Note WHERE InFolder = $folderId;")
            while (resultSet.next()) {
                notes.add(
                    Note(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                    )
                )
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return notes
    }
}