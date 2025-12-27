package com.haotsang.wanandroidkmp.ui.arch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ArchitectureScreen(
    onNavigationToDetail: (Int) -> Unit,
    viewModel: ArchitectureViewModel = viewModel { ArchitectureViewModel() }
) {

    val architectureTree by viewModel.architectureTree.collectAsState(emptyList())
    Scaffold { innerPadding ->

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = innerPadding
        ) {

            architectureTree.forEach { data ->
                item(key = data.id) {
                    Text(text = data.name, fontSize = 18.sp)
                    Spacer(Modifier.fillMaxWidth().height(10.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        data.children.orEmpty().forEachIndexed { index, child ->
                            Card(
                                modifier = Modifier.background(MaterialTheme.colorScheme.background)
                                    .clickable {
                                        onNavigationToDetail(child.id)
//                                        navController.navigate(route = ArchChildPageParams(Json.encodeToString(data), index))
                                    },
                            ) {
                                Text(text = child.name, modifier = Modifier.padding(10.dp))
                            }
                        }
                    }
                }
            }

        }
    }
}