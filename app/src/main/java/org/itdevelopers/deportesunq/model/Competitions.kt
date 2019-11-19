package org.itdevelopers.deportesunq.model

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.itdevelopers.deportesunq.interfaces.CompetitionsApiInterface
import org.itdevelopers.deportesunq.net.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Competitions : BaseObservable() {

    private lateinit var status: String

//    https://stackoverflow.com/a/50893555/5279996
    private var competitionsList: MutableList<Competition> = ArrayList()

    private val competitions: MutableLiveData<List<Competition>> = MutableLiveData()

    fun getStatus() {
        this.status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun addCompetition(competition: Competition) {
        competitionsList.add(competition)
    }

    fun getCompetitions(): MutableLiveData<List<Competition>> {
        return competitions
    }

    /**
     * Source: https://stackoverflow.com/a/43737962/5279996
     */
    fun fetchList() {
        val callback: Callback<Competitions> = object : Callback<Competitions> {
            override fun onResponse(call: Call<Competitions>, response: Response<Competitions>) {
                val body: Competitions? = response.body()
                if (body != null) {
                    status = body.status
                }
                competitions.value = body?.competitionsList
            }

            override fun onFailure(call: Call<Competitions>, t: Throwable) {
                Log.e("Test", t.message, t)
            }
        }

        API.client.create(CompetitionsApiInterface::class.java).getCompetitions().enqueue(callback)
    }

}

data class Competition(
    @SerializedName("id") @Expose private val id: Int,
    @SerializedName("name") @Expose private val name: Int,
    @SerializedName("year") @Expose private val year: String,
    @SerializedName("champion_team") @Expose private val championTeam: String,
    @SerializedName("competition_detail") @Expose private val competitionDetail: CompetitionDetail? = null)

data class CompetitionDetail(
    @SerializedName("id") @Expose private val id: Int,
    @SerializedName("competition_detail_items") @Expose private val competitionDetailItems: List<CompetitionDetailItem>) {
}

data class CompetitionDetailItem(
    @SerializedName("position") @Expose private val position: Int,
    @SerializedName("team") @Expose private val team: Team)

data class Team(
    @SerializedName("id") @Expose private val id: Int,
    @SerializedName("alias") @Expose private val alias: String,
    @SerializedName("fullName") @Expose private val fullName: String)

