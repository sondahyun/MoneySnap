package ddwu.com.mobileapp.week05.archictectureref.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "ref_table")
data class RefEntity(
    @PrimaryKey (autoGenerate = true)
    val _id: Int,
    val name: String
)
