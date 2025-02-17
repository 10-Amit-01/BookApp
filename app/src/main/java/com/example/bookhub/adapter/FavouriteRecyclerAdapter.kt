package com.example.bookhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context : Context, private val data : List<BookEntity>) : RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>(){
    class FavouriteViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val linerLayout : LinearLayout = view.findViewById(R.id.llFavContent)
        val img : ImageView = view.findViewById(R.id.imgFavBookImage)
        val bookName :TextView = view.findViewById(R.id.txtFavBookName)
        val author : TextView = view.findViewById(R.id.txtFavBookAuthor)
        val price : TextView = view.findViewById(R.id.txtFavBookPrice)
        val rating :TextView = view.findViewById(R.id.txtFavBookRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_favourites_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bookName.text = data[position].bookName
        holder.author.text = data[position].author
        holder.price.text = data[position].price
        holder.rating.text = data[position].rating
        Picasso.get().load(data[position].image).error(R.drawable.ic_launcher_background).into(holder.img)

        holder.linerLayout.setOnClickListener {
            Toast.makeText(context,"Click",Toast.LENGTH_SHORT).show()
        }
    }
}