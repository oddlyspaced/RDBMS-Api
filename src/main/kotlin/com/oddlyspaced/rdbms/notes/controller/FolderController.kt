package com.oddlyspaced.rdbms.notes.controller

import com.oddlyspaced.rdbms.notes.db.DbConnection
import com.oddlyspaced.rdbms.notes.entity.Folder
import com.oddlyspaced.rdbms.notes.entity.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FolderController {

    @GetMapping("/folders/all")
    fun getFolders() = readFolders()

    @GetMapping("/folder/add")
    fun addFolder(@RequestParam(value = "name") name: String) = addFolderToDb(name)

    private fun readFolders(): List<Folder> {
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

    private fun addFolderToDb(folderName: String): Response {
        return try {
            println(folderName)
            val statement = DbConnection.connection.createStatement()
            val resultSet = statement.execute("INSERT INTO Folder (Title) VALUES('${folderName}');")
            println(resultSet)
            Response("Folder added successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Response("Unable to add folder!")
        }
    }

}