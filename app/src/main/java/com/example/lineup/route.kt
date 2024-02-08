package com.example.lineup

import SensorManagerHelper
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lineup.models.Route
import com.example.lineup.models.location
import com.example.lineup.models.qrCode
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.greenrobot.eventbus.EventBus
import com.skyfishjy.library.RippleBackground
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class route : Fragment() {




    // private var service: Intent?=null
    private var rippleBackground: RippleBackground? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var radarView: RadarView
//    private val sensorManagerHelper = SensorManagerHelper(requireContext())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_route, container, false)
        val textView = view.findViewById<TextView>(R.id.radar_heading)

//        sensorManagerHelper.startSensorUpdates { direction ->
//            textView.text = direction
//        }

//        rippleBackground = view.findViewById(R.id.ripple_bg)
//        rippleBackground?.startRippleAnimation()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        radarView = view.findViewById(R.id.radarview)
        sharedPreferences =
            requireActivity().getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"
        Log.e("id1236", "$retrievedValue")
        val header = "Bearer $retrievedValue"


        val call = RetrofitApi.apiInterface.getRoute(header)
        call.enqueue(object : Callback<Route> {
            override fun onResponse(call: Call<Route>, response: Response<Route>) {
                val responseBody = response.body()
                Log.e("id1235", header)
                Log.e("id1235", "$responseBody")
                Log.e("id1235", "$response")
                responseBody?.let {
                    val users = responseBody.nearestUsers.map { user ->
                        location(user.name, user.distance, user.direction)
                    }
                    // Update RadarView with the new route data
                    radarView.setUsers(users)
                }

            }

            override fun onFailure(call: Call<Route>, t: Throwable) {

            }

        })

    }
}