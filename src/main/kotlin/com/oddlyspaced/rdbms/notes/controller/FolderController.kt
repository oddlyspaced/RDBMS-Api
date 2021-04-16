package com.oddlyspaced.rdbms.notes.controller

import com.oddlyspaced.rdbms.notes.db.DbConnection
import com.oddlyspaced.rdbms.notes.entity.Folder
import com.oddlyspaced.rdbms.notes.entity.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Rest Controller for all Folder related operations
 */
@RestController
class FolderController {

    /**********************************
     * Mappings of endpoints
     **********************************/

    /**
     * Mapping to fetch all existing folders in database
     */
    @GetMapping("/folders/all")
    fun getFolders() = readFoldersFromDb()

    /**
     * Mapping to add a folder to database
     */
    @GetMapping("/folders/add")
    fun addFolder(@RequestParam(value = "name") name: String) = addFolderToDb(name)

    /**
     * Mapping to check if folder exists in database
     */
    @GetMapping("/folders/exists")
    fun checkFolderExists(@RequestParam(value = "name") name: String) = checkFolderExistsInDb(name)

    /**********************************
     * Operation functions
     **********************************/

    /**
     * Function to read all folders from database
     * @return List<Folder>: List of Folders from database
     */
    private fun readFoldersFromDb(): List<Folder> {
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

    /**
     * Function to add folder to database
     * @param folderName: Folder name to add in database
     * @return Response: Response message according to exit status of execution
     */
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

    /**
     * Function to check if a folder exists in database
     * @param folderName: Folder name to check for
     * @return Response message according to folders existence
     */
    private fun checkFolderExistsInDb(folderName: String): Response {
        return try {
            val statement = DbConnection.connection.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM Folder WHERE Title = '$folderName';")
            while (resultSet.next()) {
                return Response("Exists")
            }
            Response("Does not exists")
        }
        catch (e: Exception) {
            e.printStackTrace()
            Response("Cannot determine!")
        }
    }

}