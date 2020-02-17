package com.example.footballapp.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.R
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_gallery.*
import okhttp3.*
import java.io.IOException

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)


        val teamList: RecyclerView = root.findViewById(R.id.recyclerView_main)
        teamList.layoutManager = LinearLayoutManager(this.context)

        fetchJson()

        return root
    }

    fun fetchJson(){
        println("aTTEPTING TO FETCH JSON")

        val url = "https://world-cup-json-2018.herokuapp.com/matches"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                println(body)

                val gson = GsonBuilder().create()

                val homeFeed = gson.fromJson(body, Array<HerokuEntry>::class.java)



                activity?.runOnUiThread({
                    recyclerView_main.adapter = MainAdapter(getUniqueCountries(homeFeed), view?.context!!)
                })
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("failed to fetch")
            }
        })
    }
}

fun getUniqueCountries(countriesRaw: Array<HerokuEntry> ): List<FootBallTeam> {
    var footBallTeams: MutableList<FootBallTeam> = mutableListOf()

    countriesRaw.forEach { country ->
        run {
            // <----------------->
            //Nisam se snasao sa disticntBy i GroupBy metodama da maknem duplikate pa sam isao ovim
            //putem
            // <----------------->

            //check if away team exists in list
            // if it does add statistics to sum
            // join substitutes and starting eleven for away team
            // after that add them to footBallTeams list
            if (!footBallTeams.any{obj -> obj.country.code == country.away_team.code}) {
                val joinedPlayersTeamAway: ArrayList<Player> = arrayListOf()
                joinedPlayersTeamAway.addAll(country.away_team_statistics.starting_eleven)
                joinedPlayersTeamAway.addAll(country.away_team_statistics.substitutes)
                footBallTeams.add(
                    FootBallTeam(
                        country.away_team,
                        joinedPlayersTeamAway,
                        country.away_team_statistics.yellow_cards,
                        country.away_team_statistics.red_cards,
                        country.away_team.goals
                    )
                )
            }else {
                var team:FootBallTeam = footBallTeams.find { it.country.code == country.away_team.code }!!
                team.goals += country.away_team.goals
                team.red_cards += country.away_team_statistics.red_cards
                team.yellow_cards += country.away_team_statistics.yellow_cards
            }

            //check if home team exists in list
            // if it does add statistics to sum
            //join substitutes and starting eleven for home team
            // after that add them to footBallTeams list and
            if (!footBallTeams.any{obj -> obj.country.code == country.home_team.code}) {
                val joinedPlayersTeamHome: ArrayList<Player> = ArrayList()
                joinedPlayersTeamHome.addAll(country.home_team_statistics.starting_eleven)
                joinedPlayersTeamHome.addAll(country.home_team_statistics.substitutes)
                footBallTeams.add(
                    FootBallTeam(
                        country.home_team,
                        joinedPlayersTeamHome,
                        country.home_team_statistics.yellow_cards,
                        country.home_team_statistics.red_cards,
                        country.home_team.goals
                    )
                )
            }else{
                var team:FootBallTeam = footBallTeams.find { it.country.code == country.home_team.code }!!
                team.goals += country.home_team.goals
                team.red_cards += country.home_team_statistics.red_cards
                team.yellow_cards += country.home_team_statistics.yellow_cards
            }
        }
    }
    return footBallTeams
    }

class HerokuEntry(val fifa_id: String,val home_team: Country,val home_team_statistics: CountryPlayers, val away_team: Country,
                  val away_team_statistics: CountryPlayers)

class Country(val country: String, val code: String, val goals: Int)
class CountryPlayers(val yellow_cards: Int, val red_cards: Int, val country: String,val starting_eleven: ArrayList<Player>, val substitutes: ArrayList<Player>)
class Player(val name: String, val captain: Boolean, val shirt_number: Int, val position: String)


class FootBallTeam(val country: Country,val players:ArrayList<Player>, var yellow_cards: Int, var red_cards: Int, var goals: Int)


