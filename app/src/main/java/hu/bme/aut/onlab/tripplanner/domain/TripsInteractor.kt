package hu.bme.aut.onlab.tripplanner.domain

import android.content.Context
import android.graphics.Bitmap
import hu.bme.aut.onlab.tripplanner.data.datasource.FirebaseDataSource
import hu.bme.aut.onlab.tripplanner.data.disk.database.TripListItemDao
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.ml.Model5
import kotlinx.coroutines.flow.Flow
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import javax.inject.Inject

class TripsInteractor @Inject constructor(private val database: TripListItemDao, private val firebaseDataSource: FirebaseDataSource) {

    suspend fun addListener(): Flow<List<TripListItem>> {
        return firebaseDataSource.getTrips()
    }

    suspend fun addFB(newItem: TripListItem) {
        firebaseDataSource.onUploadTrip(newItem)
    }

    suspend fun editFB(editedItem: TripListItem) {
        firebaseDataSource.onEditTrip(editedItem)
    }

    suspend fun removeFB(removedItem: TripListItem) {
        firebaseDataSource.onDeleteTrip(removedItem)
    }

    /*suspend fun load(): List<TripListItem> {
        /*val newsData = network.getNews()
        val newsList = addNews(newsData)
        if(newsData != null) {
            database.deleteAll()
            database.insertAll(newsList)
        }
        return newsList*/

        val data  = mutableListOf<TripListItem>()
        data.addAll(database.getAll())
        data.sortBy { it.date }
        return data
    }*/

    /*suspend fun add(newItem: TripListItem): List<TripListItem> {
        database.insert(newItem)
        firebaseDataSource.onUploadTrip(newItem)
        return load()
    }

    suspend fun edit(editedItem: TripListItem): List<TripListItem> {
        database.update(editedItem)
        firebaseDataSource.onEditTrip(editedItem)
        return load()
    }

    suspend fun remove(removedItem: TripListItem): List<TripListItem> {
        database.delete(removedItem)
        firebaseDataSource.onDeleteTrip(removedItem)
        return load()
    }*/

    fun identify(image: Bitmap, context: Context): Category {
        val model = Model5.newInstance(context)

        val tensorImage = TensorImage.fromBitmap(image)

        val outputs = model.process(tensorImage)
        val probability = outputs.probabilityAsCategoryList

        model.close()
        val maxScore = probability.maxByOrNull { p -> p.score }!!

        return maxScore
    }
}