package com.j920.animatedcomposables.componants

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


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
