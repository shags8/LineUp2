import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lineup.RadarView
import com.example.lineup.RetrofitApi
import com.example.lineup.models.Route
import com.example.lineup.models.location
import com.gdsc.lineup.R
import com.skyfishjy.library.RippleBackground
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteFragment : Fragment() {

    private var rippleBackground: RippleBackground? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var radarView: RadarView
    private lateinit var directionTextView: TextView
    private val handler = Handler()
    private val apiRunnable = object : Runnable {
        override fun run() {
            callApi()
            handler.postDelayed(this, 5000) // Schedule the next call after 5 seconds
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_route, container, false)
        rippleBackground = view.findViewById(R.id.ripple_bg)
        rippleBackground?.startRippleAnimation()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        directionTextView = view.findViewById(R.id.radar_heading)
        radarView = view.findViewById(R.id.radarview)
        sharedPreferences = requireActivity().getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        // Call the API when the view is created
        callApi()
    }

    override fun onResume() {
        super.onResume()
        // Start the periodic API call
        handler.post(apiRunnable)
    }

    override fun onPause() {
        super.onPause()
        // Stop the periodic API call
        handler.removeCallbacks(apiRunnable)
    }

    private fun callApi() {
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"
        val header = "Bearer $retrievedValue"
        val call = RetrofitApi.apiInterface.getRoute(header)
        Log.e("Id123434","ok1")
        call.enqueue(object : Callback<Route> {
            override fun onResponse(call: Call<Route>, response: Response<Route>) {
                val responseBody = response.body()
                responseBody?.let {
                    val users = responseBody.nearestUsers.map { user ->
                        location(user.name, user.avatar, user.distance, user.direction)
                    }
                    // Update RadarView with the new route data
                    Log.e("Id123434","ok2")
                    radarView.setUsers(users)
                }
            }

            override fun onFailure(call: Call<Route>, t: Throwable) {
                // Handle API call failure
                Log.e("RouteFragment", "API call failed: ${t.message}")
            }
        })
    }
}
