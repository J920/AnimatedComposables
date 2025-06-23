package com.j920.animatedcomposables.componants

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import com.j920.animatedcomposables.models.AnimationSpec
import com.j920.animatedcomposables.utils.getEnterAnimation
import com.j920.animatedcomposables.utils.getExitAnimation


@Composable
fun LazyListScope.AnimatedItem(
    key: Any? = null,
    contentType: Any? = null,
    order: Int = 1,
    delayInMillis: Int = 0,
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
        enter = enterTransition(order, delayInMillis),
        exit = exitTransition(order, delayInMillis)
    ) {
        item(
            key = key,
            contentType = contentType,
            content = content
        )
    }
}
