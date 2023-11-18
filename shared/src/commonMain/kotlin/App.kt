import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun App() {
    MaterialTheme {
        val coroutineScopes = rememberCoroutineScope()
        var requestState: RequestState by remember {
            mutableStateOf(RequestState.Idle)
        }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                coroutineScopes.launch {
                    ProductsApi().fetchProducts(limit = 10).collectLatest {
                        requestState = it
                    }
                }
            }) {
                Text("Fetch Data")
            }
            when (requestState) {
                is RequestState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }

                is RequestState.Success -> {
                    Card {
                        LazyColumn(
                            modifier = Modifier
                                .background(MaterialTheme.colors.surface)
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(
                                count = requestState.getProducts().items.count()
                            ) {
                                Column {
                                    Text(requestState.getProducts().items[it].title)
                                    Text(requestState.getProducts().items[it].category)
                                    ProductImage(requestState.getProducts().items[it].image)
                                }
                            }
                        }
                    }
                }

                is RequestState.Error -> {
                    Text("Error")
                }

                else -> {
                    Text("Nothing")
                }
            }

        }
    }
}

@Composable
fun ProductImage(image: String) {
    KamelImage(
        asyncPainterResource(image),
        "image",
        Modifier.fillMaxWidth()
            .height(100.dp)

    )
}

expect fun getPlatformName(): String