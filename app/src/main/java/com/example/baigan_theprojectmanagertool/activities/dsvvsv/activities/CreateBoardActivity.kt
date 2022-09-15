package com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import com.bumptech.glide.Glide
import com.example.baigan_theprojectmanagertool.R
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.firebase.FireStoreClass
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.Board
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.utils.Constants
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.utils.Constants.getFileExtension
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_board.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException

class CreateBoardActivity : BaseActivity() {

    private var mSelectedCreateBoardImageFileUri : Uri? = null
    private var mBoardProfileImageUrl :  String = ""
    private lateinit var mUserName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)

        if (intent.hasExtra(Constants.NAME)){
            mUserName = intent.getStringExtra(Constants.NAME)!!
        }

        supportActionBar?.hide()
        toolBarCreateBoard.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        toolBarCreateBoard.setNavigationOnClickListener{
            onBackPressed()
        }

        ivCreateBoard.setOnClickListener{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Constants.choosePhotoFromGallery(this)
                Handler().postDelayed({
                    tvClickToChangeProfile.visibility = View.GONE
                },500)
            }else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }

        }

        btnCreateBoard.setOnClickListener{
            if (etBoardName.text.toString().isNotEmpty()){
                if (mSelectedCreateBoardImageFileUri!=null && etBoardName.text.toString().isNotEmpty()){
                    uploadBoardImage()
                }else{

                    showProgressDialog("Please Wait")
                    createBoard()

                }
            }else{
                showErrorSnackBar("Enter Board Name")

            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage && data!!.data !=null){
            mSelectedCreateBoardImageFileUri = data.data

            try {
                Glide
                    .with(this@CreateBoardActivity)
                    .load(mSelectedCreateBoardImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_person_grey_24)
                    .into(ivCreateBoard)
            }catch (e:IOException){
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image from gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun boardCreatedSuccessfully(){
        hideProgressBar()

        setResult(Activity.RESULT_OK)

        finish()
    }

    fun createBoard(){
        val assignedUserArrayList :ArrayList<String> = ArrayList()
        assignedUserArrayList.add(getCurrentUserId())

        var board = Board(
            etBoardName.text.toString(),
            mBoardProfileImageUrl,
            mUserName,
            assignedUserArrayList
        )

        FireStoreClass().createBoard(this, board)

    }

    private fun uploadBoardImage(){
        showProgressDialog("Please Wait")
        if (mSelectedCreateBoardImageFileUri!=null){
            val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
                "BOARD_IMAGE"+ System.currentTimeMillis() + "."+ getFileExtension(this, mSelectedCreateBoardImageFileUri)
            )
            sRef.putFile(mSelectedCreateBoardImageFileUri!!).addOnSuccessListener {
                    taskSnapShot ->
                taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri ->
                    mBoardProfileImageUrl = uri.toString()

                    createBoard()

                }
            }.addOnFailureListener{
                    exception ->
                Toast.makeText(this, exception.message,Toast.LENGTH_SHORT).show()
                hideProgressBar()
            }
        }
    }
    companion object{
        const val pickImage = 1
        const val READ_STORAGE_PERMISSION_CODE = 2
    }

}