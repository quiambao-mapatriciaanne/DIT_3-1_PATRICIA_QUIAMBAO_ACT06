package com.example.apiconnect

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val Post.body: String
    get() {
        TODO()
    }
private val Post.title: String
    get() {
        TODO()
    }
private val Post.id: String
    get() {
        TODO()
    }

// ... (imports remain the same)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnFetch = findViewById<Button>(R.id.btnFetch)
        val tvResults = findViewById<TextView>(R.id.tvResults)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        btnFetch.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            btnFetch.isEnabled = false // Disable button while loading
            tvResults.text = "Connecting to server..."

            apiService.getPosts().enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    progressBar.visibility = View.GONE
                    btnFetch.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val posts = response.body()
                        val resultText = StringBuilder()

                        posts?.take(15)?.forEach { post -> // Showing first 15 for cleanliness
                            resultText.append("ID: ${post.id}\n")
                            resultText.append("TITLE: ${post.title.uppercase()}\n")
                            resultText.append("CONTENT: ${post.body}\n")
                            resultText.append("__________________________________\n\n")
                        }

                        tvResults.text = resultText.toString()
                    } else {
                        tvResults.text = "Server Error: Unable to process request."
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    btnFetch.isEnabled = true
                    tvResults.text = "Connection Failed. Please check your internet settings."

                    Toast.makeText(this@MainActivity, "Network Failure", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}