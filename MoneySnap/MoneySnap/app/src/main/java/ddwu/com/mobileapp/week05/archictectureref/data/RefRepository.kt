package ddwu.com.mobileapp.week05.archictectureref.data

import androidx.lifecycle.LiveData
import ddwu.com.mobileapp.week05.archictectureref.data.database.RefDao
import ddwu.com.mobileapp.week05.archictectureref.data.database.RefEntity
import ddwu.com.mobileapp.week05.archictectureref.data.network.RefService
import kotlinx.coroutines.flow.Flow     // Flow 사용 시 직접 추가

class RefRepository(private val refDao: RefDao, private val refService: RefService) {
    val allRefs : Flow<List<RefEntity>> = refDao.getAllRefs()

    suspend fun getNameById(id: Int) : String {
        return refDao.getNameById(id)
    }
}

