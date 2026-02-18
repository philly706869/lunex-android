package io.github.lunex_app.ui.activity.explorer

import android.content.Context
import android.os.storage.StorageManager
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.name

data class ExplorerState(
    val v: Int,
    val path: Path,
    val folders: List<String>,
    val files: List<String>,
)

data class Storage(val name: String, val path: Path)

@HiltViewModel
class ExplorerViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel(),
    ContainerHost<ExplorerState, Unit> {
    private val storages =
        context.getSystemService<StorageManager>()
            ?.storageVolumesIncludingSharedProfiles?.mapNotNull {
                val path = it.directory?.toPath() ?: return@mapNotNull null
                val name = it.getDescription(context)
                Storage(name = name, path = path)
            } ?: emptyList()

    private var storage = storages[0]
    private val folders = mutableListOf<String>()
    private val files = mutableListOf<String>()

    override val container =
        container<ExplorerState, Unit>(ExplorerState(0, Path("."), folders, files))

    private fun update(path: Path) {
        val path = storage.path.resolve(path)
        folders.clear()
        files.clear()
        Files.list(path).forEach {
            if (it.isDirectory()) folders.add(it.name)
            else files.add(it.name)
        }
    }

    fun setStorage(storage: Storage) = intent {
        this@ExplorerViewModel.storage = storage
        reduce {
            update(state.path)
            state.copy(v = state.v + 1)
        }
    }

    fun setPath(path: Path) = intent {
        reduce {
            update(path)
            state.copy(v = state.v + 1, path = path)
        }
    }
}