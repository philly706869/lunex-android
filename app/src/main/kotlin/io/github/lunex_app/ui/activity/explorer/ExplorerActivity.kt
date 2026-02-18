package io.github.lunex_app.ui.activity.explorer

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.lunex_app.R
import io.github.lunex_app.ui.LunexActivity
import kotlinx.parcelize.Parcelize
import org.orbitmvi.orbit.compose.collectAsState
import kotlin.io.path.name

@AndroidEntryPoint
class ExplorerActivity : LunexActivity() {
    private companion object {
        val KEY = this::class.qualifiedName!!
    }

    @Parcelize
    data class Output(val path: String) : Parcelable

    class Contract : ActivityResultContract<Unit, Output?>() {
        override fun createIntent(context: Context, input: Unit): Intent {
            return Intent(context, ExplorerActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Output? {
            return if (resultCode == RESULT_OK) intent?.getParcelableExtra(
                KEY,
                Output::class.java
            ) else null
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content() {
        val vm = hiltViewModel<ExplorerViewModel>()
        val state by vm.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("파일 탐색기")
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Column {
                    LazyRow {
                        items(state.path.toList()) {
                            Button(onClick = { vm.setPath(it) }) {
                                Text(it.name)
                            }
                        }
                    }
                    LazyColumn {
                        items(state.folders) {
                            Item(
                                R.drawable.ic_folder,
                                it,
                                onClick = { vm.setPath(state.path.resolve(it)) },
                                onLongClick = {}
                            )
                        }
                        items(state.files) {
                            Item(
                                R.drawable.ic_draft,
                                it,
                                onClick = {},
                                onLongClick = {}
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Item(icId: Int, name: String, onClick: () -> Unit, onLongClick: () -> Unit) {
        Row(modifier = Modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick)) {
            Icon(painterResource(icId), null)
            Text(name)
        }
    }
}