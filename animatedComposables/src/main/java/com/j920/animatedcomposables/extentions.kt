package com.j920.animatedcomposables

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


fun defaultAnimation(): ContentTransform {
    return (FadeInWithScale())
        .togetherWith(fadeOut(animationSpec = tween(250)))
}

fun fadeAnimation(): ContentTransform {
    return (fadeIn(tween(250)))
        .togetherWith(fadeOut(animationSpec = tween(250)))
}

fun slideAnimation(): ContentTransform {
    return (
            slideInVertically(initialOffsetY = { it - 60 }, animationSpec = tween(600))
                .togetherWith(
                    slideOutVertically(
                        targetOffsetY = { it - 60 },
                        animationSpec = tween(600),
                    ),
                )
            )
}

fun FadeInWithScale(
    durationMillis: Int = AnimationConstants.DefaultDurationMillis,
    delayMillis: Int = 0,
): EnterTransition {
    return fadeIn(animationSpec = tween(durationMillis, delayMillis = delayMillis)) +
            scaleIn(
                initialScale = 1.10f,
                animationSpec = tween(durationMillis, delayMillis = delayMillis),
            )
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentTransitionScope<Boolean>.expandAnimation(): ContentTransform {
    return fadeIn(animationSpec = tween(150, 150)) togetherWith
            fadeOut(animationSpec = tween(150)) using
            SizeTransform { initialSize, targetSize ->
                if (targetState) {
                    keyframes {
                        // Expand horizontally first.
                        IntSize(targetSize.width, initialSize.height) at 150
                        durationMillis = 300
                    }
                } else {
                    keyframes {
                        // Shrink vertically first.
                        IntSize(initialSize.width, targetSize.height) at 150
                        durationMillis = 300
                    }
                }
            }
}

fun slideEnterHorizontalAnimation(
    density: Density,
    expandFrom: Alignment.Horizontal = Alignment.End,
): EnterTransition {
    return slideInHorizontally {
        // Slide in from 40 dp from the End.
        with(density) { -40.dp.roundToPx() }
    } + expandHorizontally(
        // Expand from the End.
        expandFrom = expandFrom,
    ) + fadeIn(
        // Fade in with the initial alpha of 0.3f.
        initialAlpha = 0.3f,
    )
}

fun slideExitHorizontalAnimation(): ExitTransition {
    return slideOutHorizontally() + shrinkHorizontally() + fadeOut()
}

fun slideEnterVerticalAnimation(
    density: Density,
    expandFrom: Alignment.Vertical = Alignment.Top,
    animationSpec: FiniteAnimationSpec<IntOffset> =
        spring(
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = IntOffset.VisibilityThreshold,
        ),
): EnterTransition {
    return slideInVertically(animationSpec = animationSpec) {
        with(density) { -40.dp.roundToPx() }
    } + expandVertically(
        expandFrom = expandFrom,
    ) + fadeIn(
        initialAlpha = 0.3f,
    )
}