package com.haotsang.wanandroidkmp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.haotsang.wanandroidkmp.model.UiState
import com.haotsang.wanandroidkmp.model.UiState.Companion.isLoginExpired
import com.haotsang.wanandroidkmp.model.UiState.Companion.successDataOrNull
import com.haotsang.wanandroidkmp.model.bean.UserFullInfoBean
import com.haotsang.wanandroidkmp.ui.Collections
import com.haotsang.wanandroidkmp.ui.Login
import com.haotsang.wanandroidkmp.ui.TopLevelRoute
import com.haotsang.wanandroidkmp.ui.Setting
import com.haotsang.wanandroidkmp.ui.UserCoin
import com.haotsang.wanandroidkmp.ui.common.WanCenterAlignedTopAppBar
import com.haotsang.wanandroidkmp.ui.common.FullUiStateLayout
import com.haotsang.wanandroidkmp.ui.common.LoadingDialog
import com.haotsang.wanandroidkmp.ui.common.MessageDialog
import com.haotsang.wanandroidkmp.ui.icon
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import wanandroidkmp.composeapp.generated.resources.Res
import wanandroidkmp.composeapp.generated.resources.icon

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateTo: (TopLevelRoute) -> Unit
) {

    val userInfoState by viewModel.userInfoState.collectAsState()
    val logoutState by viewModel.logoutState.collectAsState(null)

    LaunchedEffect(Unit) {
        viewModel.userInfo()
    }

    // 退出登录成功主动更新userinfo
    LaunchedEffect(logoutState) {
        if (logoutState?.successDataOrNull != null) {
            viewModel.userInfo()
        }
    }

    logoutState?.let { uiState ->
        LogoutUiState(uiState = uiState)
    }

    UserInfoContent(
        userInfoState = userInfoState,
        userNavigator = listOf(UserCoin, Collections, Setting),
        needLogin = { onNavigateTo(Login) },
        refresh = { viewModel.userInfo() },
        onLogout = { viewModel.logout() },
        itemClick = {
            onNavigateTo(it)
        },
    )

}


@Composable
private fun LogoutUiState(uiState: UiState<Boolean>) {
    when (uiState) {
        is UiState.Exception -> {
            MessageDialog(true, uiState.throwable.message.orEmpty())
        }

        is UiState.Failed -> {
            MessageDialog(true, uiState.message)
        }

        is UiState.Loading -> {
            LoadingDialog(true)
        }

        is UiState.Success -> {
            MessageDialog(true, "退出登录成功")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserInfoContent(
    userInfoState: UiState<UserFullInfoBean?>? = null,
    userNavigator: List<TopLevelRoute> = emptyList(),
    itemClick: (TopLevelRoute) -> Unit = {},
    needLogin: () -> Unit = {},
    refresh: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            WanCenterAlignedTopAppBar(title = "我的", scrollBehavior = scrollBehavior)
        }, modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = innerPadding
        ) {
            item {
                Box(
                    modifier = Modifier.background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.tertiary,
                            )
                        )
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    FullUiStateLayout(
                        modifier = Modifier.fillMaxWidth()
                            .systemBarsPadding(),
                        uiState = userInfoState,
                        onRetry = {
                            if (userInfoState?.isLoginExpired == true) {
                                needLogin.invoke()
                            } else {
                                refresh.invoke()
                            }
                        },
                        content = { data ->
                            if (data != null) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().aspectRatio(1.8f)
                                        .align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.icon),
                                        contentDescription = null,
                                        modifier = Modifier.padding(vertical = 12.dp).size(50.dp)
                                            .clip(CircleShape).border(
                                                width = 2.dp,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                shape = CircleShape
                                            )
                                    )
                                    Text(
                                        text = data.userInfoData?.username ?: "",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                    Text(
                                        text = "ID:${data.coinInfoData?.userId ?: 0}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Text(
                                            text = "等级:${data.coinInfoData?.level ?: 0}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        )
                                        Text(
                                            text = "排名: ${data.coinInfoData?.rank ?: 0}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        )
                                    }
                                    Button(onClick = {
                                        onLogout()
                                    }) {
                                        Text("退出登录")
                                    }
                                }
                            }
                        }
                    )
                }
            }
            items(userNavigator) { navigator ->
                ListItem(modifier = Modifier.fillMaxWidth()
                    .clickable {
                        // 检查是否需要登录
                        if (navigator in listOf(UserCoin, Collections)) {
                            if (userInfoState?.successDataOrNull == null) {
                                needLogin()
                                return@clickable
                            }
                        }
                        itemClick.invoke(navigator)
                    },
                    leadingContent = {
                        Icon(
                            imageVector = navigator.icon,
                            contentDescription = null
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowRight,
                            contentDescription = null
                        )
                    },
                    headlineContent = {
                        Text(navigator.title, fontWeight = FontWeight.Bold)
                    }
                )
            }
        }
    }
}