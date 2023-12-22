// login up
package com.example.lineup


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.lineup.databinding.ActivityBottomBinding
import com.example.lineup.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


public class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private var databaseReference = FirebaseDatabase.getInstance().getReference("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root);


        val zeal  = binding.zeal
        val password = binding.password
        val loginbtn = binding.loginbtn


        loginbtn.setOnClickListener {
            val zealnumber = zeal.text.toString()
            val password_number = password.text.toString()

            if(zealnumber.isEmpty() || password_number.isEmpty())
            {
                Toast.makeText(this, "please enter your zealId and password", Toast.LENGTH_SHORT).show()
            }
            else{

                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // check if zealid is exist in firebase databse
                        if(dataSnapshot.hasChild(zealnumber))
                        {
                            // zeal id exist in firebase
                            // now get the user from firebase database and match it with user entered password
                            val getpassword = dataSnapshot.child(zealnumber).child("Password").getValue(String::class.java)

                            if (getpassword != null) {
                                if(getpassword.equals(password_number)) {
                                    Toast.makeText(this@LoginActivity, "Successfully Logged in..", Toast.LENGTH_SHORT).show()

                                    startActivity(Intent(this@LoginActivity , bottom_activity::class.java))
                                    finish();

                                } else {

                                    Toast.makeText(this@LoginActivity, "Credentials are Wrong.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(this@LoginActivity, "Credentials are Wrong", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "Credentials are Wrong", Toast.LENGTH_SHORT).show()

                    }
                })
            }
        }

    }


}