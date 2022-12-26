package com.mistersomov.coinjet.screen.coin.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mistersomov.coinjet.core_ui.Padding
import com.mistersomov.coinjet.screen.coin.component.ListItem
import com.mistersomov.coinjet.screen.coin.component.ShimmerEffect

@Composable
fun CoinViewLoading(
    padding: Padding = Padding(start = 6, top = 6, end = 6),
) {
    LazyColumn {
        items(10) {
            ListItem(modifier = Modifier.padding(
                start = padding.start.dp,
                top = padding.top.dp,
                end = padding.end.dp,
                bottom = padding.bottom.dp
            ),
                isLoading = true,
                content = {
                    LoadingFields()
                }) { }
        }
    }
}

@Composable
fun LoadingFields() {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
    ) {
        ShimmerEffect(containerHeight = 32.dp)
    }
    Row(
        modifier = Modifier
            .padding(start = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.padding(top = 14.dp, bottom = 6.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                ShimmerEffect(containerHeight = 32.dp)
            }
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .width(70.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                ShimmerEffect(containerHeight = 32.dp)
            }
        }
        Column(
            modifier = Modifier.padding(top = 14.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .width(134.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                ShimmerEffect(containerHeight = 32.dp)
            }
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .width(134.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                ShimmerEffect(containerHeight = 32.dp)
            }
        }
    }
}