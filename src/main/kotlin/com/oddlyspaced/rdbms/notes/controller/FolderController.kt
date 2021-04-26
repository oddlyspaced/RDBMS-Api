package com.oddlyspaced.rdbms.notes.controller

import com.google.gson.Gson
import com.oddlyspaced.rdbms.notes.db.DbConnection
import com.oddlyspaced.rdbms.notes.entity.Folder
import com.oddlyspaced.rdbms.notes.entity.Item
import com.oddlyspaced.rdbms.notes.entity.Note
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FolderController {

    private val gson = Gson()

    @GetMapping("/folder/all")
    fun getFolders(): String = gson.toJson(readFoldersFromDb())

    private fun readFoldersFromDb(): MutableList<Folder> {
        val folders = mutableListOf<Folder>()
        try {
            val statement = DbConnection.connection.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM Folder;")
            while (resultSet.next()) {
                folders.add(
                    Folder(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                    )
                )
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return folders
    }

    @GetMapping("/folder/notes")
    fun getNotesInFolders(
        @RequestParam(value = "folderId", required = false) folderId: Int?
    ): String = gson.toJson(readNotesInFolder(folderId ?: 1))

    private fun readNotesInFolder(folderId: Int): MutableList<Note> {
        val notes = mutableListOf<Note>()
        //SELECT * FROM Note WHERE InFolder = 1
        try {
            val statement = DbConnection.connection.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM Note WHERE InFolder = $folderId")
            while (resultSet.next()) {
                notes.add(
                    Note(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        readContentOfNote(resultSet.getInt(1))
                    )
                )
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return notes
    }

    private fun readContentOfNote(noteId: Int): MutableList<Item> {
        val items = mutableListOf<Item>()
        // SELECT * FROM Item WHERE InNote = 1
        try {
            val statement = DbConnection.connection.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM Item WHERE InNote = $noteId")
            while (resultSet.next()) {
                items.add(
                    Item(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getBoolean(3)
                    )
                )
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return items
    }


}