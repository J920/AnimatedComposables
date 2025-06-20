package com.j920.animatedcomposables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


enum class AnimationSpec {
    Fade,
    VerticalDown,
    VerticalUp,
    HorizontalRight,
    HorizontalLeft
}

enum class UiState {
    Initial,
    Success,
    Empty,
    Error,
}

fun AnimationSpec.getEnterAnimation(
    durationMillis: Int = 500,
    delay: Int = 0
): EnterTransition {
    return when (this) {
        AnimationSpec.Fade -> fadeIn(
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = delay,
            )
        )

        AnimationSpec.VerticalDown -> {
            fadeWithSlideDown(
                durationMillis = durationMillis,
                delay = delay
            )
        }

        AnimationSpec.VerticalUp -> {
            fadeWithSlideUp(
                durationMillis = durationMillis,
                delay = delay
            )
        }

        AnimationSpec.HorizontalRight -> {
            fadeWithSlideRight(
                durationMillis = durationMillis,
                delay = delay
            )
        }

        AnimationSpec.HorizontalLeft -> {
            fadeWithSlideLeft(
                durationMillis = durationMillis,
                delay = delay
            )
        }
    }
}

fun AnimationSpec.getExitAnimation(
    durationMillis: Int = 500,
    delay: Int = 0
): ExitTransition {
    return when (this) {
        AnimationSpec.Fade -> fadeOut(
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = delay,
            )
        )

        AnimationSpec.VerticalDown -> {
            fadeOutWithSlideDown(
                durationMillis = durationMillis,
                delay = delay
            )
        }

        AnimationSpec.VerticalUp -> {
            fadeOutWithSlideUp(
                durationMillis = durationMillis,
                delay = delay
            )
        }

        AnimationSpec.HorizontalRight -> {
            fadeOutWithSlideRight(
                durationMillis = durationMillis,
                delay = delay
            )
        }

        AnimationSpec.HorizontalLeft -> {
            fadeOutWithSlideLeft(
                durationMillis = durationMillis,
                delay = delay
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    orderingDelayInMillis: Int = 0,
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
    uiState: UiState = UiState.Initial,
    onRefresh: () -> Unit = {},
     pullRefreshState: PullToRefreshState = rememberPullToRefreshState(),
     pullRefreshIndicator: @Composable BoxScope.() -> Unit = {
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = state
        )
    },
    initialContent: @Composable () -> Unit = {},
    emptyContent: @Composable () -> Unit = {},
    errorContent: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
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
                enter = enterTransition(orderingDelayInMillis),
                exit = exitTransition(orderingDelayInMillis)
            ) {
                PullToRefreshBox(
                     state = pullRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                    indicator = pullRefreshIndicator,
                ) {
                    AnimatedContent(
                        targetState = uiState,
                        transitionSpec = {
                            switchTransition()
                        }
                    ) { state ->
                        when (state) {
                            UiState.Initial -> {
                                initialContent()
                            }

                            UiState.Success -> {
                                content(it)
                            }

                            UiState.Error -> {
                                errorContent()
                            }

                            UiState.Empty -> {
                                emptyContent()
                            }
                        }
                    }
                }

            }
        }
    )
}


@Composable
private fun AnimatedFab(
    showButton: Boolean,
    floatingActionButton: @Composable () -> Unit = {},
    orderingDelayInMillis: Int = 0,
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
) {
    AnimateAlwaysEnter(
        isSavable = true,
        condition = showButton,
        enter = enterTransition(orderingDelayInMillis),
        exit = exitTransition(orderingDelayInMillis)
    ) {
        floatingActionButton()
    }
}

@Composable
fun AnimatedColumn(
    modifier: Modifier = Modifier,
    order: Int = 1,
    orderingDelayInMillis: Int = 0,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
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
    content: @Composable ColumnScope.() -> Unit
) {
    AnimateAlwaysEnter(
        isSavable = true,
        enter = enterTransition(orderingDelayInMillis),
        exit = exitTransition(orderingDelayInMillis)
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }
}

@Composable
fun AnimatedRow(
    order: Int = 1,
    orderingDelayInMillis: Int = 0,
    durationMillis: Int = 500,
    animationSpec: AnimationSpec = AnimationSpec.VerticalDown,
    enterTransition: (delay: Int) -> EnterTransition = { delay ->
        animationSpec.getEnterAnimation(durationMillis = durationMillis, delay = delay)
    },
    exitTransition: (delay: Int) -> ExitTransition = { delay ->
        animationSpec.getExitAnimation(
            durationMillis = durationMillis,
            delay = delay
        )
    },
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit
) {
    AnimateAlwaysEnter(
        isSavable = false,
        enter = enterTransition(orderingDelayInMillis),
        exit = exitTransition(orderingDelayInMillis)
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content
        )
    }
}

@Composable
fun AnimatedBox(
    modifier: Modifier = Modifier,
    order: Int = 1,
    orderingDelayInMillis: Int = 0,
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
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    AnimateAlwaysEnter(
        isSavable = true,
        enter = enterTransition(orderingDelayInMillis),
        exit = exitTransition(orderingDelayInMillis)
    ) {
        Box(
            modifier = modifier,
            contentAlignment = contentAlignment,
            propagateMinConstraints = propagateMinConstraints,
        ) {
            content()
        }
    }
}

@Composable
fun LazyListScope.AnimatedItem(
    key: Any? = null,
    contentType: Any? = null,
    order: Int = 1,
    orderingDelayInMillis: Int = 0,
    durationMillis: Int = 500,
    animationSpec: AnimationSpec = AnimationSpec.VerticalUp,
    enterTransition: (order: Int, delay: Int) -> EnterTransition = { order, delay ->
        animationSpec.getEnterAnimation(durationMillis = durationMillis, delay = delay)
    },
    exitTransition: (order: Int, delay: Int) -> ExitTransition = { order, delay ->
        animationSpec.getExitAnimation(
            durationMillis = durationMillis,
            delay = delay
        )
    },
    content: @Composable LazyItemScope.() -> Unit
) {
    AnimateAlwaysEnter(
        isSavable = true,
        enter = enterTransition(order, orderingDelayInMillis),
        exit = exitTransition(order, orderingDelayInMillis)
    ) {
        item(
            key = key,
            contentType = contentType,
            content = content
        )
    }
}

@Composable
fun AnimateAlwaysEnter(
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn(animationSpec = tween(400)),
    exit: ExitTransition = fadeOut(animationSpec = tween(250)),
    condition: Boolean = true,
    isSavable: Boolean = false,
    visible: MutableState<Boolean> = mutableStateOf(!condition),
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    var visibility by remember { visible }
    var visibilitySavable by rememberSaveable { visible }

    LaunchedEffect(key1 = Unit, block = {
        visibilitySavable = true
        visibility = true
    })
    AnimatedVisibility(
        modifier = modifier,
        visible = if (isSavable) visibilitySavable else visibility,
        enter = enter,
        exit = exit,
        content = content,
    )
}
