package com.example.lineup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory



class Qr_code : Fragment() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var beepManager: BeepManager
    private var lastText: String? = null

    private lateinit var scannedTextView: TextView

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) {
                // Prevent duplicate scans
                return
            }

            lastText = result.text
            barcodeView.setStatusText(result.text)

            beepManager.playBeepSoundAndVibrate()



        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_qr_code, container, false)

        barcodeView = view.findViewById(R.id.barcode_scanner)
        val formats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.initializeFromIntent(requireActivity().intent)
        barcodeView.decodeContinuous(callback)

        beepManager = BeepManager(requireActivity())

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