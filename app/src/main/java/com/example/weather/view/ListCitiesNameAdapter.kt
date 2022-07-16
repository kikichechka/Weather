package com.example.weather.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentListCityItemBinding
import com.example.weather.model.Weather

class ListCitiesNameAdapter(
    private var onItemListClickListener: OnClickListener,
    private var list: List<Weather> = listOf()
) : RecyclerView.Adapter<ListCitiesNameAdapter.MyViewHolder>() {

    fun setList(thisList: List<Weather>) {
        this.list = thisList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FragmentListCityItemBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(weather: Weather) {
            FragmentListCityItemBinding.bind(itemView).apply {
                itemCityName.text = weather.city.name
                itemCityName.setOnClickListener { onItemListClickListener.onItemClick(weather) }

                if (weather.like) {
                    likeInItemNote.background =
                        ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_star)
                }
            }
        }
    }
}