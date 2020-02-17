package com.example.footballapp.ui.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.R
import com.example.footballapp.finalPath
import com.google.gson.Gson
import kotlinx.android.synthetic.main.team_row.view.*
import java.io.*
import java.lang.Exception

class MainAdapter(val herokuAPI: List<FootBallTeam>, val context: Context): RecyclerView.Adapter<CustomViewHolder>(){

    //Number of items
    override fun  getItemCount(): Int {
        return herokuAPI.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.team_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val team = herokuAPI.get(position)

        holder.view.team_row_title?.text = "${team.country.country}(${team.country.code})"

        holder.view.setOnClickListener{

            var gson = Gson()
            val jsonData = gson.toJson(team)
            saveJson(jsonData)

            readJson()
            holder.view.findNavController().navigate(R.id.nav_home)
        }
    }

    fun readJson(){
        val storageFile = File(finalPath)
        val data = storageFile.readText()
        println(data)

    }
    fun saveJson(jsonString: String) {
        val sd_main = File(context.filesDir.path+"/heroku")
        var success = true

        if(!sd_main.exists()){
            success = sd_main.mkdir()
        }

        if (success){
            val sd = File(sd_main.path,"heroku.txt")

            if (!sd.exists()){
                success = sd.createNewFile()
            }
            if (success){
                try {
                    PrintWriter(sd).use { out -> out.println(jsonString) }
                    println("sucess")
                    finalPath = sd.path
                } catch (e: Exception){
                    println(e.message)
                }
            }else{
                println("error writing to file")
            }
        }




    }
}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view)