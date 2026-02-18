package io.github.lunex_app.ui.activity.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.lunex_app.data.local.LunexDao
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(private val dao: LunexDao) : ViewModel() {
    val archives = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = true,
            prefetchDistance = 5
        ),
        pagingSourceFactory = { dao.getAllArchives() }
    ).flow.cachedIn(viewModelScope)
}
