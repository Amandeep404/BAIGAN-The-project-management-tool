package com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baigan_theprojectmanagertool.R
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.adapters.TaskListItemAdapter
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.firebase.FireStoreClass
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.Board
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.TaskModel
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.utils.Constants
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_task_list.*
import java.text.FieldPosition

class TaskListActivity :BaseActivity() {

    private lateinit var mBoardDetails :Board

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        supportActionBar?.hide()

        var boardDocumentId = ""
        if (intent.hasExtra(Constants.DOCUMENT_ID)){
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }

       showProgressDialog("Fetching your boards")
       FireStoreClass().getBoardDetails(this, boardDocumentId)

    }

    fun boardDetails(board: Board){

        mBoardDetails = board

        hideProgressBar()
        setUpProfileActionBar()

        val addTaskList = TaskModel("Add List")
        board.taskList.add(addTaskList)

        rvTaskList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvTaskList.setHasFixedSize(true)
        val adapter = TaskListItemAdapter(this, board.taskList)

        rvTaskList.adapter = adapter
    }
    private fun setUpProfileActionBar(){

        toolBarTaskList.title = mBoardDetails.name
        toolBarTaskList.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        toolBarTaskList.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    fun addUpdateTaskListSuccess(){
        hideProgressBar()

        showProgressDialog("Making Changes")
        FireStoreClass().getBoardDetails(this, mBoardDetails.documentId)
    }

    fun createTaskList(taskListName: String){
        val task = TaskModel(taskListName, FireStoreClass().getCurrentUserId())
        mBoardDetails.taskList.add(0, task)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size -1)

        showProgressDialog("Creating List")
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }
    fun updateTaskList(position:Int, listName:String, model: TaskModel){
        val task = TaskModel(listName, model.createdBy)

        mBoardDetails.taskList[position] = task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size -1)

        showProgressDialog("Updating List")
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }
    fun deleteTaskList(position: Int){
        mBoardDetails.taskList.removeAt(position)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)

        showProgressDialog("Deleting List")
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }

}