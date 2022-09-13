package com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities


import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.baigan_theprojectmanagertool.R
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.firebase.FireStoreClass
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        signUpToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
        SignUpActivitySignUpBtn.setOnClickListener{
            registerUser()
        }

    }

    private fun registerUser(){
        val name: String = etSignUpName.text.toString().trim(){it <= ' '}
        val email: String = etSignUpEmail.text.toString().trim(){it <= ' '}
        val password: String = etSignUpPassword.text.toString().trim(){it <= ' '}

        if( validateForm(name, email, password)){
            showProgressDialog("Building You Account")
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val fireBaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = fireBaseUser.email!!
                        val user = User(fireBaseUser.uid, name, registeredEmail)

                        FireStoreClass().registerUser(this, user)
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        hideProgressBar()
                    }
                }
        }
    }
    private fun validateForm(name:String, email: String, password : String): Boolean{
        return when{
            TextUtils.isEmpty(name) ->{
                showErrorSnackBar("Please Enter Name")
                false
            }
            TextUtils.isEmpty(email) ->{
                showErrorSnackBar("Please Enter E-mail")
                false
            }
            TextUtils.isEmpty(password) ->{
                showErrorSnackBar("Please Enter Password")
                false
            }
            else -> true
        }
    }
    fun userRegisteredSuccess(){
        Toast.makeText(
            this@SignUpActivity,
            "Congrats you have successfully registered with BAIGAN",
            Toast.LENGTH_SHORT
        ).show()

        hideProgressBar()
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}
