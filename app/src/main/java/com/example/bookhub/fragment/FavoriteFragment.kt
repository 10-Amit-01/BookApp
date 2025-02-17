package com.example.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.bookhub.R
import com.example.bookhub.adapter.FavouriteRecyclerAdapter
import com.example.bookhub.database.BookDatabase
import com.example.bookhub.database.BookEntity

class FavoriteFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout : RelativeLayout
    var data = listOf<BookEntity>(BookEntity(1,"amit","jhfjh","hdhd","jfu","kjbkjbjjb","kbbjbiubibukjb"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        recyclerView = view.findViewById(R.id.favRecycle)
        progressBar = view.findViewById(R.id.progressBarFav)
        progressLayout = view.findViewById(R.id.rlProgressBar)

        progressLayout.visibility = View.VISIBLE

        val layoutManager = GridLayoutManager(activity as Context,2)

        data = GetBookFromdb(activity as Context).execute().get()

        if(activity != null){
            progressLayout.visibility = View.GONE
            val adapter = FavouriteRecyclerAdapter(activity as Context, data )
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager
        }

        return view
    }

    class GetBookFromdb(val context : Context) : AsyncTask<Void,Void,List<BookEntity>>(){
        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java,"book-db").build()
            return db.BookDao().getAllBooks()
        }

    }
}
