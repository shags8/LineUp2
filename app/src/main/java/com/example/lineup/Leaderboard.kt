package com.example.lineup

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lineup.models.LeaderboardModel
import com.example.lineup.models.LeaderboardModel2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Leaderboard : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var leaderboardRV: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var leaderboardList: ArrayList<LeaderboardModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//            val name:String
//            val membersFound:Int
//            val avatar:Int
//            val playerDetails= LeaderboardModel(name, membersFound, avatar)


        leaderboardRV = view.findViewById(R.id.leaderboard_rv)
        leaderboardRV.layoutManager = LinearLayoutManager(requireContext())


        sharedPreferences =
            requireActivity().getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"
       // Log.e("id123", retrievedValue)
        val header = "Bearer $retrievedValue"

        val call = RetrofitApi.apiInterface.getPlayers(header)
        call.enqueue(object : Callback<LeaderboardModel2> {
            override fun onResponse(
                call: Call<LeaderboardModel2>, response: Response<LeaderboardModel2>
            ) {
                if (response.isSuccessful) {
                    val leaderboard = response.body()
                    Log.e("id123", "$leaderboard")
                    //Log.e("id2", "$response")
                    //   Log.e("id2345", "${response.headers()}")
                    if (leaderboard != null) {
                        // Handle the leaderboard data
//                        val player = response.body()!!.name
//                        val avatar = response.body()!!.avatar
//                        val score = response.body()!!.membersFound
//                        leaderboardList.add(LeaderboardModel( player, score, avatar))
                        leaderboardAdapter = LeaderboardAdapter(requireContext(), leaderboardList)
                        leaderboardRV.adapter = leaderboardAdapter
                        leaderboardAdapter.notifyDataSetChanged()
                    } else {
                        Log.e("id23", "${response.code()}")
                    }
                } else {
                    Log.e("id123", "Error: ${response.code()}")
                }



            }


            override fun onFailure(call: Call<LeaderboardModel2>, t: Throwable) {
                Toast.makeText(requireContext(), "Oops something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }


}
