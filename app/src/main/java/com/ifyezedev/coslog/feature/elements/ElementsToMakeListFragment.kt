package com.ifyezedev.coslog.feature.elements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.CosplayBaseFragment
import com.ifyezedev.coslog.R
import com.ifyezedev.coslog.databinding.ElementItemToMakeBinding
import com.ifyezedev.coslog.databinding.FragmentElementToMakeListBinding

class ElementsToMakeListFragment : CosplayBaseFragment<FragmentElementToMakeListBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_element_to_make_list
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
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
            holder.binding.durationTextView.text = data[position].toString()
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }
}

