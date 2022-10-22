package hu.bme.aut.onlab.tripplanner.ui.details.pages.information

import android.content.Context
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(private val informationPresenter: InformationPresenter) : RainbowCakeViewModel<InformationViewState>(
    Loading
) {
    fun setTrip(trip: TripListItem) {
        viewState = DetailsContent(false, trip)
    }

    fun setWeather(city: String, context: Context) = execute {
        viewState = WeatherContent(false, informationPresenter.getWeather(city, context))
    }
}