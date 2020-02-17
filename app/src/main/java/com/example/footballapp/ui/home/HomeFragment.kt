package com.example.footballapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.R
import com.example.footballapp.finalPath
import com.example.footballapp.ui.gallery.FootBallTeam
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import org.w3c.dom.Text
import java.io.File

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val playerList = root.findViewById(R.id.favTeamPlayerList) as RecyclerView
        playerList.layoutManager = LinearLayoutManager(this.context)
        getFavouriteFootBallTeam(root)

        return root
    }

    fun getFavouriteFootBallTeam(root: View) {
        val storageFile = File(finalPath)
        if (storageFile.exists()) {
            val data = storageFile.readText()
            println(data)
            val gson = Gson()
            val team = gson.fromJson(data, FootBallTeam::class.java)

            root.findViewById<RecyclerView>(R.id.favTeamPlayerList).adapter = HomeAdapter(team)
            root.findViewById<TextView>(R.id.favTeamName).text = "${team.country.country} (${team.country.code})"
            root.findViewById<TextView>(R.id.goalsLabel).text = "Goals: ${team.goals}"
            root.findViewById<TextView>(R.id.yellowCardsLabel).text = "Yellow cards: ${team.yellow_cards}"
            root.findViewById<TextView>(R.id.redCardsLabel).text = "Red cards: ${team.red_cards}"
        }
        else{
            findNavController().navigate(R.id.nav_gallery)
        }
    }
}