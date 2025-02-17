package com.example.bookhub.database

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey


@androidx.room.Entity(tableName = "Books")
data class BookEntity(
    @PrimaryKey val bookId : Int,
    @ColumnInfo(name = "book_name") val bookName : String,
    @ColumnInfo(name = "book_author") val author : String,
    @ColumnInfo(name = "book_price") val price : String,
    @ColumnInfo(name = "book_rating") val rating : String,
    @ColumnInfo(name = "book_des") val description : String,
    @ColumnInfo(name = "book_img") val image : String
)
