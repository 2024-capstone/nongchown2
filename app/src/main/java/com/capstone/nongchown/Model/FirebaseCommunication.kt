package com.capstone.nongchown.Model

import android.media.MicrophoneInfo.Coordinate3F
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope

class FirebaseCommunication {

    private val db = FirebaseFirestore.getInstance()
    private val collectionUser = db.collection("user_data")
    private val collectionAccident = db.collection("accident_data")

    // 특정 documentId로 문서 검색 및 UserInfo로 변환
    fun fetchUserByDocumentId(email: String, callback: (UserInfo?) -> Unit) {
        Log.d("[로그]", "fetchUserByDocumentId email: $email")
        val user = collectionUser.document(email)
        user.get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    Log.d("[로그]", "addOnSuccessListener")
                    val userInfo = doc.toObject(UserInfo::class.java)
                    Log.d("[로그]", "userInfo: $userInfo")
                    callback(userInfo)
                } else {
                    Log.d("[로그]", "No such document")
                    callback(null)
                }
            }.addOnFailureListener { e ->
                e.printStackTrace()
                Log.d("[에러]", "Firebase connection error")
                callback(null)
            }
    }

    fun updateOrCreateUser(userInfo: UserInfo) {
        val documentReference = collectionUser.document(userInfo.email)
        try {
            documentReference.set(userInfo)
                .addOnSuccessListener {
                    Log.d("[로그]", "Firebase document of user successfully updated.")
                }.addOnFailureListener { e ->
                    Log.e("[에러]", "Firebase document of user failed to update.", e)
                }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun recordAccidentLocation(latitude: Double, longitude: Double) {
        val documentReference = collectionUser.document("first_accident")
        val location = mapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )

        try {
            documentReference.set(location)
                .addOnSuccessListener {
                    Log.d("[로그]", "Firebase document of accident successfully updated.")
                }.addOnFailureListener { e ->
                    Log.e("[에러]", "Firebase document of accident failed to update.", e)
                }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
