package com.example.lineup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.greenrobot.eventbus.EventBus
import com.skyfishjy.library.RippleBackground



class route : Fragment() {

   // private var service: Intent?=null
    private var rippleBackground: RippleBackground? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_route, container, false)
        rippleBackground = view.findViewById(R.id.ripple_bg)
        rippleBackground?.startRippleAnimation()
        return view
    }
}