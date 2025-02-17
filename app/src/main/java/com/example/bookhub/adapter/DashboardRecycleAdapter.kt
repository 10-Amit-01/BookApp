package com.example.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.activity.DescriptionActivity
import com.example.bookhub.database.BookEntity
import com.example.bookhub.modal.Book
import com.squareup.picasso.Picasso


class DashboardRecycleAdapter(private val context: Context, private val itemList : ArrayList<Book> ) : RecyclerView.Adapter<DashboardRecycleAdapter.DashBoardViewHolder>(){

    class DashBoardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val bookName : TextView = view.findViewById(R.id.txtBookName)
        val author : TextView = view.findViewById(R.id.txtBookAuthor)
        val price : TextView = view.findViewById(R.id.txtBookPrice)
        val rating : TextView = view.findViewById(R.id.txtBookRating)
        val img : ImageView = view.findViewById(R.id.imgBookImage)
        val relativeLayout : RelativeLayout = view.findViewById(R.id.rlRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_dashboard_single_row,parent,false)

        return DashBoardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashBoardViewHolder, position: Int) {
        holder.bookName.text = itemList[position].bookName
        holder.author.text = itemList[position].author
        holder.price.text = itemList[position].price
        holder.rating.text = itemList[position].rating
        Picasso.get().load(itemList[position].image).error(R.drawable.ic_launcher_background).into(holder.img)
        holder.rating.isCursorVisible = false

        holder.relativeLayout.setOnClickListener {
            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("Book_id",itemList[position].id)
            context.startActivity(intent)
        }
    }
}