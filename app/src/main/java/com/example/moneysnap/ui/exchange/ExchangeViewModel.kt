package com.example.moneysnap.ui.exchange

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneysnap.data.network.Exchange
import com.example.moneysnap.data.repository.ExchangeRepository
import kotlinx.coroutines.launch


class ExchangeViewModel (val exchangeRepository: ExchangeRepository) : ViewModel() {

    // DB 관련 참고 부분
    /*
    // Flow 를 사용하여 지속 관찰
    val allRefs : LiveData<List<RefEntity>> = refRepository.allRefs.asLiveData()

    // one-shot 결과를 확인하고자 할 때 사용
    private var _name = MutableLiveData<String>()
    val nameData = _name

    // viewModelScope 는 Dispatcher.Main 이므로 긴시간이 걸리는 IO 작업은 Dispatchers.IO 에서 작업
    fun findName(id: Int) = viewModelScope.launch {
        var result : String
        withContext(Dispatchers.IO) {
            result = refRepository.getNameById(id)
        }
        _name.value = result
    }
    */

    private var _exchanges = MutableLiveData<List<Exchange>?>()
    val exchanges = _exchanges

    fun getExchanges(authkey: String, searchdate: String, data: String) = viewModelScope.launch {
        try {
            val result = exchangeRepository.getExchanges(authkey, searchdate, data)
            if (result != null) {
                _exchanges.value = result
            } else {
                _exchanges.value = emptyList() // 빈 리스트 설정
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _exchanges.value = emptyList()
        }
    }

}