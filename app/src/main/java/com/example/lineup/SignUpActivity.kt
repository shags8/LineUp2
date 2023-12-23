// sign up
package com.example.lineup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.lineup.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.installations.FirebaseInstallations

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var databaseReference = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fullname = binding.name
        val Email = binding.email
        val zealid = binding.zeal
        val Password = binding.password
        val Registbutton = binding.regtbtn




   //     FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val deviceId = task.result
//                //val deviceRef = databaseReference.getReference("$deviceId")
//                // Use the device ID as needed, for example, store it in Firebase Authentication
//            } else {
//                // Handle errors
//            }
//        }

//        val auth = FirebaseAuth.getInstance()
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            currentUser.updateProfile(
//                UserProfileChangeRequest.Builder()
//             //   .setCustomMetadata(mapOf("deviceId" to deviceId))
//                .build())
//        }

        Registbutton.setOnClickListener {
            Log.e("error1" , "abcder")

            val fullnametxt = fullname.text.toString()
            val emailtxt = Email.text.toString()
            val zealidtxt = zealid.text.toString()
            val passwordtxt = Password.text.toString()

            if (fullnametxt.isEmpty() || emailtxt.isEmpty() || zealidtxt.isEmpty() || passwordtxt.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("error6" , "abcder")
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailtxt,passwordtxt).addOnCompleteListener{
                    if(it.isSuccessful){
                        val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            //  @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.hasChild(userid)) {
                                    Log.e("error2" , "abcder")
                                    Toast.makeText(this@SignUpActivity, "Zeal id is already registered..", Toast.LENGTH_SHORT).show()
                                } else {
                                    Log.e("error3" , "abcder")
                                    // Store user data without whitespaces in keys
                                    databaseReference.child(userid).child("FullName").setValue(fullnametxt)
                                    databaseReference.child(userid).child("Email").setValue(emailtxt)
                                    databaseReference.child(userid).child("Password").setValue(passwordtxt)
                                    databaseReference.child(userid).child("zealid").setValue(zealidtxt)
                                    Toast.makeText(this@SignUpActivity, "User Registered Successfully.", Toast.LENGTH_SHORT).show()
                                    val i= Intent(this@SignUpActivity,bottom_activity::class.java)
                                    startActivity(i)
                                    finish()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.e("error4" , "abcder")
                                Toast.makeText(this@SignUpActivity, "Failed to register user...", Toast.LENGTH_SHORT).show()
                            }
                        })
                                }else{
                        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
                    }
                }



            }
        }
    }
}
