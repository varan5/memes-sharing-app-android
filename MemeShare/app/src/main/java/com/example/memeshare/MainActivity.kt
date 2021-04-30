package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var currentImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    private fun loadMeme() {
        val memeProgressBar = findViewById(R.id.memeProgressBar) as ProgressBar
        memeProgressBar.visibility = View.VISIBLE
        val memeImageView = findViewById(R.id.memeImageView) as ImageView
        // Instantiate the RequestQueue
//        val queue = Volley.newRequestQueue(this)
        val currentImageUrl = "https://meme-api.herokuapp.com/gimme"

       val jsonObjectRequest = JsonObjectRequest(
           Request.Method.GET, currentImageUrl, null,
           { response ->
               val url = response.getString("url")
               Glide.with(this).load(url).listener(object : RequestListener<Drawable> {
                   override fun onLoadFailed(
                       e: GlideException?,
                       model: Any?,
                       target: Target<Drawable>?,
                       isFirstResource: Boolean
                   ): Boolean {
                        memeProgressBar.visibility = View.GONE
                       return false
                   }

                   override fun onResourceReady(
                       resource: Drawable?,
                       model: Any?,
                       target: Target<Drawable>?,
                       dataSource: DataSource?,
                       isFirstResource: Boolean
                   ): Boolean {
                        memeProgressBar.visibility = View.GONE
                       return false
                   }

               }).into(memeImageView)
           },
           {
               Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
           }
       )

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
//        queue.add(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme I got from Reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }
}