package io.github.lunex_app.ui.activity.explorer

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.name

class Explorer(base: Path, var name: String) {
    var base: Path = base
        set(value) {
            field = value
            path = Path(".")
        }

    var path: Path = Path(".")
        set(value) {
            field = value
            update()
        }

    private val _folders = mutableListOf<String>()
    private val _files = mutableListOf<String>()
    val folders: List<String> get() = _folders
    val files: List<String> get() = _files

    init {
        update()
    }

    private fun update() {
        val path = base.resolve(path)
        _folders.clear()
        _files.clear()
        Files.list(path).forEach {
            if (it.isDirectory()) _folders.add(it.name)
            else _files.add(it.name)
        }
    }
}
