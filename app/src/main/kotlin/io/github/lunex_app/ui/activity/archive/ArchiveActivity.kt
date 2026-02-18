package io.github.lunex_app.ui.activity.archive

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.github.lunex_app.ui.LunexActivity
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class ArchiveActivity : LunexActivity() {
    private companion object {
        val KEY = this::class.qualifiedName!!
    }

    @Parcelize
    data class Input(val archiveId: Long) : Parcelable

    class Contract : ActivityResultContract<Input, Unit>() {
        override fun createIntent(context: Context, input: Input): Intent {
            return Intent(context, ArchiveActivity::class.java).apply {
                putExtra(KEY, input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?) = Unit
    }

    private val args: Input? by lazy { intent.getParcelableExtra(KEY, Input::class.java) }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content() {
        val vm = hiltViewModel<ArchiveViewModel>()
        LaunchedEffect(Unit) { vm.setArchive(args?.archiveId) }
        val archive by vm.archive.collectAsStateWithLifecycle()

        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Column {
                        Text(
                            archive?.data?.name ?: "",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            archive?.itemCount?.toString() ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                })
            },
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {

            }
        }
    }
}
