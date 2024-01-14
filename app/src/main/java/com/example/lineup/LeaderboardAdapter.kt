package com.example.lineup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.core.Context

class LeaderboardAdapter(val context:Context , val list:ArrayList<LeaderboardModel>):RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>()
{

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val avatar=itemView.findViewById<ImageView>(R.id.player_avatar)
        val name=itemView.findViewById<TextView>(R.id.leaderboard_name)
        val memebersFound=itemView.findViewById<TextView>(R.id.leaderboard_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_item , parent , false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val leaderboardList=list[position]
        holder.avatar.setImageResource(leaderboardList.avatar)
        holder.name.text=leaderboardList.name
        holder.memebersFound.text=leaderboardList.membersFound.toString()
    }
}