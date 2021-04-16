package com.oddlyspaced.rdbms.notes.db

import com.oddlyspaced.rdbms.notes.global.Global
import java.sql.Connection
import java.sql.DriverManager

object DbConnection {

    val connection: Connection

    init {
        Class.forName("com.mysql.cj.jdbc.Driver")
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/${Global.DATABASE}", Global.USER, Global.PASSWORD
        )
    }

}