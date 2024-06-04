package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

// this is A Data access object of Astroid database

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM near_earth_objects_table")
    fun getAllAsteroids(): List<DatabaseAsteroid>


    @Query("SELECT * FROM near_earth_objects_table WHERE" +
            " closeapproachdate >= :periodStart" +
            " AND closeapproachdate <= :periodEnd")
    fun getAsteroidslimittime(periodStart: String, periodEnd: String): List<DatabaseAsteroid>



    @Query("SELECT * FROM near_earth_objects_table WHERE" +
            " ispotentiallyhazardous = :isPotentiallyHazardous")
    fun allHazardousAsteroids(isPotentiallyHazardous: Boolean): LiveData<List<DatabaseAsteroid>>




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg asteroids: DatabaseAsteroid)


    @Query("DELETE FROM near_earth_objects_table")
    fun clearAll()

}

