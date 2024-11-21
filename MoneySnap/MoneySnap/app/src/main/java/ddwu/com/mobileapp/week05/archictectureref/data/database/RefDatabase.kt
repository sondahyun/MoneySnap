package ddwu.com.mobileapp.week05.archictectureref.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [RefEntity::class], version=1)
abstract class RefDatabase : RoomDatabase() {
    abstract fun refDao() : RefDao

    companion object {
        @Volatile
        private var INSTANCE : RefDatabase? = null

        fun getDatabase(context: Context) : RefDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, RefDatabase::class.java, "my_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}