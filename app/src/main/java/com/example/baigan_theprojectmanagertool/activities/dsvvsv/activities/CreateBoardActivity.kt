package com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.baigan_theprojectmanagertool.R
import kotlinx.android.synthetic.main.activity_create_board.*

class CreateBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)

        supportActionBar?.hide()
        toolBarCreateBoard.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        toolBarCreateBoard.setNavigationOnClickListener{
            onBackPressed()
        }
    }

}