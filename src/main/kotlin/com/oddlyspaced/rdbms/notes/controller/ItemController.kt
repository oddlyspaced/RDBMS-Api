package com.oddlyspaced.rdbms.notes.controller

import com.oddlyspaced.rdbms.notes.db.DbConnection
import com.oddlyspaced.rdbms.notes.entity.Item
import com.oddlyspaced.rdbms.notes.entity.Note
import com.oddlyspaced.rdbms.notes.entity.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ItemController {

    @GetMapping("/item/get")
    fun getNoteContent(@RequestParam(value = "noteId") noteId: Int) = fetchContentOfNote(noteId)

    @GetMapping("/item/add")
    fun addContent(
        @RequestParam(value = "content") content: String,
        @RequestParam(value = "noteId") noteId: Int
    ) = addContentToNote(content, noteId)

    private fun fetchContentOfNote(noteId: Int): MutableList<Item> {
        val items = mutableListOf<Item>()
        try {
            val statement = DbConnection.connection.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM Item WHERE InNote = $noteId;")
            while (resultSet.next()) {
                items.add(
                    Item(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getBoolean(4)
                    )
                )
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return items
    }

    private fun addContentToNote(content: String, noteId: Int): Response {
        return try {
            val statement = DbConnection.connection.createStatement()
            statement.execute("INSERT INTO Item(Content, IsDone, InNote) VALUES('$content', FALSE, $noteId);")
            Response("Content added successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Response("Unable to add Content!")
        }
    }
}