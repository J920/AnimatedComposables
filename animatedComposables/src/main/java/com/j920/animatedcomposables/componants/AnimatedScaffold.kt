package com.j920.animatedcomposables.componants

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.j920.animatedcomposables.utils.fadeAnimation
import com.j920.animatedcomposables.models.AnimationSpec
import com.j920.animatedcomposables.models.UiState
import com.j920.animatedcomposables.utils.getEnterAnimation
import com.j920.animatedcomposables.utils.getExitAnimation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <S> AnimatedScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    delayInMillis: Int = 0,
    durationMillis: Int = 500,
    animationSpec: AnimationSpec = AnimationSpec.VerticalUp,
    enterTransition: (delay: Int) -> EnterTransition = { delay ->
        animationSpec.getEnterAnimation(durationMillis = durationMillis, delay = delay)
    },
    exitTransition: (delay: Int) -> ExitTransition = { delay ->
        animationSpec.getExitAnimation(
            durationMillis = durationMillis,
            delay = delay
        )
    },
    switchTransition: () -> ContentTransform = {
        fadeAnimation()
    },
    showFloatingActionButton: Boolean = true,
    isRefreshing: Boolean = false,
    uiState: UiState<S> = UiState.Initial,
    onRefresh: () -> Unit = {},
    pullRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    pullRefreshIndicator: @Composable BoxScope.() -> Unit = {
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = pullRefreshState
        )
    },
    initialContent: @Composable () -> Unit = {},
    emptyContent: @Composable () -> Unit = {},
    loadingContent: @Composable () -> Unit = {},
    errorContent: @Composable (UiState.Error) -> Unit = {},
    content: @Composable (PaddingValues, UiState.Success<S>) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = { AnimatedFab(showFloatingActionButton, floatingActionButton) },
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = { padding ->
            AnimateAlwaysEnter(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                isSavable = true,
                enter = enterTransition(delayInMillis),
                exit = exitTransition(delayInMillis)
            ) {
                PullToRefreshBox(
                    state = pullRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                    indicator = pullRefreshIndicator,
                ) {
                    StatefulContainer(
                        uiState = uiState,
                        switchTransition = switchTransition,
                        initialContent = initialContent,
                        emptyContent = emptyContent,
                        loadingContent = loadingContent,
                        errorContent = errorContent,
                        content = { content(padding, it) }
                    )
                }

            }
        }
    )
}

