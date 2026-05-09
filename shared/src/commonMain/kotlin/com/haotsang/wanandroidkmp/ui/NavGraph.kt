package com.haotsang.wanandroidkmp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
//import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.haotsang.wanandroidkmp.ui.arch.ArchitectureChildScreen
import com.haotsang.wanandroidkmp.ui.arch.ArchitectureScreen
import com.haotsang.wanandroidkmp.ui.coin.UserCoinCountScreen
import com.haotsang.wanandroidkmp.ui.home.HomeScreen
import com.haotsang.wanandroidkmp.ui.login.LoginScreen
import com.haotsang.wanandroidkmp.ui.login.RegisterScreen
import com.haotsang.wanandroidkmp.ui.profile.ProfileScreen
import com.haotsang.wanandroidkmp.ui.profile.UserFavoriteScreen
import com.haotsang.wanandroidkmp.ui.rank.RankScreen
import com.haotsang.wanandroidkmp.ui.setting.SettingScreen
import com.haotsang.wanandroidkmp.ui.square.SquareScreen
import com.haotsang.wanandroidkmp.ui.webview.WebViewScreen
import com.haotsang.wanandroidkmp.ui.wenda.WendaScreen

private val topLevelRoutes: List<TopLevelRoute> = listOf(Home, Square, Wenda, Architecture, Profile)

@Composable
fun MainRoute(useNavRail: Boolean) {
    val backStack = rememberNavBackStack(NavConfig, Home)

    if (useNavRail) {
        Row(modifier = Modifier.fillMaxSize()) {
            AnimatedNavigation(
                modifier = Modifier.fillMaxHeight().width(100.dp).zIndex(12f),
                useNavRail = useNavRail,
                topLevelRoutes = topLevelRoutes,
                backStack = backStack
            )
            NavGraph(backStack = backStack, modifier = Modifier.weight(1f))
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            NavGraph(backStack = backStack, modifier = Modifier.weight(1f))
            AnimatedNavigation(
                modifier = Modifier.fillMaxWidth(),
                useNavRail = useNavRail,
                topLevelRoutes = topLevelRoutes,
                backStack = backStack
            )
        }
    }

}

@Composable
fun NavGraph(backStack: NavBackStack<NavKey>, modifier: Modifier = Modifier) {
    val onBack: () -> Unit = {
        backStack.removeLastOrNull()
    }
    fun onNavigateTo(key: NavKey) {
        backStack.add(key)
    }

    fun onReplace(key: NavKey) {
        backStack[backStack.lastIndex] = key
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = onBack,
//        entryDecorators = listOf(
//            // Add the default decorators for managing scenes and saving state
//            rememberSaveableStateHolderNavEntryDecorator(),
//            // Then add the view model store decorator
//            rememberViewModelStoreNavEntryDecorator()
//        ),
        entryProvider = entryProvider {
            entry<Login> {
                LoginScreen(
                    onBack = onBack,
                    onNavigateToRegister = {
                        onReplace(Register)
                    },
                )
            }
            entry<Register> {
                RegisterScreen(
                    onBack = onBack,
                    onNavigateToLogin = {
                        onReplace(Login)
                    }
                )
            }
            entry<Home> {
                HomeScreen(
                    onNavigateToLogin = { onNavigateTo(Login) },
                    onNavigateToWebView = { onNavigateTo(WebView(it)) }
                )
            }
            entry<Square> {
                SquareScreen(
                    onNavigateToLogin = { onNavigateTo(Login) },
                    onNavigateToWebView = { onNavigateTo(WebView(it)) }
                )
            }

            entry<Profile> {
                ProfileScreen(
                    onNavigateTo = { onNavigateTo(it) },
                )
            }
            entry<Architecture> {
                ArchitectureScreen(
                    onNavigationToDetail = {
                        onNavigateTo(ArchitectureDetail(it))
                    }
                )
            }
            entry<ArchitectureDetail> { key ->
                ArchitectureChildScreen(
                    cid = key.cid,
                    onBack = onBack,
                    onNavigateToLogin = { onNavigateTo(Login) },
                    onNavigateToWebView = { onNavigateTo(WebView(it)) }
                )
            }
            entry<Wenda> {
                WendaScreen(
                    onNavigateToLogin = { onNavigateTo(Login) },
                    onNavigateToWebView = { onNavigateTo(WebView(it)) }
                )
            }
            entry<UserCoin> {
                UserCoinCountScreen(
                    onBack = onBack,
                    onNavigateToRanking = { onNavigateTo(Rank) }
                )
            }
            entry<Collections> {
                UserFavoriteScreen(
                    onBack = onBack,
                    onNavigateToWebView = { onNavigateTo(WebView(it)) }
                )
            }
            entry<Rank> {
                RankScreen(
                    onBack = onBack,
                )
            }
            entry<WebView> {
                WebViewScreen(onBack = onBack, url = it.url)
            }
            entry<Setting>(
                metadata = NavDisplay.transitionSpec {
                    // Slide new content up, keeping the old content in place underneath
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(1000)
                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                } + NavDisplay.popTransitionSpec {
                    // Slide old content down, revealing the new content in place underneath
                    EnterTransition.None togetherWith
                            slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(1000)
                            )
                } + NavDisplay.predictivePopTransitionSpec {
                    // Slide old content down, revealing the new content in place underneath
                    EnterTransition.None togetherWith
                            slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(1000)
                            )
                }
            ) {
                SettingScreen(onBack = onBack)
            }
        },
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(1000)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(1000)
            )
        },
        popTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(1000)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(1000)
            )
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(1000)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(1000)
            )
        }
    )
}



@Composable
private fun AnimatedNavigation(
    modifier: Modifier = Modifier.fillMaxWidth(),
    useNavRail: Boolean = true,
    topLevelRoutes: List<TopLevelRoute>,
    backStack: NavBackStack<NavKey>,
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(),
) {
    val showBottomBar = topLevelRoutes.any { it == backStack.last() }
    AnimatedVisibility(
        visible = showBottomBar,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        if (useNavRail) {
            NavigationRail(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = contentColorFor(MaterialTheme.colorScheme.background)
            ) {
                topLevelRoutes.forEach {
                    NavigationRailItem(selected = backStack.last() == it, onClick = {
                        backStack[backStack.lastIndex] = it
                    }, icon = {
                        Icon(it.icon, contentDescription = null)
                    }, label = {
                        Text(text = it.title)
                    })
                }
            }
        } else {
            NavigationBar(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = contentColorFor(MaterialTheme.colorScheme.background)
            ) {
                topLevelRoutes.forEach {
                    NavigationBarItem(selected = backStack.last() == it, onClick = {
                        backStack[backStack.lastIndex] = it
                    }, icon = {
                        Icon(it.icon, contentDescription = null)
                    }, label = {
                        Text(text = it.title)
                    })
                }
            }
        }
    }
}