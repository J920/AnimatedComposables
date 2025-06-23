package com.j920.animatedcomposables.componants

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.j920.animatedcomposables.models.AnimationSpec
import com.j920.animatedcomposables.utils.getEnterAnimation
import com.j920.animatedcomposables.utils.getExitAnimation


@Composable
fun AnimatedRow(
    delayInMillis: Int = 0,
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
        enter = enterTransition(delayInMillis),
        exit = exitTransition(delayInMillis)
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            content = content
        )
    }
}

