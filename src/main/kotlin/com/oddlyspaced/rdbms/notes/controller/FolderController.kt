package com.oddlyspaced.rdbms.notes.controller

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oddlyspaced.rdbms.notes.db.DbConnection
import com.oddlyspaced.rdbms.notes.entity.Folder
import com.oddlyspaced.rdbms.notes.entity.Item
import com.oddlyspaced.rdbms.notes.entity.Note
import com.oddlyspaced.rdbms.notes.entity.Response
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
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

    @PostMapping("/note/update")
    fun updateNote(
        @RequestParam(value = "note") noteJson: String,
        @RequestParam(value = "folderId", required = false) folderId: Int?
    ): Response {
        val type = object: TypeToken<Note>(){}.type
        val note: Note = gson.fromJson(noteJson, type)
        return if (note.id == -1) {
            addNoteInDb(note, folderId ?: 1)
        } else {
            updateNoteInDb(note)
        }
    }

    private fun updateNoteInDb(note: Note): Response {
        try {
            val statement = DbConnection.connection.createStatement()
            val resultSet = statement.execute("UPDATE Note SET Title=\"${note.title}\" WHERE NoteID = ${note.id};")
        }
        catch (e: Exception) {
            e.printStackTrace()
            return Response("Update Unsuccessful")
        }
        return Response("Updated Successfully ${note.title}")
    }

    private fun addNoteInDb(note: Note, folderId: Int): Response {
        try {
            val statement = DbConnection.connection.createStatement()
            // INSERT INTO Note(Title, Date, InFolder) VALUES('Test Note', '202006191840', 1)
            statement.execute("INSERT INTO Note(Title, Date, InFolder) VALUES('${note.title}', '${note.date}', $folderId);")
            saveContentOfNote(note.content, searchNote(note))
        }
        catch (e: Exception) {
            e.printStackTrace()
            return Response("Add Unsuccessful")
        }
        return Response("Added Successfully ${note.title}")
    }

    private fun searchNote(note: Note): Int {
        try {
            val statement = DbConnection.connection.createStatement()
            val resultSet = statement.executeQuery("SELECT NoteID FROM Note WHERE Title='${note.title}' AND Date='${note.date}';")
            while (resultSet.next()) {
                return resultSet.getInt(1)
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    private fun saveContentOfNote(items: List<Item>, noteId: Int) {
        // INSERT INTO Item(Content, IsDone, InNote) VALUES('Test Content', FALSE, 1);
        try {
            items.forEach { item ->
                val statement = DbConnection.connection.createStatement()
                statement.execute("INSERT INTO Item(Content, IsDone, InNote) VALUES('${item.content}', ${item.isDone}, $noteId);")
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }


}