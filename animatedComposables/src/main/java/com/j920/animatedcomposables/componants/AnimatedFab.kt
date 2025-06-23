package com.j920.animatedcomposables.componants

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import com.j920.animatedcomposables.models.AnimationSpec
import com.j920.animatedcomposables.utils.getEnterAnimation
import com.j920.animatedcomposables.utils.getExitAnimation

@Composable
fun AnimatedFab(
    showButton: Boolean,
    floatingActionButton: @Composable () -> Unit = {},
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
) {
    AnimateAlwaysEnter(
        isSavable = true,
        condition = showButton,
        enter = enterTransition(delayInMillis),
        exit = exitTransition(delayInMillis)
    ) {
        floatingActionButton()
    }
}
