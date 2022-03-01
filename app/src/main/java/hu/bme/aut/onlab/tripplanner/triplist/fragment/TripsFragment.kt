package hu.bme.aut.onlab.tripplanner.triplist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.onlab.tripplanner.data.TriplistDatabase
import hu.bme.aut.onlab.tripplanner.data.TriplistItem
import hu.bme.aut.onlab.tripplanner.databinding.FragmentTripsBinding
import hu.bme.aut.onlab.tripplanner.triplist.adapter.TriplistAdapter
import kotlin.concurrent.thread

class TripsFragment : Fragment(), TriplistAdapter.TriplistItemClickListener {
    private lateinit var binding: FragmentTripsBinding

    private lateinit var database: TriplistDatabase
    private lateinit var adapter: TriplistAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentTripsBinding.inflate(layoutInflater, container, false)
        database = TriplistDatabase.getDatabase(requireActivity().getApplicationContext())

        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = TriplistAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.triplistItemDao().getAll()
            requireActivity().runOnUiThread {
                adapter.updateItem(items)
            }
        }
    }

    override fun onItemChanged(item: TriplistItem) {
        thread {
            database.triplistItemDao().update(item)
            Log.d("TripsFragment", "TriplistItem update was successful")
        }
    }

    override fun onItemRemoved(delItem: TriplistItem) {
        thread {
            database.triplistItemDao().deleteItem(delItem)

            requireActivity().runOnUiThread {
                adapter.removeItem(delItem)
            }
        }
    }

    override fun onItemEdited(editItem: TriplistItem) {
        NewTriplistItemDialogFragment(editItem).show(
            getChildFragmentManager(),
            NewTriplistItemDialogFragment.TAG
        )
    }

    override fun onTripSelected(country: String?, place: String?) {
        /*val showDetailsIntent = Intent()
        showDetailsIntent.setClass(this@TriplistActivity, DetailsActivity::class.java)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_TRIP_COUNTRY, country)
        showDetailsIntent.putExtra(DetailsActivity.EXTRA_TRIP_PLACE, place)
        startActivity(showDetailsIntent)*/
    }

    fun triplistItemCreated(newItem: TriplistItem) {
        requireActivity().runOnUiThread {
            adapter.addItem(newItem)
        }
    }

    fun triplistItemEdited(item: TriplistItem) {
        requireActivity().runOnUiThread {
            adapter.editItem(item)
        }
    }
}