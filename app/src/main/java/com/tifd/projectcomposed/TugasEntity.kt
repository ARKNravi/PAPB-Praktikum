package com.tifd.projectcomposed.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tugas")
data class TugasEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaMatkul: String,
    val detailTugas: String,
    var isDone: Boolean = false
)