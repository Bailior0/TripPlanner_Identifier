package hu.bme.aut.onlab.tripplanner.triplist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.onlab.tripplanner.R
import hu.bme.aut.onlab.tripplanner.data.TriplistItem
import hu.bme.aut.onlab.tripplanner.databinding.ItemTriplistListBinding

class TriplistAdapter(private val listener: TriplistItemClickListener) : RecyclerView.Adapter<TriplistAdapter.TriplistViewHolder>() {

    private val items = mutableListOf<TriplistItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriplistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_triplist_list, parent, false)
        return TriplistViewHolder(view)
    }

    override fun onBindViewHolder(holder: TriplistViewHolder, position: Int) {
        val triplistItem = items[position]

        holder.binding.ivIcon.setImageResource(getImageResource(triplistItem.category))
        holder.binding.cbVisited.isChecked = triplistItem.visited
        holder.binding.tvCountry.text = triplistItem.country
        holder.binding.tvPlace.text = triplistItem.place
        holder.binding.tvDate.text = triplistItem.date

        holder.binding.tvCategory.text = triplistItem.category.name

        holder.binding.cbVisited.setOnCheckedChangeListener { buttonView, isChecked ->
            triplistItem.visited = isChecked
            listener.onItemChanged(triplistItem)
        }

        holder.binding.ibEdit.setOnClickListener {
            listener.onItemEdited(triplistItem)
        }

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemRemoved(triplistItem)
        }

        holder.bind(triplistItem.country, triplistItem.place, triplistItem.description)
    }

    @DrawableRes()
    private fun getImageResource(category: TriplistItem.Category): Int {
        return when (category) {
            TriplistItem.Category.OUTDOORS -> R.drawable.outdoors
            TriplistItem.Category.BEACHES -> R.drawable.beaches
            TriplistItem.Category.SIGHTSEEING -> R.drawable.sightseeing
            TriplistItem.Category.SKIING -> R.drawable.skiing
            TriplistItem.Category.BUSINESS -> R.drawable.business
        }
    }

    fun addItem(item: TriplistItem) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
        items.sortBy { it.date }
        notifyDataSetChanged()
    }

    fun updateItem(triplistItems: List<TriplistItem>) {
        items.clear()
        items.addAll(triplistItems)
        notifyDataSetChanged()
        items.sortBy { it.date }
        notifyDataSetChanged()
    }

    fun removeItem(item: TriplistItem) {
        val pos = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(pos)
    }

    fun editItem(item: TriplistItem) {
        val position = items.indexOf(items.find { it.id == item.id })
        items.removeAt(position)
        items.add(position, item)
        notifyItemChanged(position)
        items.sortBy { it.date }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface TriplistItemClickListener {
        fun onItemChanged(item: TriplistItem)
        fun onItemRemoved(item: TriplistItem)
        fun onItemEdited(item: TriplistItem)
        fun onTripSelected(country: String?, place: String?, description: String?)
    }

    inner class TriplistViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemTriplistListBinding.bind(itemView)
        var country: String? = null
        var place: String? = null
        var description: String? = null

        init {
            binding.root.setOnClickListener { listener.onTripSelected(country, place, description) }
        }

        fun bind(newCountry: String?, newPlace: String?, newDescription: String?) {
            country = newCountry
            binding.tvCountry.text = country
            place = newPlace
            binding.tvPlace.text = place
            description = newDescription
        }
    }
}