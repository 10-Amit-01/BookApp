package com.example.bookhub.fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.example.bookhub.activity.DescriptionActivity
import com.example.bookhub.adapter.DashboardRecycleAdapter
import com.example.bookhub.modal.Book
import com.example.bookhub.util.ConnectionManger
import org.json.JSONException
import java.util.Collections

class DashboardFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: DashboardRecycleAdapter
    private lateinit var relativeLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var itemList = arrayListOf<Book>()
    var ratingComparator = Comparator<Book>{b1,b2 ->
        if(b1.rating.compareTo(b2.rating,true) == 0){
            b1.bookName.compareTo(b2.bookName,true)
        }
        else
        {
            b1.rating.compareTo(b2.rating,true)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerView = view.findViewById(R.id.dashboardRecycleView)
        relativeLayout = view.findViewById(R.id.rlProgressBar)
        progressBar = view.findViewById(R.id.progressBar)
        layoutManager = LinearLayoutManager(activity)

        progressBar.visibility = View.VISIBLE

        adapter = DashboardRecycleAdapter(activity as Context, itemList)

        if (ConnectionManger().checkConnectivity(activity as Context)) {
            val queue = Volley.newRequestQueue(activity as Context)

            val url = "http://13.235.250.119/v1/book/fetch_books/"

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null,
                    Response.Listener {
                        try {
                            progressBar.visibility = View.GONE

                            if (it.getBoolean("success")) {
                                val data = it.getJSONArray("data")

                                for (i in 0 until data.length()) {
                                    val book = Book(
                                        data.getJSONObject(i).getString("book_id"),
                                        data.getJSONObject(i).getString("name"),
                                        data.getJSONObject(i).getString("author"),
                                        data.getJSONObject(i).getString("price"),
                                        data.getJSONObject(i).getString("rating"),
                                        data.getJSONObject(i).getString("image")
                                    )

                                    itemList.add(book)
                                }
                                recyclerView.layoutManager = layoutManager
                                recyclerView.adapter = adapter
                            } else {
                                Toast.makeText(
                                    activity as Context,
                                    "Something Went Wrong",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(
                                activity as Context,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, Response.ErrorListener {
                        if (activity != null){
                            Toast.makeText(
                                activity as Context,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val header = HashMap<String, String>()
                        header["Context-type"] = "application/json"
                        header["token"] = "9bf534118365f1"
                        return header
                    }
                }

            queue.add(jsonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Not Found")
            dialog.setPositiveButton("open settings") { _, _ ->
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
                activity?.finish()
            }
            dialog.setNegativeButton("exit") { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sort){
            Collections.sort(itemList,ratingComparator)
            itemList.reverse()
        }
        adapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}