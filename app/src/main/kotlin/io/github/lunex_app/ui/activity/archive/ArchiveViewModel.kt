package io.github.lunex_app.ui.activity.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.lunex_app.data.local.LunexDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(private val dao: LunexDao) : ViewModel() {
    private var archiveId = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val archive = archiveId
        .filterNotNull()
        .flatMapLatest { id -> dao.getArchiveWithCount(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setArchive(id: Long?) {
        archiveId.value = id
    }
}
