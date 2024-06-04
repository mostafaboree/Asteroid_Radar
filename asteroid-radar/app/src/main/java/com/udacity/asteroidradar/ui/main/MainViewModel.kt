package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.NeoApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repo.AsteroidsRepository
import kotlinx.coroutines.launch



class MainViewModel(
    application: Application
): AndroidViewModel(application) {

    /**
     * Factory for constructing this MainViewModel with application parameter
     */
    class Factory(val app: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
    private val db = getDatabase(application)
    private val repository = AsteroidsRepository(db)


    enum class ApiStatus { LOADING, ERROR, DONE }
    private val _status = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _status


    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _selectedAs = MutableLiveData<Asteroid>()
    val selectedAs: LiveData<Asteroid>
        get() = _selectedAs

    init {
        updateAsteroids()
        getPictureOfDay()
        getLiveData()
    }

    // triggers api fetch
    private fun updateAsteroids() {
        viewModelScope.launch {
            repository.getAllData()
        }
    }

    // gets asteroids from database as live data (default filter == SHOW_ALL)
    fun getLiveData(filter: AsteroidsRepository.DataFilter = AsteroidsRepository.DataFilter.get_ALL) {
        viewModelScope.launch {
            _asteroids.value = repository.getLiveData(filter)
        }
    }

    // get picture of day from api endpoint
    private fun getPictureOfDay() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING

            //on success
            try {
                _pictureOfDay.value = NeoApi.remote.getPictureOfDay().also {
                    _status.value = ApiStatus.DONE
                }

            //on error
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    // navigation

    // call to set selectedAsteroid to trigger navigation
    fun showDetailFragment(asteroid: Asteroid) {
        _selectedAs.value = asteroid
    }

    fun showDetailFragmentComplete() {
        _selectedAs.value = null // prevent subsequent navigation
    }
}