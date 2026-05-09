package com.haotsang.wanandroidkmp.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import com.haotsang.wanandroidkmp.model.UiState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val username by viewModel.usernameInput.collectAsState()
    val password by viewModel.passwordInput.collectAsState()
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is UiState.Success) {
            onBack()
        }
    }

    // 重置状态
    LaunchedEffect(Unit) {
        viewModel.resetLoginState()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("登录") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserNameInput(username = username, onUserNamChange = { viewModel.usernameUpdate(it) })

                PasswordInput(password = password, onPasswordChange = { viewModel.passwordUpdate(it) })

                // 错误信息显示
                if (loginState is UiState.Failed) {
                    Text(
                        text = (loginState as UiState.Failed).message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    onClick = { viewModel.login() },
                    enabled = loginState !is UiState.Loading,
                    content = {
                        if (loginState is UiState.Loading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text("登录")
                        }
                    }
                )

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateToRegister,
                    content = { Text("注册账号") }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Wan Android",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge.copy(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    ), drawStyle = Stroke(3f)
                ),
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }
    }



}

@Composable
private fun UserNameInput(username: String, onUserNamChange: (String) -> Unit) {
    OutlinedTextField(
        value = username,
        onValueChange = onUserNamChange,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
        ),
        singleLine = true,
        label = {
            Text("账号")
        },
        placeholder = {
            Text("请输入账号")
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PasswordInput(password: String, onPasswordChange: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        label = {
            Text("密码")
        },
        placeholder = {
            Text("请输入密码")
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun RePasswordInput(rePassword: String, onRePasswordChange: (String) -> Unit) {
    OutlinedTextField(
        value = rePassword,
        onValueChange = onRePasswordChange,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        label = {
            Text("确认密码")
        },
        placeholder = {
            Text("请再次输入密码")
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val username by viewModel.usernameInput.collectAsState()
    val password by viewModel.passwordInput.collectAsState()
    val rePassword by viewModel.rePasswordInput.collectAsState()
    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        if (registerState is UiState.Success) {
            onBack()
        }
    }

    // 重置状态
    LaunchedEffect(Unit) {
        viewModel.resetRegisterState()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("注册") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserNameInput(username = username, onUserNamChange = { viewModel.usernameUpdate(it) })

                PasswordInput(password = password, onPasswordChange = { viewModel.passwordUpdate(it) })

                RePasswordInput(rePassword = rePassword, onRePasswordChange = { viewModel.rePasswordUpdate(it) })

                // 错误信息显示
                if (registerState is UiState.Failed) {
                    Text(
                        text = (registerState as UiState.Failed).message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    onClick = { viewModel.register() },
                    enabled = registerState !is UiState.Loading,
                    content = {
                        if (registerState is UiState.Loading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text("注册")
                        }
                    }
                )

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateToLogin,
                    content = {
                        Text("已有账号？立即登录")
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Wan Android",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge.copy(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    ), drawStyle = Stroke(3f)
                ),
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }
    }
}
