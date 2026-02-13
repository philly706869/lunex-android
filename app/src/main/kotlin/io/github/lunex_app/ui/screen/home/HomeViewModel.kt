package io.github.lunex_app.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.lunex_app.data.local.ArchiveEntity
import io.github.lunex_app.data.local.LunexDao
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: LunexDao,
) : ViewModel() {
    val archives = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = true,
            prefetchDistance = 5
        ),
        pagingSourceFactory = { dao.getAllArchives() }
    ).flow.cachedIn(viewModelScope)

    fun createArchive(name: String) = viewModelScope.launch {
        dao.insertArchive(ArchiveEntity(name = name))
    }
}
