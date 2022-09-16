package com.example.baigan_theprojectmanagertool.activities.dsvvsv.firebase

import android.app.Activity
import android.widget.Toast
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities.*
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.Board
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.User
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
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

    fun createBoard(activity: CreateBoardActivity, board: Board){
        mFirestore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(activity, "Board Created Successfully", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }
            .addOnFailureListener{
                exception ->
                activity.hideProgressBar()
                Toast.makeText(activity, "Error while creating Board", Toast.LENGTH_SHORT).show()
            }
    }

    fun getBoardsList(activity: MainActivity){
        mFirestore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
            .get()
            .addOnSuccessListener {
                document ->
                val boardList : ArrayList<Board> = ArrayList()
                for (i in document.documents){
                    val board = i.toObject(Board::class.java)
                    board!!.documentId= i.id
                    boardList.add(board)
                }
                activity.populateBoardsList(boardList)
            }.addOnFailureListener{
                activity.hideProgressBar()
                Toast.makeText( activity, "Board not added", Toast.LENGTH_SHORT).show()
            }
    }

    fun getBoardDetails(activity: TaskListActivity, documentId: String){
        mFirestore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener {
                    document ->

                val board = document.toObject(Board::class.java)!!
                board.documentId = document.id
                activity.boardDetails(board)

            }.addOnFailureListener{
                activity.hideProgressBar()
                Toast.makeText( activity, "Board not added", Toast.LENGTH_SHORT).show()
            }

    }

    fun addUpdateTaskList(activity: TaskListActivity, board: Board){
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFirestore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener {
                activity.addUpdateTaskListSuccess()
            }
            .addOnFailureListener{
                exception ->
                activity.hideProgressBar()
                Toast.makeText(activity, "Error while creating Board", Toast.LENGTH_SHORT).show()
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

    fun loadUserData(activity : Activity, readBoardsList : Boolean = false){

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
                            activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
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