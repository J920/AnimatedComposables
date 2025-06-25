package com.j920.animatedcomposables.componants

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.j920.animatedcomposables.models.UiState
import com.j920.animatedcomposables.utils.fadeAnimation
import com.j920.animatedcomposables.utils.pagingLoadStateItem
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> AnimatedLazyPullToRefresh(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    isRefreshing: MutableState<Boolean> = mutableStateOf(false),
    uiState: UiState<Flow<PagingData<T>>> = UiState.Initial,
    onRefresh: () -> Unit = {},
    contentAnimation: ContentTransform = fadeIn(tween(350)).togetherWith(fadeOut(tween(350))),
    emptyContentAnimation: ContentTransform = fadeIn(tween(delayMillis = 350)).togetherWith(fadeOut()),
    state: PullToRefreshState = rememberPullToRefreshState(),
    indicator: @Composable BoxScope.() -> Unit = {
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing.value,
            state = state
        )
    },
    initialContent: @Composable () -> Unit = { CircularProgressIndicator() },
    prependContent: @Composable () -> Unit = { CircularProgressIndicator() },
    prependError: @Composable () -> Unit = {},
    loadingNextContent: @Composable () -> Unit = {
        CircularProgressIndicator(
            modifier = Modifier.size(
                18.dp
            )
        )
    },
    loadingNextError: @Composable () -> Unit = {},
    emptyContent: @Composable () -> Unit = {},
    header: @Composable AnimatedVisibilityScope.() -> Unit = {},
    content: LazyListScope.(LazyPagingItems<T>) -> Unit,
) {

    PullToRefreshBox(
        modifier = modifier,
        state = state,
        isRefreshing = isRefreshing.value,
        onRefresh = onRefresh,
        indicator = indicator
    ) {
        StatefulContainer(
            uiState = uiState,
            modifier = Modifier.fillMaxSize(),
            initialContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    initialContent()
                }
            },
            loadingContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    initialContent()
                }
            },
        ) {
            val paging = it.data?.collectAsLazyPagingItems()

            isRefreshing.value = paging?.loadState?.refresh is LoadState.Loading
            paging?.let {
                Column {
                    AnimatedVisibility(
                        paging.itemCount == 0 && isRefreshing.value.not() && paging.loadState.append is LoadState.NotLoading,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .animateEnterExit(
                                    enter = emptyContentAnimation.targetContentEnter,
                                    exit = emptyContentAnimation.initialContentExit
                                ), contentAlignment = Alignment.Center
                        ) {
                            emptyContent()
                        }
                    }
                }
                AnimatedContent(
                    paging.itemCount,
                    transitionSpec = {
                        fadeAnimation()
                    },
                ) {

                    if (it > 0) {
                        Column {
                            header()

                            LazyColumn(
                                modifier = listModifier
                                    .animateContentSize()
                                    .animateEnterExit(
                                        enter = contentAnimation.targetContentEnter,
                                        exit = contentAnimation.initialContentExit,
                                    ),
                                verticalArrangement = Arrangement.spacedBy(0.dp),
                                contentPadding = PaddingValues(bottom = 26.dp),
                            ) {
                                pagingLoadStateItem(
                                    loadState = paging.loadState.prepend,
                                    keySuffix = "prepend",
                                    loading = {
                                        Box(
                                            Modifier
                                                .animateItem()
                                                .padding(top = 20.dp)
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            prependContent()
                                        }
                                    },
                                    error = {
                                        prependError()
                                    },
                                )

                                content(paging)

                                pagingLoadStateItem(
                                    loadState = paging.loadState.append,
                                    keySuffix = "append",
                                    loading = {
                                        Box(
                                            Modifier
                                                .animateItem()
                                                .padding(top = 20.dp)
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            loadingNextContent()
                                        }
                                    },
                                    error = { loadingNextError() },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}