package com.example.baigan_theprojectmanagertool.activities.dsvvsv.firebase

import android.app.Activity
import android.widget.Toast
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities.MainActivity
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities.MyProfileActivity
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

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Toast.makeText( activity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener{
                activity.hideProgressBar()
                Toast.makeText( activity, "Unable to update profile", Toast.LENGTH_SHORT).show()
            }
    }

    fun loadUserData(activity : Activity){

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
                        is MyProfileActivity ->{
                            activity.setUserProfile(loggedInUser)
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