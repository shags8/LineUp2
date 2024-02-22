package com.example.lineup

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lineup.models.Code
import com.example.lineup.models.scanner
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.ViewfinderView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class scanner : Fragment() {
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var viewfinderView: ViewfinderView
    private var lastText: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val scannedQRSet = HashSet<String>()
    private var scanningEnabled = true

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (!scanningEnabled || result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                return
            }

            barcodeView.setStatusText(result.text)

            val token = Code(result.text)
            Log.e("id1235", "$token")
            if (token != null) {
                scanQRCode(token)
            }

        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    fun scanQRCode(qrCode: Code) {
        if (scannedQRSet.contains(qrCode.toString())) {
            Log.e("id1235", "Already scanned")
        } else {
            scannedQRSet.add(qrCode.toString())
            Log.e("id1235", "Added")
            scanningEnabled = false
            sendQRtoBackend(qrCode)
        }
    }

    fun sendQRtoBackend(qrCode: Code) {
        sharedPreferences =
            requireActivity().getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"
        Log.e("id1236", "$retrievedValue")
        val header = "Bearer $retrievedValue"
        val call = RetrofitApi.apiInterface.scan(header, qrCode)
        call.enqueue(object : Callback<scanner> {
            override fun onResponse(call: Call<scanner>, response: Response<scanner>) {
                val responseBody = response.body()
                Log.e("id12356", "$responseBody")
                Log.e("id12356", "$response")
                scanningEnabled = true
            }

            override fun onFailure(call: Call<scanner>, t: Throwable) {

                scanningEnabled = true
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_scanner, container, false)

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