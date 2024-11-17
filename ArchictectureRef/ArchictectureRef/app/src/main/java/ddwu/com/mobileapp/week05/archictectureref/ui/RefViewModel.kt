package ddwu.com.mobileapp.week05.archictectureref.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ddwu.com.mobileapp.week05.archictectureref.data.RefRepository
import ddwu.com.mobileapp.week05.archictectureref.data.database.RefEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RefViewModel (val refRepository: RefRepository) : ViewModel() {
    // Flow 를 사용하여 지속 관찰
    val allRefs : LiveData<List<RefEntity>> = refRepository.allRefs.asLiveData()

    // one-shot 결과를 확인하고자 할 때 사용
    // LiveData는 값 변경 불가: MutableLiveData는 값 변경 가능
    // 이용해서 간접적으로 값 바꿈
    private var _name = MutableLiveData<String>()
    
    // ViewModel에서 화면이 파괴되도 사용하려면 멤버변수로 저장
    // MutableLiveData의 _name을 가르킴
    // _name이 바뀌면 nameData도 바뀜
    val nameData : LiveData<String> = _name

    // viewModelScope 는 Dispatcher.Main 이므로 긴시간이 걸리는 IO 작업은 Dispatchers.IO 에서 작업
    fun findName(id: Int) = viewModelScope.launch { // CoroutineScope(Dispatchers.Main).launch
        var result : String
        withContext(Dispatchers.IO) { // Main에서 DB 작업하면 막힘 -> IO로 쓰레드 사용
            result = refRepository.getNameById(id)
        }
        _name.value = result
    }

}