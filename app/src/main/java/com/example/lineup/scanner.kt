package com.example.lineup

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class scanner : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scanner, container, false)

    //    val btn=view.findViewById<Button>(R.id.gen_btn)

   //     }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val qr=view.findViewById<ImageView>(R.id.qr_code)
        val zeal_id=arguments?.getString("id").toString()
        Log.e("id123","$zeal_id")
        // btn.setOnClickListener {
        val multiFormatWriter = MultiFormatWriter()
        try {
            val multiFormatWriter = MultiFormatWriter()
            val bitMatrix: BitMatrix = multiFormatWriter.encode(zeal_id, BarcodeFormat.QR_CODE, 300, 300)
            val barcodeEncoder = BarcodeEncoder()
            val bitMap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)

            qr.setImageBitmap(bitMap)

        } catch (e: Exception) {
            // Toast.makeText(this, "Unable to generate QR", Toast.LENGTH_SHORT).show()
        }
    }
}