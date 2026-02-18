package io.github.lunex_app.ui.activity.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.lunex_app.data.local.LunexDao
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveDeletionViewModel @Inject constructor(private val dao: LunexDao) : ViewModel() {
    fun deleteArchive(id: Long) {
        viewModelScope.launch {
            dao.deleteArchive(id)
        }
    }
}
