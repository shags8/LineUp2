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

class scanner : Fragment() {
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var viewfinderView: ViewfinderView
    private var lastText: String? = null
    private lateinit var sharedPreferences: SharedPreferences

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                return
            }

            lastText = result.text
            barcodeView.setStatusText(result.text)

            sharedPreferences = requireActivity().getSharedPreferences("ScannerToken", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            Log.e("id23","$lastText")
            editor.putString("ScannerToken", lastText)
            val token=lastText
            Log.e("id23","$token")

        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(com.example.lineup.R.layout.fragment_scanner, container, false)

        barcodeView = view.findViewById(R.id.barcode_scanner)


        val formats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.initializeFromIntent(requireActivity().intent)
        barcodeView.decodeContinuous(callback)

        return view
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
    fun pause() {
        barcodeView.pause()
    }

    fun resume() {
        barcodeView.resume()
    }

    fun triggerScan() {
        barcodeView.decodeSingle(callback)
    }
}