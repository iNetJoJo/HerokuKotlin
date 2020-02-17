package com.example.footballapp.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.R
import com.example.footballapp.finalPath
import com.example.footballapp.ui.gallery.CustomViewHolder
import com.example.footballapp.ui.gallery.FootBallTeam
import com.example.footballapp.ui.gallery.Player
import com.google.gson.Gson
import kotlinx.android.synthetic.main.player_row.view.*
import java.io.File

class HomeAdapter(val team: FootBallTeam): RecyclerView.Adapter<CustomViewHolder>() {
    override fun getItemCount(): Int {
        return team.players.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.player_row, parent, false)
        return CustomViewHolder(cellForRow)
    }



    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val player: Player = team.players.get(position)

        holder.view.playerName.text = player.name
        holder.view.shirtNumberLabel.text = "Player number: ${player.shirt_number}"
        holder.view.positionLabel.text = player.position

        if (player.captain) {
            holder.view.capitanLabel.visibility = View.VISIBLE
            holder.view.capitanLabel.setBackgroundColor(Color.RED)
            holder.view.capitanLabel.text = "Captain"
        }
    }

}
