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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.lineup.models.LeaderboardModel
import com.example.lineup.models.LeaderboardModel2
import com.google.android.play.integrity.internal.f
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Leaderboard : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var leaderboardRV: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var leaderboardList: ArrayList<LeaderboardModel>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var characters: IntArray
    var fetchSuccess:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         characters= intArrayOf(
            R.drawable.red_avatar,
            R.drawable.pink_avatar,
            R.drawable.yellow_avatar,
            R.drawable.small_avatar,
            R.drawable.grey_avatar,
            R.drawable.blue_avatar,
            R.drawable.brown_avatar,
            R.drawable.green_avatar
        )

        leaderboardRV = view.findViewById(R.id.leaderboard_rv)
        leaderboardRV.layoutManager = LinearLayoutManager(requireContext())
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh)



        sharedPreferences =
            requireActivity().getSharedPreferences("LineUpTokens", Context.MODE_PRIVATE)

        dataFetch()

        refreshLeaderboard()
    }

    private fun refreshLeaderboard(){
        swipeRefreshLayout.setOnRefreshListener {
            val fetchSuccess = dataFetch()
           // Log.e("id123" , "$fetchSuccess")
            if (fetchSuccess) {
                Toast.makeText(requireContext(), "LeaderBoard Refreshed!!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Unable to refresh Leaderboard", Toast.LENGTH_SHORT).show()

            }
            swipeRefreshLayout.isRefreshing= false
        }
    }

    private fun dataFetch():Boolean {
        val retrievedValue = sharedPreferences.getString("Token", "defaultValue") ?: "defaultValue"
        val header = "Bearer $retrievedValue"
        val call = RetrofitApi.apiInterface.getPlayers(header)
        call.enqueue(object : Callback<LeaderboardModel2> {
            override fun onResponse(
                call: Call<LeaderboardModel2>, response: Response<LeaderboardModel2>
            ) {
                if (response.isSuccessful) {
                    val leaderboard = response.body()
                    Log.e("id123", "$leaderboard")
                    if (leaderboard != null) {
                        fetchSuccess = true
                        leaderboardList = ArrayList()
                        val array = leaderboard.users
                        for (i in array.indices) {
                            val user = array[i]
                            leaderboardList.add(
                                LeaderboardModel(
                                    user.name, user.membersFound, characters[user.avatar - 1]
                                )
                            )
                        }
                    //    Log.e("id123" , "$fetchSuccess")
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
        return fetchSuccess
    }


}
