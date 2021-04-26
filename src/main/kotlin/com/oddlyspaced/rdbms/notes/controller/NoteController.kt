package com.oddlyspaced.rdbms.notes.controller

import com.oddlyspaced.rdbms.notes.db.DbConnection
import com.oddlyspaced.rdbms.notes.entity.CompleteNote
import com.oddlyspaced.rdbms.notes.entity.Item
import com.oddlyspaced.rdbms.notes.entity.Note
import com.oddlyspaced.rdbms.notes.entity.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class NoteController {

    @GetMapping("/note/get")
    fun getNotesFromFolder(@RequestParam(value = "folderId", required = false) folderId: Int?) = fetchNotesFromFolder(folderId?: 1)

    @GetMapping("/note/add")
    fun addNote(
        @RequestParam(value = "title") title: String,
        @RequestParam(value = "folderId", required = false) folderId: Int?
    ) = addNoteToDb(title, folderId?: 1)

    @GetMapping("/note/fetch")
    fun fetchNote(
        @RequestParam(value = "noteId") noteId: Int
    ) = fetchFullNote(noteId)

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
                        resultSet.getString(3),
                    )
                )
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return notes
    }

    private fun addNoteToDb(noteTitle: String, folderId: Int): Response {
        return try {
            val statement = DbConnection.connection.createStatement()
            if (FolderController().checkFolderExistsInDb(folderId).message == "Exists") {
                statement.execute("INSERT INTO Note (Title, InFolder) VALUES('$noteTitle', $folderId);")
                Response("Note added successfully")
            }
            else {
                Response("Folder does not exist for note!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Response("Unable to add Note!")
        }
    }

    private fun fetchFullNote(noteId: Int): CompleteNote? {
        try {
            val content = mutableListOf<Item>()
            val resultSetContent = DbConnection.connection.createStatement().executeQuery("SELECT * FROM Item WHERE InNote = $noteId;")
            while (resultSetContent.next()) {
                content.add(
                    Item(
                        resultSetContent.getInt(1),
                        resultSetContent.getString(2),
                        resultSetContent.getBoolean(3),
                    )
                )
            }

            // SELECT * FROM Note WHERE NoteID = 1;
            var note = Note(-1, "", "")
            val resultSetNote = DbConnection.connection.createStatement().executeQuery("SELECT * FROM Item WHERE InNote = $noteId;")
            while (resultSetNote.next()) {
                note = Note(
                    resultSetNote.getInt(1),
                    resultSetNote.getString(2),
                    resultSetNote.getString(3),
                )
            }
            return CompleteNote(note, content)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}