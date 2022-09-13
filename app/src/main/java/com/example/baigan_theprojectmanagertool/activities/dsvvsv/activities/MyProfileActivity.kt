package com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.baigan_theprojectmanagertool.R
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.firebase.FireStoreClass
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.User
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.IOException


class MyProfileActivity : BaseActivity() {

    private var mSelectedImageFileUri : Uri? = null
    private var mProfileImageUrl :  String = ""
    private lateinit var mUserDetails : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setUpProfileActionBar()

        FireStoreClass().loadUserData(this)

        ivProfileEditIcon.setOnClickListener{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                choosePhotoFromGallery()
            }else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_STORAGE_PERMISSION_CODE )
            }
        }

        btnProfile.setOnClickListener{
            if (mSelectedImageFileUri!=null){
                uploadUserImage()
            }else{
                showProgressDialog("Please Wait")
                updateUserProfileData()
            }
        }

    }

    private fun choosePhotoFromGallery(){
        val gallery  =  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == pickImage){
                if (data!!.data !=null){
                    mSelectedImageFileUri= data.data
                        try{

                            Glide
                                .with(this@MyProfileActivity)
                                .load(mSelectedImageFileUri)
                                .centerCrop()
                                .placeholder(R.drawable.ic_baseline_person_24)
                                .into(ivProfileImage)

                        }catch (e:IOException){
                            e.printStackTrace()
                            Toast.makeText(this, "Failed to load image from gallery", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                choosePhotoFromGallery()
            }
        }else{
            Toast.makeText(this,
                "The app needs storage permission to det profile picture. You can give permissions from settings",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpProfileActionBar(){
        supportActionBar?.hide()
        profileToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)
        profileToolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    fun setUserProfile(user: User){
        etProfileName.setText(user.name)
        etProfileEmail.setText(user.email)

        mUserDetails = user

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(ivProfileImage)

        if (user.mobile!=""){
            etProfileMobile.setText(user.mobile.toString())
        }
    }

    private fun uploadUserImage(){
        showProgressDialog("Please Wait")
        if (mSelectedImageFileUri != null){
            val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE"+ System.currentTimeMillis() + "."+ getFileExtension(mSelectedImageFileUri)
            )
            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                taskSnapShot ->
                taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    mProfileImageUrl = uri.toString()

                    updateUserProfileData()

                }
            }.addOnFailureListener{
                exception ->
                Toast.makeText(this, exception.message,Toast.LENGTH_SHORT).show()
                hideProgressBar()
            }
        }
    }

    private  fun getFileExtension(uri : Uri?): String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

 fun profileUpdateSuccess(){
        hideProgressBar()
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun updateUserProfileData(){
        val userHashMap = HashMap<String, Any>()
        var anyChangesMade = false

        if (mProfileImageUrl.isNotEmpty() && mProfileImageUrl!= mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageUrl
            anyChangesMade = true
        }
        if (etProfileName.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = etProfileName.text.toString()
            anyChangesMade = true
        }
        if (etProfileMobile.text.toString() != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = etProfileMobile.text.toString()
            anyChangesMade = true
        }

        if (anyChangesMade){
            FireStoreClass().updateUserProfileData(this, userHashMap)
        }

    }

    companion object{
        const val pickImage = 1
        const val READ_STORAGE_PERMISSION_CODE = 2

    }

}