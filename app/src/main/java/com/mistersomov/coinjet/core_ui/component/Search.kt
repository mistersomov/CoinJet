package com.mistersomov.coinjet.core_ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.core_ui.MainTheme
import kotlinx.coroutines.launch

/**
 * Search widget to embed in a searchable screen
 * @param placeholderText text for the placeholder
 * @param resultContent content resulting from a search query
 * @param onFocusChanged action that takes place when the search field focus is received
 * @param onValueChanged action that occurs when a search query changes
 */
@Composable
fun Search(
    modifier: Modifier = Modifier,
    placeholderText: String?,
    resultContent: @Composable () -> Unit,
    onFocusChanged: () -> Unit,
    onValueChanged: (String) -> Unit,
    onCancelClicked: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val text = rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val isExpanded = remember { mutableStateOf(false) }
    val maxChar = 12

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(weight = if (!isExpanded.value) 0.5f else 1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        isExpanded.value = it.isFocused
                        onFocusChanged.invoke()
                    },
                value = text.value,
                onValueChange = {
                    scope.launch {
                        isExpanded.value = true
                        text.value = it.take(maxChar)
                        onValueChanged.invoke(it)
                    }
                },
                placeholder = {
                    if (!placeholderText.isNullOrBlank()) {
                        Text(text = placeholderText)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.search),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (isExpanded.value) {
                        Icon(
                            modifier = Modifier.clickable(interactionSource = remember {
                                MutableInteractionSource()
                            },
                                indication = null,
                                onClick = {
                                    text.value = ""
                                }
                            ),
                            imageVector = ImageVector.vectorResource(id = R.drawable.close_circle),
                            contentDescription = null
                        )
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = with(CoinJetTheme.colors) {
                    TextFieldDefaults.textFieldColors(
                        backgroundColor = surfaceVariant,
                        cursorColor = primary,
                        errorCursorColor = error,
                        focusedIndicatorColor = Color.Unspecified,
                        unfocusedIndicatorColor = Color.Unspecified,
                        leadingIconColor = onSurfaceVariant,
                        trailingIconColor = onSurfaceVariant,
                        errorTrailingIconColor = error,
                        focusedLabelColor = primary,
                        unfocusedLabelColor = onSurface.copy(ContentAlpha.medium),
                        errorLabelColor = error,
                        placeholderColor = onSurface.copy(ContentAlpha.medium),
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            if (isExpanded.value) {
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                text.value = ""
                                focusManager.clearFocus(force = true)
                                onCancelClicked.invoke()
                            }
                        ),
                    text = stringResource(id = R.string.cancel),
                    style = CoinJetTheme.typography.titleMedium,
                    color = CoinJetTheme.colors.primary
                )
            }
        }
        if (isExpanded.value) {
            resultContent.invoke()
        }
    }
}

@Preview(showBackground = true, name = "Search")
@Composable
fun PreviewSearch() {
    MainTheme() {
        Search(
            placeholderText = "ETHEREUM, BTC",
            resultContent = { },
            onFocusChanged = {},
            onValueChanged = {},
            onCancelClicked = {}
        )
    }
}