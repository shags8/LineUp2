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
//            val playerDetails= LeaderboardClass(name, membersFound, avatar)


        leaderboardRV = view.findViewById(R.id.leaderboard_rv)
        leaderboardRV.layoutManager = LinearLayoutManager(requireContext())


        sharedPreferences =
            requireActivity().getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"
        Log.e("id23", retrievedValue)
        val header = "Bearer $retrievedValue"

        val call = RetrofitApi.apiInterface.getPlayers(header)
        call.enqueue(object : Callback<LeaderboardModel> {
            override fun onResponse(
                call: Call<LeaderboardModel>, response: Response<LeaderboardModel>
            ) {
                if (response.isSuccessful) {
                    val leaderboard = response.body()
                    Log.e("id23", "$leaderboard")
                    Log.e("id2", "$response")
                    //   Log.e("id2345", "${response.headers()}")
                    if (leaderboard != null) {
                        // Handle the leaderboard data
                        val player = response.body()!!.name
                        val avatar = response.body()!!.avatar
                        val score = response.body()!!.membersFound
                        leaderboardList.add(LeaderboardModel( player, score, avatar))
                        leaderboardAdapter = LeaderboardAdapter(requireContext(), leaderboardList)
                        leaderboardRV.adapter = leaderboardAdapter
                        leaderboardAdapter.notifyDataSetChanged()
                    } else {
                        Log.e("id23", "${response.code()}")
                    }
                } else {
                    Log.e("id23", "Error: ${response.code()}")
                }



            }


            override fun onFailure(call: Call<LeaderboardModel>, t: Throwable) {
                Toast.makeText(requireContext(), "Oops something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }


}
