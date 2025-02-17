package com.example.bookhub.activity

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.example.bookhub.database.BookDatabase
import com.example.bookhub.database.BookEntity
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {
    lateinit var bookname: TextView
    lateinit var author: TextView
    lateinit var price: TextView
    lateinit var rating: TextView
    lateinit var btnFav: Button
    lateinit var txtDes: TextView
    lateinit var progressBar: ProgressBar
    lateinit var img : ImageView
    lateinit var toolbar : Toolbar
    var id: String? = "100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        btnFav = findViewById(R.id.btnAddToFav)
        txtDes = findViewById(R.id.txtDescription)
        bookname = findViewById(R.id.txtBookName)
        author = findViewById(R.id.txtBookAuthor)
        price = findViewById(R.id.txtBookPrice)
        rating = findViewById(R.id.txtBookRating)
        img = findViewById(R.id.imgBookImage)
        progressBar = findViewById(R.id.ProgressBar)
        toolbar = findViewById(R.id.desToolbar)

        if (intent != null) {
            id = intent.getStringExtra("Book_id")
        } else {
            Toast.makeText(this, "Somthing went wrong", Toast.LENGTH_SHORT).show()
            finish()
        }

        if (id == "100") {
            Toast.makeText(this, "Somthing went wrong", Toast.LENGTH_SHORT).show()
            finish()
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Description"
        progressBar.visibility = View.VISIBLE

        val url = "http://13.235.250.119/v1/book/get_book/"

        val queue = Volley.newRequestQueue(this)


        val jsonParams = JSONObject()
        jsonParams.put("book_id", id)


        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonParams,
            Response.Listener {
                try {
                    progressBar.visibility = View.GONE
                    if (it.getBoolean("success")){
                        bookname.text = it.getJSONObject("book_data").getString("name")
                        author.text = it.getJSONObject("book_data").getString("author")
                        price.text = it.getJSONObject("book_data").getString("price")
                        rating.text = it.getJSONObject("book_data").getString("rating")
                        txtDes.text = it.getJSONObject("book_data").getString("description")
                        Picasso.get().load(it.getJSONObject("book_data").getString("image")).error(R.drawable.ic_launcher_background).into(img)

                        val book = BookEntity(
                        id?.toInt() as Int,
                        bookname.text.toString(),
                        author.text.toString(),
                        price.text.toString(),
                        rating.text.toString(),
                        txtDes.text.toString(),
                        it.getJSONObject("book_data").getString("image")
                        )

                        val colornotFav = ContextCompat.getColor(applicationContext,R.color.color_primary)
                        val colorFav = ContextCompat.getColor(applicationContext,R.color.noFav)
                        if (DBsync(applicationContext,book,1).execute().get()){
                            btnFav.text = "Remove from favourites"
                            btnFav.setBackgroundColor(colorFav)
                            rating.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_full,0,0,0)
                        }
                        else{
                            btnFav.text = "Add to favourites"
                            btnFav.setBackgroundColor(colornotFav)
                            rating.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_border,0,0,0)
                        }

                        btnFav.setOnClickListener {
                            if(!DBsync(applicationContext,book,1).execute().get()){
                                DBsync(applicationContext,book,2).execute().get()
                                btnFav.setBackgroundColor(colorFav)
                                Toast.makeText(this,"Added to favorites",Toast.LENGTH_SHORT).show()
                                btnFav.text = "Remove from favourites"
                                rating.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_full,0,0,0)
                            }
                            else{
                                DBsync(applicationContext,book,3).execute().get()
                                btnFav.setBackgroundColor(colornotFav)
                                btnFav.text = "Add to favourites"
                                Toast.makeText(this,"Remove from favorites",Toast.LENGTH_SHORT).show()
                                rating.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_border,0,0,0)
                            }
                        }
                    }
                    else{
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    Toast.makeText(this, "Sever Not Responded", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
//                if(get != null){
//                    Toast.makeText(
//                        this,
//                        "Sever Not Responded",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["context-type"] = "application/json"
                header["token"] = "9bf534118365f1"
                return header
            }
        }

        queue.add(jsonRequest)
    }

    class DBsync(val context : Context, val bookEntity: BookEntity, val mode :Int,) : AsyncTask<Void,Void,Boolean>(){



        override fun doInBackground(vararg p0: Void?): Boolean {

            val db = Room.databaseBuilder(context, BookDatabase::class.java,"book-db").build()

            when (mode){
                1 -> {
                    val check  : BookEntity = db.BookDao().getBookById(bookEntity.bookId.toString())
                    db.close()
                    return check != null
                }
                2 -> {
                    db.BookDao().insert(bookEntity)
                    db.close()
                    return true
                }
                3-> {
                    db.BookDao().delete(bookEntity)
                    db.close()
                    return true
                }
            }
            return false;
        }
    }
}