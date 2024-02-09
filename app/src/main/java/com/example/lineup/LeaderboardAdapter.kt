package com.example.lineup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lineup.models.LeaderboardModel
import com.google.firebase.database.core.Context

class LeaderboardAdapter(
    val context: android.content.Context,
    private val list: List<LeaderboardModel>
) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.player_avatar)
        val name: TextView = itemView.findViewById(R.id.leaderboard_name)
        val memebersFound: TextView = itemView.findViewById(R.id.leaderboard_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val leaderboardList = list[position]
        holder.avatar.setImageResource(leaderboardList.avatar)
        holder.name.text = leaderboardList.name
        holder.memebersFound.text = leaderboardList.membersFound.toString()
    }
}