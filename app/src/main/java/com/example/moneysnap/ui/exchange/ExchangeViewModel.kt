package com.example.moneysnap.ui.exchange

import androidx.lifecycle.ViewModel
import com.example.moneysnap.data.repository.ExchangeRepository


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

//    private var _movies = MutableLiveData<List<Movie>?>()
//    val movies = _movies
//
//    fun getMovies(key: String, date: String) = viewModelScope.launch {
//        var result: List<Movie>?
//        // withContext(Dispatchers.IO) {       // retrofit 에 coroutine 적용 시 불필요
//        result = refRepository.getMovies(key, date)
//        // }
//        _movies.value = result
//    }

}