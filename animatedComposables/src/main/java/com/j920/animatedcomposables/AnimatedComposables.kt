package com.j920.animatedcomposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
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


enum class AnimationDirection {
    VerticalDown,
    VerticalUp,
    HorizontalDown,
    HorizontalUp
}

fun AnimationDirection.getEnterAnimation(
    durationMillis: Int = 500,
    delay: Int = 0
): EnterTransition {
    return when (this) {
        AnimationDirection.VerticalDown -> {
            slideInVertically(
                animationSpec = tween(
                    durationMillis = durationMillis / 2,
                    delayMillis = delay
                ),
                initialOffsetY = { -it / 2 }
            )
        }

        AnimationDirection.VerticalUp -> {
            slideInVertically(
                animationSpec = tween(
                    durationMillis = durationMillis / 2,
                    delayMillis = delay
                ),
                initialOffsetY = { it / 2 }
            )
        }

        AnimationDirection.HorizontalDown -> {
            slideInHorizontally(
                animationSpec = tween(
                    durationMillis = durationMillis / 2,
                    delayMillis = delay
                ),
                initialOffsetX = { -it / 2 }
            )
        }

        AnimationDirection.HorizontalUp -> {
            slideInHorizontally(
                animationSpec = tween(
                    durationMillis = durationMillis / 2,
                    delayMillis = delay
                ),
                initialOffsetX = { it / 2 }
            )
        }
    }
}

fun AnimationDirection.getExitAnimation(
    durationMillis: Int = 500,
    delay: Int = 0
): ExitTransition {
    return when (this) {
        AnimationDirection.VerticalDown -> {
            slideOutVertically(
                animationSpec = tween(
                    durationMillis = durationMillis / 2,
                    delayMillis = delay
                ),
                targetOffsetY = { -it / 2 }
            )
        }

        AnimationDirection.VerticalUp -> {
            slideOutVertically(
                animationSpec = tween(
                    durationMillis = durationMillis / 2,
                    delayMillis = delay
                ),
                targetOffsetY = { it / 2 }
            )
        }

        AnimationDirection.HorizontalDown -> {
            slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = durationMillis / 2,
                    delayMillis = delay
                ),
                targetOffsetX = { -it / 2 }
            )
        }

        AnimationDirection.HorizontalUp -> {
            slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = durationMillis / 2,
                    delayMillis = delay
                ),
                targetOffsetX = { it / 2 }
            )
        }
    }
}


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
    order: Int = 1,
    orderingDelayInMillis: Int = 0,
    durationMillis: Int = 500,
    animationDirection: AnimationDirection = AnimationDirection.VerticalUp,
    enterTransition: (order: Int, delay: Int) -> EnterTransition = { order, delay ->
        fadeIn(
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = delay * order,
            )
        ) + animationDirection.getEnterAnimation(durationMillis = durationMillis, delay = delay)
    },
    exitTransition: (order: Int, delay: Int) -> ExitTransition = { order, delay ->
        fadeOut(animationSpec = tween(delayMillis = delay * order)) + animationDirection.getExitAnimation(
            durationMillis = durationMillis,
            delay = delay
        )
    },
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = { AnimatedFab(true, floatingActionButton) },
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = {
            AnimateAlwaysEnter(
                isSavable = true,
                enter = enterTransition(order, orderingDelayInMillis),
                exit = exitTransition(order, orderingDelayInMillis)
            ) {
                content(it)
            }
        },
    )
}


@Composable
private fun AnimatedFab(
    showButton: Boolean,
    floatingActionButton: @Composable () -> Unit = {},
    order: Int = 1,
    orderingDelayInMillis: Int = 0,
    durationMillis: Int = 500,
    animationDirection: AnimationDirection = AnimationDirection.VerticalUp,
    enterTransition: (order: Int, delay: Int) -> EnterTransition = { order, delay ->
        fadeIn(
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = delay * order,
            )
        ) + animationDirection.getEnterAnimation(durationMillis = durationMillis, delay = delay)
    },
    exitTransition: (order: Int, delay: Int) -> ExitTransition = { order, delay ->
        fadeOut(animationSpec = tween(delayMillis = delay * order)) + animationDirection.getExitAnimation(
            durationMillis = durationMillis,
            delay = delay
        )
    },
) {
    AnimateAlwaysEnter(
        isSavable = true,
        condition = showButton,
        enter = enterTransition(order, orderingDelayInMillis),
        exit = exitTransition(order, orderingDelayInMillis)
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
    animationDirection: AnimationDirection = AnimationDirection.VerticalUp,
    enterTransition: (order: Int, delay: Int) -> EnterTransition = { order, delay ->
        fadeIn(
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = delay * order,
            )
        ) + animationDirection.getEnterAnimation(durationMillis = durationMillis, delay = delay)
    },
    exitTransition: (order: Int, delay: Int) -> ExitTransition = { order, delay ->
        fadeOut(animationSpec = tween(delayMillis = delay * order)) + animationDirection.getExitAnimation(
            durationMillis = durationMillis,
            delay = delay
        )
    },
    content: @Composable ColumnScope.() -> Unit
) {
    AnimateAlwaysEnter(
        isSavable = true,
        enter = enterTransition(order, orderingDelayInMillis),
        exit = exitTransition(order, orderingDelayInMillis)
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
    animationDirection: AnimationDirection = AnimationDirection.VerticalDown,
    enterTransition: (order: Int, delay: Int) -> EnterTransition = { order, delay ->
        fadeIn(
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = delay * order,
            )
        ) + animationDirection.getEnterAnimation(durationMillis = durationMillis, delay = delay)
    },
    exitTransition: (order: Int, delay: Int) -> ExitTransition = { order, delay ->
        fadeOut(animationSpec = tween(delayMillis = delay * order)) + animationDirection.getExitAnimation(
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
        enter = enterTransition(order, orderingDelayInMillis),
        exit = exitTransition(order, orderingDelayInMillis)
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
    animationDirection: AnimationDirection = AnimationDirection.VerticalUp,
    enterTransition: (order: Int, delay: Int) -> EnterTransition = { order, delay ->
        fadeIn(
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = delay * order,
            )
        ) + animationDirection.getEnterAnimation(durationMillis = durationMillis, delay = delay)
    },
    exitTransition: (order: Int, delay: Int) -> ExitTransition = { order, delay ->
        fadeOut(animationSpec = tween(delayMillis = delay * order)) + animationDirection.getExitAnimation(
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
        enter = enterTransition(order, orderingDelayInMillis),
        exit = exitTransition(order, orderingDelayInMillis)
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
    animationDirection: AnimationDirection = AnimationDirection.VerticalUp,
    enterTransition: (order: Int, delay: Int) -> EnterTransition = { order, delay ->
        fadeIn(
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = delay * order,
            )
        ) + animationDirection.getEnterAnimation(durationMillis = durationMillis, delay = delay)
    },
    exitTransition: (order: Int, delay: Int) -> ExitTransition = { order, delay ->
        fadeOut(animationSpec = tween(delayMillis = delay * order)) + animationDirection.getExitAnimation(
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
