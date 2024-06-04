package com.udacity.asteroidradar.ui

import com.udacity.asteroidradar.models.Asteroid

interface AsteroidRcListener {
    fun onItemViewclick(asteroid: Asteroid)
}