package com.example.mobile.Model

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

class StorageModel(rootPath: String) {

    private val storageReference = FirebaseStorage.getInstance().reference.child(rootPath)

    fun uploadFile(localUri: Uri, onComplete: (String) -> Unit, onFailure: (String) -> Unit) {
        val filename = System.currentTimeMillis().toString()
        val fileReference = storageReference.child(filename)
        fileReference.putFile(localUri).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    onComplete(uri.toString())
                }
            } else {
                onFailure(task.exception?.message ?: "")
            }
        }
    }
}