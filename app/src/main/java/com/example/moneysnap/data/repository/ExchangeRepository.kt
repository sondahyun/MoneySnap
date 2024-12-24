package com.example.moneysnap.data.repository

import com.example.moneysnap.data.network.Exchange
import com.example.moneysnap.data.network.ExchangeService
import kotlinx.coroutines.flow.Flow

class ExchangeRepository(private val exchangeService: ExchangeService) {

    suspend fun getMovies(authkey: String, searchdate: String, data: String) : List<Exchange>? {
        return exchangeService.getExchanges(authkey , searchdate, data)
    }

}