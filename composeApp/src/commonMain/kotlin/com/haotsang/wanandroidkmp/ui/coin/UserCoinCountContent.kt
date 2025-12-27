package com.haotsang.wanandroidkmp.ui.coin

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.ViewKanban
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.haotsang.wanandroidkmp.model.bean.UserCoinCountData
import com.haotsang.wanandroidkmp.model.bean.UserCoinCountListData
import com.haotsang.wanandroidkmp.ui.common.PagingFullLoadLayout
import com.haotsang.wanandroidkmp.ui.common.collectAsLazyEmptyPagingItems
import com.haotsang.wanandroidkmp.ui.common.pagingFooter
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCoinCountScreen(
    viewModel: UserCoinCountViewModel = koinViewModel(),
    onBack: () -> Unit,
    onNavigateToRanking: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val userCoinCountState by viewModel.userCoinCountState.collectAsState()
    val userCoinCountListState = viewModel.userCoinCountList.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.userCoinCount()
    }

    Scaffold(modifier = Modifier.fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
        LargeTopAppBar(
            title = { TitleCoinCount(userCoinCountState) },
            actions = {
                IconButton(onClick = onNavigateToRanking) {
                    Icon(
                        imageVector = Icons.Outlined.ViewKanban,
                        contentDescription = null,
                        modifier = Modifier.rotate(180f)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                actionIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
                navigationIconContentColor = contentColorFor(MaterialTheme.colorScheme.background),
            ),
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }, content = {
        PagingFullLoadLayout(
            modifier = Modifier.fillMaxSize().padding(it),
            pagingState = userCoinCountListState,
            content = {
                UserCoinCountContent(
                    modifier = Modifier.fillMaxSize(),
                    userCoinCountList = userCoinCountListState,
                )
            })
    })
}

@Composable
private fun TitleCoinCount(userCoinCountState: UserCoinCountData) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val animatable = remember { Animatable(0f) }
        LaunchedEffect(userCoinCountState.coinCount) {
            animatable.animateTo(
                targetValue = userCoinCountState.coinCount.toFloat(),
                animationSpec = tween(1000)
            )
        }
        Text(text = "我的积分👉")
        Text(
            text = animatable.value.roundToInt().toString(),
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun UserCoinCountContent(
    modifier: Modifier = Modifier,
    userCoinCountList: LazyPagingItems<UserCoinCountListData> = collectAsLazyEmptyPagingItems(),
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(userCoinCountList.itemCount) { index ->
            val data = userCoinCountList[index]
            if (data != null) {
                UserCoinCountItem(data)
            }
        }
        pagingFooter(userCoinCountList)
    }
}

@Composable
private fun UserCoinCountItem(data: UserCoinCountListData) {
    ListItem(
        modifier = Modifier.fillMaxWidth(), headlineContent = {
            Text(text = data.desc, maxLines = 2)
        }, supportingContent = {
            Text(
                text = data.dateFormatString,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }, trailingContent = {
            Text(
                text = data.coinCount.toString(),
                color = MaterialTheme.colorScheme.primary,
            )
        })
}