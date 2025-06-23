package com.j920.animatedcomposables.componants

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.j920.animatedcomposables.models.AnimationSpec
import com.j920.animatedcomposables.utils.getEnterAnimation
import com.j920.animatedcomposables.utils.getExitAnimation


@Composable
fun AnimatedColumn(
    modifier: Modifier = Modifier,
    delayInMillis: Int = 0,
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
        enter = enterTransition(delayInMillis),
        exit = exitTransition(delayInMillis)
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }
}
