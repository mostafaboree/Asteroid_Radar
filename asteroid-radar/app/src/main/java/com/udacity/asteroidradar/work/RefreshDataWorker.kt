package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repo.AsteroidsRepository
import retrofit2.HttpException

class RefreshDataWorker(
    appContext: Context, params: WorkerParameters
): CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)

        return try {
            repository.getAllData()
            Result.success()

        } catch (e: HttpException) {
            Result.retry()
        }
    }
}