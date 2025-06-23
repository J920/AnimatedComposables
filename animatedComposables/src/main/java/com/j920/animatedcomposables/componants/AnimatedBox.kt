package com.j920.animatedcomposables.componants

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.j920.animatedcomposables.models.AnimationSpec
import com.j920.animatedcomposables.utils.getEnterAnimation
import com.j920.animatedcomposables.utils.getExitAnimation


@Composable
fun AnimatedBox(
    modifier: Modifier = Modifier,
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
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    AnimateAlwaysEnter(
        isSavable = true,
        enter = enterTransition(delayInMillis),
        exit = exitTransition(delayInMillis)
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
