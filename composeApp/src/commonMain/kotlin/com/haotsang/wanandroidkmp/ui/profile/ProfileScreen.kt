package com.haotsang.wanandroidkmp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.haotsang.wanandroidkmp.model.UiState
import com.haotsang.wanandroidkmp.model.UiState.Companion.isLoginExpired
import com.haotsang.wanandroidkmp.model.UiState.Companion.successDataOrNull
import com.haotsang.wanandroidkmp.model.bean.UserFullInfoBean
import com.haotsang.wanandroidkmp.model.bean.UserInfoBean
import com.haotsang.wanandroidkmp.ui.Collections
import com.haotsang.wanandroidkmp.ui.Route
import com.haotsang.wanandroidkmp.ui.Setting
import com.haotsang.wanandroidkmp.ui.UserCoin
import com.haotsang.wanandroidkmp.ui.common.FullUiStateLayout
import com.haotsang.wanandroidkmp.ui.common.LoadingDialog
import com.haotsang.wanandroidkmp.ui.common.MessageDialog
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import wanandroidkmp.composeapp.generated.resources.Res
import wanandroidkmp.composeapp.generated.resources.icon

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToUserCoin: () -> Unit
) {

    val userInfoState by viewModel.userInfoState.collectAsState()
    val logoutState by viewModel.logoutState.collectAsState(null)

    LaunchedEffect(Unit) {
        viewModel.userInfo()
    }

    logoutState?.let { uiState ->
        LogoutUiState(
            uiState = uiState,
            refresh = { viewModel.userInfo() },
        )
    }

    UserInfoContent(
        userInfoState = userInfoState,
        userNavigator = listOf(UserCoin, Collections, Setting),
        needLogin = { onNavigateToLogin() },
        refresh = { viewModel.userInfo() },
        onLogout = { viewModel.logout() },
        itemClick = {
            when (it) {
                UserCoin -> onNavigateToUserCoin()
                else -> {

                }
            }

        },
    )

}


@Composable
private fun LogoutUiState(uiState: UiState<Boolean>, refresh: () -> Unit = {}) {
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
            MessageDialog(true, "退出登录成功", hide = refresh)
        }
    }
}

@Composable
private fun UserInfoContent(
    userInfoState: UiState<UserFullInfoBean?>? = null,
    userNavigator: List<Route> = emptyList(),
    itemClick: (Route) -> Unit = {},
    needLogin: () -> Unit = {},
    refresh: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    Scaffold {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                        loginContent = {
                            Box(
                                modifier = Modifier.fillMaxWidth().aspectRatio(1.8f)
                                    .align(Alignment.Center),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(onClick = needLogin, content = {
                                    Text(it)
                                })
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
                                        Text("退出登陆")
                                    }
                                }
                            }
                        }
                    )
                }
            }
            items(userNavigator) { navigator ->
                ListItem(modifier = Modifier.fillMaxWidth()
                    .clickable(
//                        enabled = userInfoState?.successDataOrNull != null || navigator in listOf(
//                            UserNavigator.AboutUser,
//                            UserNavigator.SystemSetting
//                        )
                    ) {
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