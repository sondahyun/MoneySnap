package ddwu.com.mobileapp.week05.archictectureref

import android.app.Application
import ddwu.com.mobileapp.week05.archictectureref.data.RefRepository
import ddwu.com.mobileapp.week05.archictectureref.data.database.RefDatabase
import ddwu.com.mobileapp.week05.archictectureref.data.network.RefService

class RefApplication : Application() {
    val refDao by lazy {
        RefDatabase.getDatabase(this).refDao()
    }
    val refService by lazy {
        RefService()
    }
    val refRepository by lazy {
        RefRepository(refDao, refService)
    }
}