package com.example.lineup

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.ResultPoint
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.ViewfinderView

class Qr_code : Fragment() {

    private var database = FirebaseDatabase.getInstance()
    val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val userInfo=database.getReference("users/$userid")
    private lateinit var zeal_id:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_qr_code, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val qr=view.findViewById<ImageView>(R.id.qr_code)
        userInfo.get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val snapshot=task.result
                zeal_id=snapshot.child("zealid").value.toString()
                Log.e("id123", zeal_id)

            }else{
                Log.e("TAG","Error getting data")
            }

        }
        //   Log.e("id123","$zeal_id")
        // btn.setOnClickListener {
        val multiFormatWriter = MultiFormatWriter()
        try {
            val multiFormatWriter = MultiFormatWriter()
            val bitMatrix: BitMatrix = multiFormatWriter.encode(userid, BarcodeFormat.QR_CODE, 300, 300)
            val barcodeEncoder = BarcodeEncoder()
            val bitMap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)

            qr.setImageBitmap(bitMap)

        } catch (e: Exception) {
            // Toast.makeText(this, "Unable to generate QR", Toast.LENGTH_SHORT).show()
        }
    }
}
