package com.example.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Insert
    fun insert(Book : BookEntity)

    @Delete
    fun delete(Book : BookEntity)

    @Query("SELECT * FROM Books")
    fun getAllBooks() : List<BookEntity>

    @Query("SELECT * FROM BOOKS WHERE bookId = :bookId")
    fun getBookById(bookId : String) : BookEntity
}