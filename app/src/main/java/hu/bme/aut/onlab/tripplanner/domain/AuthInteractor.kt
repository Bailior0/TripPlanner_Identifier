package hu.bme.aut.onlab.tripplanner.domain

import android.content.Context
import android.text.Editable
import android.widget.Toast
import co.zsmb.rainbowcake.navigation.Navigator
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.onlab.tripplanner.ui.list.tripslist.TripListFragment
import javax.inject.Inject

class AuthInteractor @Inject constructor() {
    private var firebaseAuth = FirebaseAuth.getInstance()

    private fun validateForm(context: Context, mail: Editable, pass: Editable): Boolean {
        if (mail.isEmpty()) {
            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pass.isEmpty()) {
            Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun registerClick(context: Context, mail: Editable, pass: Editable) {
        if (!validateForm(context, mail, pass)) {
            return
        }

        firebaseAuth
            .createUserWithEmailAndPassword(mail.toString(), pass.toString())
            .addOnSuccessListener { result ->
                val firebaseUser = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(firebaseUser?.email?.substringBefore('@'))
                    .build()
                firebaseUser?.updateProfile(profileChangeRequest)
                firebaseUser?.sendEmailVerification()

                Toast.makeText(context, "Registration was successful\nVerification email has been sent", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun loginClick(navigator: Navigator?, context: Context, mail: Editable, pass: Editable) {
        if (!validateForm(context, mail, pass)) {
            return
        }

        firebaseAuth
            .signInWithEmailAndPassword(mail.toString(), pass.toString())
            .addOnSuccessListener {
                if(firebaseAuth.currentUser!!.isEmailVerified) {
                    navigator?.add(TripListFragment())
                }
                else
                    Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUserEmail(): String? {
        return firebaseAuth.currentUser?.email
    }

    fun sendPasswordReset() {
        val mail = getCurrentUserEmail() as String
        firebaseAuth.sendPasswordResetEmail(mail)
    }

    fun changeEmail(context: Context, password: String?, newEmail: String?) {
        if(password.isNullOrEmpty()){
            Toast.makeText(context, "Please enter a valid password", Toast.LENGTH_SHORT).show()
        }
        else if(newEmail.isNullOrEmpty() || !newEmail.contains('@') || newEmail == getCurrentUserEmail() as String){
            Toast.makeText(context, "Please enter a valid new email", Toast.LENGTH_SHORT).show()
        }
        else {
            val mail = getCurrentUserEmail() as String
            val credential = EmailAuthProvider.getCredential (
                mail,
                password
            )

            firebaseAuth.currentUser?.reauthenticate(credential)
                ?.addOnSuccessListener {
                    firebaseAuth.currentUser?.verifyBeforeUpdateEmail(newEmail)

                    Toast.makeText(context, "Verification email has been sent", Toast.LENGTH_SHORT).show()
                }
                ?.addOnFailureListener { exception ->
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
    }
}