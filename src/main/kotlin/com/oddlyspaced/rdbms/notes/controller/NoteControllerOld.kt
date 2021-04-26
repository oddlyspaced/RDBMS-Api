package com.oddlyspaced.rdbms.notes.controller

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oddlyspaced.rdbms.notes.entity.Note
import com.oddlyspaced.rdbms.notes.db.DbConnection
import com.oddlyspaced.rdbms.notes.entity.Item
import com.oddlyspaced.rdbms.notes.entity.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

//@RestController
//class NoteController {
//    val gson = Gson()
//
//    @GetMapping("/note/get")
//    fun getNotesFromFolder(@RequestParam(value = "folderId", required = false) folderId: Int?) = fetchNotesFromFolder(folderId?: 1)
//
//    @PostMapping("/note/add")
//    fun addNote(
//        @RequestParam(value = "note") note: String,
//        @RequestParam(value = "folderId", required = false) folderId: Int?
//    ) = addNoteToDb(note, folderId?: 1)
//
//    @GetMapping("/note/fetch")
//    fun fetchNote(
//        @RequestParam(value = "noteId") noteId: Int
//    ) = fetchFullNote(noteId)
//
//    private fun fetchNotesFromFolder(folderId: Int): String {
//        val notes = mutableListOf<Note>()
//        val noteIds = mutableListOf<Int>()
//        try {
//            val statement = DbConnection.connection.createStatement()
//            val resultSet = statement.executeQuery("SELECT * FROM Note WHERE InFolder = $folderId;")
//            while (resultSet.next()) {
//                noteIds.add(
//                    resultSet.getInt(1)
//                )
//            }
//        }
//        catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return gson.toJson(noteIds)
//    }
//
//    private fun addNoteToDb(noteJson: String, folderId: Int): Response {
//        val type = object: TypeToken<Note>(){}.type
//        val note: Note = gson.fromJson(noteJson, type)
//         CompleteNote(note=Note(id=69, title=Added from app, date=2020), content=[Item(id=69, content=App content, isDone=false)])
//        return try {
//            val statement = DbConnection.connection.createStatement()
//            if (FolderController().checkFolderExistsInDb(folderId).message == "Exists") {
//                 UPDATE Note SET Title="Ok Note" WHERE NoteID = 1;
//                statement.execute("INSERT INTO Note(Title, Date, InFolder) VALUES('${note.title}', '${note.date}', $folderId);")
//                note.content.forEach { item ->
//                    statement.execute("INSERT INTO Item(Content, IsDone, InNote) VALUES('${item.content}', ${if (item.isDone) "TRUE" else "FALSE"}, ${note.id});")
//                }
//                 INSERT INTO Item(Content, IsDone, InNote) VALUES('Test Content', FALSE, 1);
//                println("Note added successfully")
//                Response("Note added successfully")
//            }
//            else {
//                println("Folder does not exist for note!")
//                Response("Folder does not exist for note!")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            println("Unable to add note")
//            Response("Unable to add Note!")
//        }
//    }
//
//    private fun fetchFullNote(noteId: Int): String {
//        try {
//            val content = mutableListOf<Item>()
//            val resultSetContent = DbConnection.connection.createStatement().executeQuery("SELECT * FROM Item WHERE InNote = $noteId;")
//            while (resultSetContent.next()) {
//                content.add(
//                    Item(
//                        resultSetContent.getInt(1),
//                        resultSetContent.getString(2),
//                        resultSetContent.getBoolean(3),
//                    )
//                )
//            }
//
//             SELECT * FROM Note WHERE NoteID = 1;
//            var note: Note? = null
//            val resultSetNote = DbConnection.connection.createStatement().executeQuery("SELECT * FROM Item WHERE InNote = $noteId;")
//            while (resultSetNote.next()) {
//                note = Note(
//                    resultSetNote.getInt(1),
//                    resultSetNote.getString(2),
//                    resultSetNote.getString(3),
//                    content
//                )
//            }
//            return gson.toJson(note)
//        }
//        catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return gson.toJson(null)
//    }
//}