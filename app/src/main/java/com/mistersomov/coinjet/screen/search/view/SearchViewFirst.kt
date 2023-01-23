package com.mistersomov.coinjet.screen.search.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mistersomov.coinjet.R
import com.mistersomov.coinjet.core_ui.CoinJetTheme
import com.mistersomov.coinjet.core_ui.MainTheme

@Composable
fun SearchViewFirst() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.crypto_search_headline_title),
            color = CoinJetTheme.colors.onSurface,
            style = CoinJetTheme.typography.titleMedium
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            color = CoinJetTheme.colors.surfaceVariant
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.first_search),
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.crypto_search_headline_title_first_search),
            color = CoinJetTheme.colors.onSurface,
            style = CoinJetTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true, name = "First")
@Composable
fun PreviewFirstSearch() {
    MainTheme {
        SearchViewFirst()
    }
}