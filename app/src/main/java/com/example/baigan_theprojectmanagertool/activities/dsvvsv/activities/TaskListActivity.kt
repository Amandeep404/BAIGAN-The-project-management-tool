package com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.baigan_theprojectmanagertool.R

class TaskListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        supportActionBar?.hide()

    }
}