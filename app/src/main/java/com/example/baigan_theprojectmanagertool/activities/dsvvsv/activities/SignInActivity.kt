package com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.baigan_theprojectmanagertool.R
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.firebase.FireStoreClass
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.nav_header_main.*

class SignInActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        signInToolBar.setNavigationOnClickListener{
            onBackPressed()
        }
        SignInActivitySignInBtn.setOnClickListener{
            logInUser()
        }
    }

    private fun logInUser(){
        val email: String = etSignInEmail.text.toString().trim(){it <= ' '}
        val password : String = etSignInPassword.text.toString().trim(){it <= ' '}

        if (validateLogInForm(email,password)){
            showProgressDialog("Signing You In  ")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    FireStoreClass().loadUserData(this)
                }
            }.addOnFailureListener{exception ->
                Toast.makeText(this, "Invalid Username or password", Toast.LENGTH_LONG).show()
                hideProgressBar()

            }
        }
    }

    private fun validateLogInForm(email:String, password:String):Boolean{
        return when{
                TextUtils.isEmpty(email) ->{
                    showErrorSnackBar("Please Enter Email")
                    false
                }
            TextUtils.isEmpty(password) ->{
                showErrorSnackBar("Please Enter Password")
                false
            }
            else -> true
        }
    }

    fun signInSuccess(user :User){
        hideProgressBar()
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

}