/*
Written by Abhishek Jajal for CS6326.001, assignment 5, starting October  24, 2019.
NetID: apj180001
This activity is executed when the app launches, it displays the list of top 10 highscores.
Also there is an add button which launches AddScoreActivity.
It primarily manages the listView and makes fileIO functin calls to read.write from/to the file

 */

package com.example.apj180001_asg4

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.R.attr.data
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    val REQUEST_CODE=1
    var listOfScores = ArrayList<Score>()
     lateinit var listView : ListView

    @RequiresApi(Build.VERSION_CODES.N)
    //This function initializes the listView with the list, that it gets from the Tab Seperated file, and also manages the fab button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listView = findViewById<ListView>(R.id.highScoreList)
        listOfScores= FileIO.readScoresFromFile(this)
        listView.adapter= customListAdapter(this,listOfScores)

        fab.setOnClickListener { view ->
            //Launches AddScoreActivity for result
           val addScoreActivity = Intent(this, AddScoreActivity::class.java)
            startActivityForResult(addScoreActivity,REQUEST_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    //This function deals the data returned from the AddScoreActivity, updates the listView and makes calls to FileIO functions to update the file.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Checks if the request code matches
        if (requestCode === REQUEST_CODE)
        {
            //Checks if the activity did return something
            if (resultCode === Activity.RESULT_OK) {
                val playerName: String = data?.getStringExtra("playerName").toString()
                val scoreString: String = data?.getStringExtra("score").toString().trim() // Making sure name doesn't contain extra whitespaces
                // If nothing is entered in the Score field, then it is stored as 0.
                var score: Long = 0
                if(!scoreString.isEmpty())
                {
                    score= scoreString.toLong()
                }
                val dateTime : String = data?.getStringExtra("dateTime").toString()
                // Creating a Score using the results returned and adding it to the listOfScores.
                listOfScores.add(Score(playerName,score,dateTime))
                listView.adapter= customListAdapter(this,listOfScores)
                //Writes the updated list in to the file
                FileIO.writeScoresToFile(this,listOfScores)
            }
            if (resultCode === Activity.RESULT_CANCELED) {
                // if Nothing was retured, do nothing.
            }
        }
    }
}
