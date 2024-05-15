package com.example.lineup

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lineup.models.qrCode
import com.gdsc.lineup.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Qr_code : Fragment() {

    private var database = FirebaseDatabase.getInstance()
    val userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private val userInfo = database.getReference("users/$userid")
    private lateinit var zeal_id: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val qr = view.findViewById<ImageView>(R.id.qr_code)
        val progressbar=view.findViewById<ProgressBar>(R.id.progressBar)

        sharedPreferences =
            requireActivity().getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"
        Log.e("id1236" , "$retrievedValue")
        val header = "Bearer $retrievedValue"

        progressbar.visibility=View.VISIBLE
        val call = RetrofitApi.apiInterface.getCode(header)
        call.enqueue(object : Callback<qrCode> {
            override fun onResponse(call: Call<qrCode>, response: Response<qrCode>) {
                val multiFormatWriter = MultiFormatWriter()
                val responseBody = response.body()
                Log.e("id1235", header)
                Log.e("id1235", "$responseBody")
                Log.e("id1235", "$response")
                try {
                    val multiFormatWriter = MultiFormatWriter()
                    val bitMatrix: BitMatrix = multiFormatWriter.encode(
                        responseBody!!.code, BarcodeFormat.QR_CODE, 300, 300
                    )
                    progressbar.visibility=View.GONE
                    val barcodeEncoder = BarcodeEncoder()
                    val bitMap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)

                    qr.setImageBitmap(bitMap)

                } catch (e: Exception) {
                    progressbar.visibility=View.GONE
                    Toast.makeText(requireContext(), "Unable to generate QR", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<qrCode>, t: Throwable) {
                progressbar.visibility=View.GONE
                Toast.makeText(requireContext(), "Unable to fetch QR",Toast.LENGTH_SHORT).show()
            }

        })

    }
}
