// sign up
package com.example.lineup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.lineup.databinding.ActivitySignUpBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChild(zealidtxt)) {
                            Log.e("error2" , "abcder")
                            Toast.makeText(this@SignUpActivity, "Zeal id is already registered..", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("error3" , "abcder")
                            // Store user data without whitespaces in keys
                            databaseReference.child(zealidtxt).child("FullName").setValue(fullnametxt)
                            databaseReference.child(zealidtxt).child("Email").setValue(emailtxt)
                            databaseReference.child(zealidtxt).child("Password").setValue(passwordtxt)

                            Toast.makeText(this@SignUpActivity, "User Registered Successfully.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("error4" , "abcder")
                        Toast.makeText(this@SignUpActivity, "Failed to register user...", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}
