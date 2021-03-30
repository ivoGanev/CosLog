package com.ifyezedev.coslog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.databinding.ElementItemToBuyBinding
import com.ifyezedev.coslog.databinding.ElementItemToMakeBinding
import com.ifyezedev.coslog.databinding.FragmentElementToMakeBinding

class ElementsToMakeFragment : BindingFragment<FragmentElementToMakeBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_element_to_make
    private lateinit var recyclerView: RecyclerView

    override fun onBindingCreated() {
        recyclerView = binding.elementToMakeRv
        recyclerView.adapter = Adapter(listOf("one", "two", "three", "four"))
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private class Adapter(private val data: List<Any>) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val binding: ElementItemToMakeBinding = ElementItemToMakeBinding.bind(itemView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.element_item_to_make, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.elementItemToMakeName.text = data[position].toString()
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }
}

