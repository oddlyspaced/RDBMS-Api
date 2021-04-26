package com.oddlyspaced.rdbms.notes.controller

import com.google.gson.Gson
import com.oddlyspaced.rdbms.notes.db.DbConnection
import com.oddlyspaced.rdbms.notes.entity.Folder
import org.springframework.web.bind.annotation.GetMapping
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


}