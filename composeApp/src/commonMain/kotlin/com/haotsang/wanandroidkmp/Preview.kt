package com.haotsang.wanandroidkmp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import wanandroidkmp.composeapp.generated.resources.Res
import wanandroidkmp.composeapp.generated.resources.compose_multiplatform

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HorizontalCenteredHeroCarouselSample() {
    data class CarouselItem(
        val id: Int,

    )
    val items =
        listOf(
            CarouselItem(0),
            CarouselItem(1),
            CarouselItem(2),
            CarouselItem(3),
            CarouselItem(4),
        )
    val state = rememberCarouselState { items.count() }
    val animationScope = rememberCoroutineScope()
    HorizontalCenteredHeroCarousel(
        state = state,
        modifier = Modifier.fillMaxWidth().height(221.dp).padding(horizontal = 24.dp),
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) { i ->
        val item = items[i]
        Image(
            modifier =
                Modifier.fillMaxWidth()
                    .height(205.dp)
                    .maskClip(MaterialTheme.shapes.extraLarge)
                    .clickable(true, "Tap to focus", Role.Image) {
                        animationScope.launch { state.animateScrollToItem(i) }
                    },
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )
    }
}
