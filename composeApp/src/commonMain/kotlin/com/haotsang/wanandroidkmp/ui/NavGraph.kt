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
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.haotsang.wanandroidkmp.ui.arch.ArchitectureChildScreen
import com.haotsang.wanandroidkmp.ui.arch.ArchitectureScreen
import com.haotsang.wanandroidkmp.ui.coin.UserCoinCountScreen
import com.haotsang.wanandroidkmp.ui.home.HomeScreen
import com.haotsang.wanandroidkmp.ui.login.LoginScreen
import com.haotsang.wanandroidkmp.ui.profile.ProfileScreen
import com.haotsang.wanandroidkmp.ui.rank.RankScreen
import com.haotsang.wanandroidkmp.ui.search.SearchScreen
import com.haotsang.wanandroidkmp.ui.webview.WebViewScreen
import com.haotsang.wanandroidkmp.ui.wenda.WendaScreen

@Composable
fun MainRoute() {
    val backStack = rememberNavBackStack(NavConfig, Home)

    val bottomNavigatorDataLists = listOf(
        Home, Square, Wenda, Architecture, Profile
    )

    val isNavigation = bottomNavigatorDataLists.any { it == backStack.last() }

    val compactMode = true
    if (compactMode) {
        Column(modifier = Modifier.fillMaxSize()) {
            NavGraph(backStack = backStack, modifier = Modifier.weight(1f))
            AnimatedNavigation(
                modifier = Modifier.fillMaxWidth(),
                isNavigation = isNavigation,
                compactMode = compactMode,
                bottomNavigatorDataLists = bottomNavigatorDataLists,
                backStack = backStack
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            AnimatedNavigation(
                modifier = Modifier.fillMaxHeight().width(100.dp).zIndex(12f),
                isNavigation = isNavigation,
                compactMode = compactMode,
                bottomNavigatorDataLists = bottomNavigatorDataLists,
                backStack = backStack
            )
            NavGraph(backStack = backStack, modifier = Modifier.weight(1f))
        }
    }

}

@Composable
fun NavGraph(backStack: NavBackStack<NavKey>, modifier: Modifier = Modifier) {
    val onBack: () -> Unit = {
        backStack.removeLastOrNull()
    }
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Login> {
                LoginScreen(
                    onBack = onBack,
                    onNavigateToRegister = {

                    },
                )
            }
            entry<Home> {
                HomeScreen(
                    onNavigateToLogin = {
                        backStack.add(Login)
                    }, onNavigateToSearch = {
                        backStack.add(Search)
                    }, onNavigateToWebView = {
                        backStack.add(WebView(it))
                    }
                )
            }
            entry<Square> {
                Text("Square")
            }
            entry<Search> {
                SearchScreen()
            }
            entry<Profile> {
                ProfileScreen(onNavigateToLogin = {
                    backStack.add(Login)
                }, onNavigateToUserCoin = {
                    backStack.add(UserCoin)
                })
            }
            entry<Architecture> {
                ArchitectureScreen(
                    onNavigationToDetail = {
                        backStack.add(ArchitectureDetail(it))
                    }
                )
            }
            entry<ArchitectureDetail> {
                ArchitectureChildScreen(it.cid)
            }
            entry<Wenda> {
                WendaScreen()
            }
            entry<UserCoin> {
                UserCoinCountScreen(onBack = onBack, onNavigateToRanking = {
                    backStack.add(Rank)
                })
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
                Text("This is Screen C")
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
    isNavigation: Boolean,
    compactMode: Boolean = true,
    bottomNavigatorDataLists: List<Route>,
    backStack: NavBackStack<NavKey>,
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(),
) {
    AnimatedVisibility(
        visible = isNavigation,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        if (compactMode) {
            NavigationBar(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = contentColorFor(MaterialTheme.colorScheme.background)
            ) {
                bottomNavigatorDataLists.forEach {
                    NavigationBarItem(selected = backStack.last() == it, onClick = {
                        backStack[backStack.lastIndex] = it
                    }, icon = {
                        Icon(it.icon, contentDescription = null)
                    }, label = {
                        Text(text = it.title)
                    })
                }
            }
        } else {
            NavigationRail(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = contentColorFor(MaterialTheme.colorScheme.background)
            ) {
                bottomNavigatorDataLists.forEach {
                    NavigationRailItem(selected = backStack.last() == it, onClick = {
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