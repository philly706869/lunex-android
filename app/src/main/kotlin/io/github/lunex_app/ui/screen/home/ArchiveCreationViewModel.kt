package io.github.lunex_app.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.lunex_app.data.local.ArchiveEntity
import io.github.lunex_app.data.local.LunexDao
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveCreationViewModel @Inject constructor(private val dao: LunexDao) : ViewModel() {
    fun createArchive(name: String) = viewModelScope.launch {
        dao.insertArchive(ArchiveEntity(name = name))
    }
}
