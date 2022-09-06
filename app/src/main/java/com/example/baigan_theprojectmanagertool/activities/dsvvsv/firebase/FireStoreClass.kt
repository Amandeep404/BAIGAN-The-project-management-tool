package com.example.baigan_theprojectmanagertool.activities.dsvvsv.firebase

import android.app.Activity
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities.MainActivity
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities.SignInActivity
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities.SignUpActivity
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.User
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()
    fun registerUser(activity : SignUpActivity, userInfo : User){

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener {
                activity.userRegisteredSuccess()
            }
    }

    fun signInUser(activity : Activity){

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener{document ->
                val loggedInUser = document.toObject(User::class.java)!!
                    when(activity){
                        is SignInActivity ->{
                            activity.signInSuccess(loggedInUser)
                        }
                        is MainActivity ->{
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                    }
            }
            .addOnFailureListener{
                when(activity){
                    is SignInActivity ->{
                        activity.hideProgressBar()
                    }
                    is MainActivity ->{
                        activity.hideProgressBar()
                    }
                }
            }
    }

    fun getCurrentUserId():String{
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser!=null){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

}