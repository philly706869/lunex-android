package io.github.lunex_app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data class Archive(val archiveId: Long) : Screen
}
