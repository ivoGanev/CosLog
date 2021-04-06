package com.ifyezedev.coslog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifyezedev.coslog.databinding.ElementItemToBuyBinding
import com.ifyezedev.coslog.databinding.FragmentElementToBuyListBinding

class ElementsToBuyListFragment : CosplayBaseFragment<FragmentElementToBuyListBinding>() {
    override fun bindingLayoutId(): Int  = R.layout.fragment_element_to_buy_list
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.adapter = Adapter(listOf(1,2,3,4))
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}

private class Adapter(private val data: List<Any>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ElementItemToBuyBinding = ElementItemToBuyBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.element_item_to_buy, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.toBuyPriceText.text = data[position].toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

