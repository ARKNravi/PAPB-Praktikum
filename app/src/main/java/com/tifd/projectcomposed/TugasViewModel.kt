package com.tifd.projectcomposed.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tifd.projectcomposed.data.TugasDatabase
import com.tifd.projectcomposed.data.TugasEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TugasViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TugasDatabase.getDatabase(application)
    private val tugasDao = database.tugasDao()

    private val _tugas = MutableStateFlow<List<TugasEntity>>(emptyList())
    val tugas = _tugas.asStateFlow()

    init {
        viewModelScope.launch {
            tugasDao.getAllTugas().collect {
                _tugas.value = it
            }
        }
    }

    fun addTugas(namaMatkul: String, detailTugas: String, imageUri: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            tugasDao.insertTugas(
                TugasEntity(
                    namaMatkul = namaMatkul,
                    detailTugas = detailTugas,
                    imageUri = imageUri // Save image URI
                )
            )
        }
    }

    fun toggleTugasDone(tugas: TugasEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            tugasDao.updateTugas(tugas.copy(isDone = !tugas.isDone))
        }
    }
}
