package com.example.baigan_theprojectmanagertool.activities.dsvvsv.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities.MyProfileActivity

object Constants {

    const val USERS: String = "users"
    const val BOARDS : String = "boards"

    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val MOBILE: String = "mobile"
    const val ASSIGNED_TO: String = "assignedTo"

    const val pickImage = 1
    const val READ_STORAGE_PERMISSION_CODE = 2

    fun choosePhotoFromGallery(activity : Activity){
        val gallery  =  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(gallery,pickImage)

    }

    fun getFileExtension(activity: Activity ,uri : Uri?): String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}
