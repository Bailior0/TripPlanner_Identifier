package hu.bme.aut.onlab.tripplanner.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import hu.bme.aut.onlab.tripplanner.data.disk.model.TripListItem
import hu.bme.aut.onlab.tripplanner.data.network.ConnectivityChecker.isConnected
import hu.bme.aut.onlab.tripplanner.data.network.model.WeatherData

@Composable
fun Information(
    trip: TripListItem?,
    weatherList: WeatherData?
) {
    val weather = weatherList?.weather?.first()
    isConnected(LocalContext.current)

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(5.dp, 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text("Country", Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.country != null)
                Text(trip.country, Modifier.weight(1f).padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text("Place", Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.place != null)
                Text(trip.place, Modifier.weight(1f).padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text("Description", Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.description != null)
                Text(trip.description, Modifier.weight(1f).padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text("Date", Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.date != null)
                Text(trip.date, Modifier.weight(1f).padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text("Category", Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.category != null)
                Text(trip.category.name, Modifier.weight(1f).padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text("Visited", Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(trip?.visited != null) {
                when(trip.visited) {
                    true -> Text("Visited", Modifier.weight(1f).padding(start = 50.dp), textAlign = TextAlign.Justify)
                    false -> Text("Not yet visited", Modifier.weight(1f).padding(start = 50.dp), textAlign = TextAlign.Justify)
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text("Current weather", Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(weather?.main != null)
                Text(weather.main, Modifier.weight(1f).padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text("Current weather desc", Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(weather?.description != null)
                Text(weather.description, Modifier.weight(1f).padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
        ) {
            Text("Current temperature", Modifier.weight(1f), textAlign = TextAlign.Justify)
            if(weatherList?.main?.temp != null)
                Text(weatherList.main?.temp.toString() + " °C", Modifier.weight(1f).padding(start = 50.dp), textAlign = TextAlign.Justify)
        }
        Box(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            GlideImage(
                imageModel = { "https://openweathermap.org/img/w/${weather?.icon}.png" }
            )
        }
    }
}