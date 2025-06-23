package com.j920.animatedcomposables.componants

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.j920.animatedcomposables.utils.fadeAnimation
import com.j920.animatedcomposables.models.UiState

@Composable
fun <S> StatefulContainer(
    modifier: Modifier = Modifier,
    switchTransition: () -> ContentTransform = {
        fadeAnimation()
    },
    uiState: UiState<S> = UiState.Initial,
    initialContent: @Composable () -> Unit = {},
    loadingContent: @Composable () -> Unit = {},
    errorContent: @Composable (UiState.Error) -> Unit = {},
    emptyContent: @Composable () -> Unit = {},
    content: @Composable (UiState.Success<S>) -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = uiState,
        transitionSpec = {
            switchTransition()
        }
    ) { state ->
        when (state) {
            is UiState.Initial -> {
                initialContent()
            }

            is UiState.Loading -> {
                loadingContent()
            }

            is UiState.Success -> {
                content(state)
            }

            is UiState.Error -> {
                errorContent(state)
            }

            is UiState.Empty -> {
                emptyContent()
            }
        }
    }
}
