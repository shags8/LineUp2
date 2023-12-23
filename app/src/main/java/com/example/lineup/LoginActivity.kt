// login up
package com.example.lineup
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.example.lineup.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


public class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private var database = FirebaseDatabase.getInstance()
    val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val userInfo=database.getReference("users/$userid")
    private var databaseReference = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var zeal_id:String
    private lateinit var auth_password:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root);

val loginbtn = binding.loginBtn
     //   Log.e("zealID","$zeal")
        userInfo.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val snapshot=task.result
                zeal_id=snapshot.child("zealid").value.toString()
                auth_password=snapshot.child("Password").value.toString()

             //   Log.e("id123", zeal_id)

            }else{
              //  Log.e("TAG","Error getting data")
            }

        }


        loginbtn.setOnClickListener {
            val zeal = binding.zeal.text.toString()
            val password = binding.password.text.toString()

            if(zeal.isEmpty() || password.isEmpty())
            {
                Log.e("zealID","$zeal")
                Toast.makeText(this, "please enter your zealId and password", Toast.LENGTH_SHORT).show()
            }
           else{

               if(zeal == zeal_id && password == auth_password){
                   Toast.makeText(this,"User verified",Toast.LENGTH_SHORT).show()
                   startActivity(Intent(this,bottom_activity::class.java))
                   finish()
               }else{
                   Toast.makeText(this,"User not found",Toast.LENGTH_SHORT).show()
               }
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Are you sure?")
            .setPositiveButton("yes", DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            })
            .setNegativeButton("no", DialogInterface.OnClickListener { dialog, which ->
                super.onBackPressed()
            })
            .show()
    }


}