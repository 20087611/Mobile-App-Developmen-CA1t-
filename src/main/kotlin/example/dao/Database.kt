package com.example.dao

import java.sql.Connection
import java.sql.DriverManager

class Database {
    var conn: Connection

    init {
        // 使用 Class.forName() 加载 SQLite 驱动类
        Class.forName("org.sqlite.JDBC")
        // 创建数据库连接
        conn = DriverManager.getConnection("jdbc:sqlite:/Users/liudaijie/mydatabase.db")
    }
}
