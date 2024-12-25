package com.example.moneysnap.ui.exchange

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneysnap.data.network.Exchange
import com.example.moneysnap.databinding.ItemExchangeRateBinding

class ExchangeAdapter(private var exchanges: List<Exchange>) :
    RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    inner class ExchangeViewHolder(private val binding: ItemExchangeRateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exchange: Exchange) {
            binding.currencyName.text = exchange.curUnit
            binding.countryName.text = exchange.curNm
            binding.exchangeRate.text = "매매 기준율: ${exchange.dealBasR}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val binding = ItemExchangeRateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExchangeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        holder.bind(exchanges[position])
    }

    override fun getItemCount(): Int = exchanges.size

    // 데이터 업데이트 메서드
    fun updateData(newExchanges: List<Exchange>) {
        exchanges = newExchanges
        if (newExchanges.isEmpty()) {
            Log.d("ExchangeAdapter", "No data available")
        }
        notifyDataSetChanged()
    }
}