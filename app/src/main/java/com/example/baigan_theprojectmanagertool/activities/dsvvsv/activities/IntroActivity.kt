package com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.baigan_theprojectmanagertool.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        introActivitySignUpBtn.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        introActivitySignInBtn.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}