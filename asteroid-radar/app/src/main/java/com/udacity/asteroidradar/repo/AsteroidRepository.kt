package com.udacity.asteroidradar.repo

import android.annotation.SuppressLint
import android.util.Log
import com.udacity.asteroidradar.api.NeoApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.utilities.DateUtilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

// uses dependency injection to get reference to database from ViewModel instance to negate need
// to keep a reference to context and prevent leaks

class AsteroidsRepository(private val database: AsteroidsDatabase) {


    enum class DataFilter(val value: String) {
        get_ALL("all"),
        Get_PAST_WEEK("week"),
        SHOW_TODAY("today")
    }


    suspend fun getLiveData(filter: DataFilter): List<Asteroid> {

        var list: List<Asteroid>

        withContext(Dispatchers.IO) {

            // show all
            if (filter == DataFilter.get_ALL) {
                list = database.asteroidDao.getAllAsteroids().asDomainModel()

            } else {

                var startDate = ""
                var endDate = ""

                when (filter) {

                    // show last 7 days
                    DataFilter.Get_PAST_WEEK -> {
                        startDate = DateUtilities.getPastSevenDaysFormattedDates().last()
                        endDate = DateUtilities.getPastSevenDaysFormattedDates().first()
                        Log.i("RepostriyAstr", "post weak"+startDate + endDate)

                    }

                    // show today
                    DataFilter.SHOW_TODAY -> {
                        startDate = DateUtilities.getTodayFormattedDates().first()
                        endDate = DateUtilities.getTodayFormattedDates().last()
                    }
                    else -> {
                        list = database.asteroidDao.getAllAsteroids().asDomainModel()

                    }
                }


                list = database.asteroidDao.getAsteroidslimittime(startDate, endDate).asDomainModel()
            }
        }
        
        return list
    }


    @SuppressLint("LogNotTimber")
    suspend fun getAllData() {
        withContext(Dispatchers.IO) {

            try {
                val response = NeoApi.remote.getAsteroidsData()
                val jsonObject = JSONObject(response)
                val asteroids = parseAsteroidsJsonResult(jsonObject)

                val dbAsteroids = AsteroidDBList(asteroids).databaseModel()
                /* = java.util.ArrayList<com.udacity.asteroidradar.database.DatabaseAsteroid> */

                database.asteroidDao.insert(*dbAsteroids)

                Log.i("RepostriyAstr", "Asteroid Json objects successfully fetched from api")

            //on error
            }  catch (e: Exception) {
                Log.e("RepostriyAstr", "Failed to fetch : $e.")
            }
        }
        fun getWeek(){
            val  x=System.currentTimeMillis()
            val z=Calendar.getInstance()
            z.time
        }
    }
    data class AsteroidDBList(val asteroids: ArrayList<Asteroid>)

    fun AsteroidDBList.databaseModel(): Array<DatabaseAsteroid> {
        return asteroids.map {
            DatabaseAsteroid(
                id = it.id,
                codename = it.codename,
                isPotentiallyHazardous = it.isPotentiallyHazardous,
                        closeApproachDate = it.closeApproachDate,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
            )
        }.toTypedArray()
    }


}