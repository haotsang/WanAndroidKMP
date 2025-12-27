package com.haotsang.wanandroidkmp.ui.rank

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.haotsang.wanandroidkmp.model.bean.CoinCountRankingBean
import com.haotsang.wanandroidkmp.ui.common.pagingFooter
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankScreen(
    onBack: () -> Unit,
    viewModel: RankViewModel = koinViewModel()
) {

    val coinCountRankingState = viewModel.coinCountRankingState.collectAsLazyPagingItems()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = { Text("积分排行榜") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回"
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
        ))
    }, content = { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = paddingValues) {
            items(
                coinCountRankingState.itemCount,
                key = coinCountRankingState.itemKey { it.userId }
            ) { index ->
                val data = coinCountRankingState[index]
                if (data != null) {
                    CoinCountRankingItem(
                        data = data, max = coinCountRankingState[0]?.coinCount ?: 0
                    )
                }
            }
            pagingFooter(pagingState = coinCountRankingState)
        }

    })

}

@Composable
private fun CoinCountRankingItem(data: CoinCountRankingBean, max: Int) {
    val scope = rememberCoroutineScope()
    val widthFraction by remember { derivedStateOf { if (max == 0) 1f else data.coinCount / max.toFloat() } }
    val color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
    val animatable = remember(data.userId) { Animatable(0f) }

    DisposableEffect(widthFraction) {
        scope.launch {
            animatable.animateTo(widthFraction, animationSpec = tween(1000))
        }
        onDispose {
            scope.launch {
                animatable.stop()
            }.invokeOnCompletion { scope.cancel() }
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth().drawWithContent {
            drawRect(
                color = color, size = size.copy(this.size.width * animatable.value)
            )
            drawContent()
        }.padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SuggestionChip(label = {
            Text(text = data.rankDesc, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }, onClick = {})
        Text(text = data.username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "${data.coinCount}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
    }
}
