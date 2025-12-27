package com.haotsang.wanandroidkmp.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haotsang.wanandroidkmp.htmlUnescape
import com.haotsang.wanandroidkmp.model.bean.Article

@Composable
fun ArticleItem(
    article: Article,
    onClickArticle: (String) -> Unit,
    onFavoriteClick: Article. (Boolean) -> Unit = {},
) {
    val showPinned = false
    val favoriteState by rememberSaveable(article.id) { mutableStateOf(article.collect) }

    Surface(
        onClick = { onClickArticle(article.link) },
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.large,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            Text(
//                text = "${article.superChapterName}/${article.chapterName}",
//            )

            Text(
                text = buildAnnotatedString {
                    if (showPinned) {
                        appendInlineContent("pinned", "置顶")
                    }
                    if (article.fresh) {
                        appendInlineContent("fresh", "新")
                    }

                    append(htmlUnescape(article.title))
                },
                maxLines = 2,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                inlineContent = tagMapOf(
                    showPinned = showPinned,
                    showNew = article.fresh,
                    lineHeight = LocalTextStyle.current.lineHeight
                )
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = article.shareUser.ifEmpty { article.author },
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Text(
                        text = article.niceDate.ifEmpty { article.niceShareDate },
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        modifier = Modifier.align(Alignment.Start),
                    )
                }

                IconToggleButton(
                    checked = favoriteState,
                    onCheckedChange = {
                        onFavoriteClick.invoke(article, it)
                    },
                    content = {
                        Icon(
                            imageVector = if (favoriteState) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                )
            }
        }
    }
}

private fun tagMapOf(showPinned: Boolean, showNew: Boolean, lineHeight: TextUnit) = buildMap {
    if (showPinned) {
        put("pinned", InlineTextContent(
            placeholder = Placeholder(
                width = 48.sp,
                height = lineHeight,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Bottom
            )
        ) {
            Box(
                modifier = Modifier.padding(end = 4.dp).fillMaxSize().border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(2.dp)
                ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        })
    }
    if (showNew) {
        put("fresh", InlineTextContent(
            placeholder = Placeholder(
                width = 24.sp,
                height = 24.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
            )
        ) {
            Box(
                modifier = Modifier.padding(end = 4.dp).fillMaxSize().border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(2.dp)
                ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        })
    }
}

