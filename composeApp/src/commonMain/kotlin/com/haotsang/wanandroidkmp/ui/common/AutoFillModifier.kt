package com.haotsang.wanandroidkmp.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

private const val TAG = "AutoFillModifier"

fun Modifier.autofill(
    autofillTypes: List<AutofillType>,
    onFill: ((String) -> Unit),
) = composed {
    val autofill = LocalAutofill.current
    val autofillTree = LocalAutofillTree.current

    val autofillNode = remember {
        AutofillNode(autofillTypes = autofillTypes, onFill = onFill)
    }

    DisposableEffect(autofillNode) {
        autofillTree += autofillNode
        onDispose {
            autofillTree.children.remove(autofillNode.id)
        }
    }

    onGloballyPositioned {
        autofillNode.boundingBox = it.boundsInWindow()
        println("autofill, onGloballyPositioned, boundsInWindow = ${it.boundsInWindow()}")
    }.onFocusChanged { focusState ->
        autofill?.run {
            if (focusState.isFocused) {
                requestAutofillForNode(autofillNode)
            } else {
                cancelAutofillForNode(autofillNode)
            }
        }
    }
}



@Composable
private fun Username(
    userName: String,
    onValueChanged: (String) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
) {
    OutlinedTextField(
        value = userName,
        onValueChange = onValueChanged,
        label = { Text("Username") },
        supportingText = {
            if (!error.isNullOrEmpty()) {
                Text(error)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onNext = { onNextClick() }),
        isError = !error.isNullOrEmpty(),
        modifier = modifier
            .fillMaxWidth()
            .autofill(autofillTypes = listOf(AutofillType.Username), onFill = onValueChanged),
        singleLine = true,
    )
}

@Composable
private fun Password(
    password: String,
    onValueChanged: (String) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
) {
    val visualTransformation = remember { PasswordVisualTransformation() }
    OutlinedTextField(
        value = password,
        onValueChange = onValueChanged,
        label = { Text("Password") },
        supportingText = {
            if (!error.isNullOrEmpty()) {
                Text(error)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password,
        ),
        keyboardActions = KeyboardActions(onNext = { onNextClick() }),
        isError = !error.isNullOrEmpty(),
        modifier = modifier
            .fillMaxWidth()
            .autofill(autofillTypes = listOf(AutofillType.Password), onFill = onValueChanged),
        singleLine = true,
        visualTransformation = visualTransformation,
    )
}