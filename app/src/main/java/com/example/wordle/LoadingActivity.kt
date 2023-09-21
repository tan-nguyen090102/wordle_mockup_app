package com.example.wordle

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ThemedSpinnerAdapter
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.system.measureTimeMillis

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        window.decorView.setBackgroundColor(Color.WHITE)

        val wordList = ArrayList<String>()
        var time : Long = 0

        //Thread to load word list
        val readingThread = Thread()
        {
            val timer = measureTimeMillis {
                val inputStream : InputStream
                try {
                    val resource : Resources = resources
                    inputStream = resource.openRawResource(R.raw.words)
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    var line = reader.readLine()
                    while (line != null)
                    {
                        wordList.add(line)
                        line = reader.readLine()
                    }
                }
                catch (e : IOException)
                {
                    Log.d("readfile", "Error reading files")
                }
            }
            time = timer
            Thread.interrupted()
        }.start()

        //Send data to main activity
        val loadingThread = Thread()
        {
            val mainIntent = Intent(this@LoadingActivity, MainActivity::class.java)
            val extra = Bundle()
            extra.putStringArrayList("bundle", wordList)
            mainIntent.putExtra("wl", extra)
            if (time < 3000)
                Thread.sleep(3000 - time)
            startActivity(mainIntent)
            finish()
            Thread.interrupted()
        }.start()

    }
}