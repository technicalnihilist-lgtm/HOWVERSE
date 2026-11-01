package com.howverse.app

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.Locale

// For Android emulator use http://10.0.2.2:3000/.
// For a real phone, use your deployed HTTPS backend URL.
private const val API_BASE_URL = "https://YOUR-BACKEND-DOMAIN.example"

private val Ink = Color(0xFF080B13)
private val Panel = Color(0xFF171D2B)
private val Glass = Color(0xCC252E40)
private val Accent = Color(0xFF9DBBFF)
private val SoftText = Color(0xFFB8C2D6)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HowverseApp() }
    }
}

data class Category(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val categories = listOf(
    Category("Technology", Icons.Default.PhoneAndroid),
    Category("Vehicles", Icons.Default.DirectionsCar),
    Category("Cooking", Icons.Default.Restaurant),
    Category("Coding & AI", Icons.Default.Code),
    Category("Construction", Icons.Default.Home),
    Category("Business", Icons.Default.Storefront),
    Category("Money", Icons.Default.AccountBalanceWallet),
    Category("Science", Icons.Default.Science),
    Category("Home skills", Icons.Default.Handyman),
    Category("Publishing", Icons.Default.Publish)
)

@Composable
fun HowverseApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Everything") }
    var answer by remember { mutableStateOf("") }
    var sources by remember { mutableStateOf(listOf<String>()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var tab by remember { mutableStateOf("Home") }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) tts?.language = Locale("en", "IN")
        }
        onDispose { tts?.shutdown() }
    }

    fun ask(question: String) {
        if (question.isBlank() || loading) return
        query = question
        loading = true
        error = ""
        answer = ""
        sources = emptyList()
        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val client = OkHttpClient()
                    val payload = JSONObject()
                        .put("question", question)
                        .put("category", selectedCategory)
                        .put("language", "simple English; use Hindi if the user asks in Hindi")
                    val body = payload.toString()
                        .toRequestBody("application/json; charset=utf-8".toMediaType())
                    val request = Request.Builder()
                        .url("$API_BASE_URL/api/learn")
                        .post(body)
                        .build()
                    client.newCall(request).execute().use { response ->
                        val raw = response.body?.string().orEmpty()
                        if (!response.isSuccessful) {
                            throw IllegalStateException("Server error ${response.code}: $raw")
                        }
                        JSONObject(raw)
                    }
                }
                answer = result.optString("answer", "No answer returned.")
                val arr = result.optJSONArray("sources")
                sources = if (arr == null) emptyList() else
                    (0 until arr.length()).map { arr.optJSONObject(it)?.optString("title") ?: "" }
                        .filter { it.isNotBlank() }
            } catch (e: Exception) {
                error = "Couldn't reach HOWVERSE AI. Check your internet and backend URL. ${e.message ?: ""}"
            } finally {
                loading = false
            }
        }
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Ink, surface = Panel, primary = Accent,
            onBackground = Color.White, onSurface = Color.White
        )
    ) {
        Box(
            Modifier.fillMaxSize().background(
                Brush.linearGradient(listOf(Color(0xFF111A2B), Ink, Color(0xFF171326)))
            )
        ) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("HOWVERSE", fontSize = 25.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                        Text("Learn anything. Understand everything.", color = SoftText, fontSize = 12.sp)
                    }
                    Surface(shape = RoundedCornerShape(18.dp), color = Glass) {
                        Icon(Icons.Default.Person, null, tint = Accent, modifier = Modifier.padding(12.dp))
                    }
                }

                Row(Modifier.padding(horizontal = 18.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("What do you want to learn?", color = SoftText) },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = Accent) },
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Accent, unfocusedBorderColor = Color(0xFF46516A),
                            focusedContainerColor = Glass, unfocusedContainerColor = Glass
                        ),
                        maxLines = 2
                    )
                    Spacer(Modifier.width(8.dp))
                    FilledIconButton(
                        onClick = { ask(query) },
                        enabled = query.isNotBlank() && !loading,
                        modifier = Modifier.size(54.dp)
                    ) { Icon(Icons.Default.ArrowForward, "Search") }
                }

                if (tab == "Home") {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 20.dp, bottom = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Surface(color = Color(0xAA283957), shape = RoundedCornerShape(24.dp)) {
                                Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text("Your AI learning companion", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Spacer(Modifier.height(5.dp))
                                        Text("Ask how to make, repair, understand or create something.", color = SoftText, fontSize = 13.sp)
                                    }
                                    Icon(Icons.Default.AutoAwesome, null, tint = Accent, modifier = Modifier.size(38.dp))
                                }
                            }
                        }
                        item { Text("Explore categories", fontSize = 19.sp, fontWeight = FontWeight.Bold) }
                        items(categories.chunked(2)) { pair ->
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                pair.forEach { category ->
                                    Surface(
                                        modifier = Modifier.weight(1f).clickable {
                                            selectedCategory = category.title
                                            query = "Teach me the basics of ${category.title}"
                                            ask(query)
                                        },
                                        color = Glass, shape = RoundedCornerShape(19.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x334F6388))
                                    ) {
                                        Column(Modifier.padding(16.dp)) {
                                            Icon(category.icon, null, tint = Accent, modifier = Modifier.size(25.dp))
                                            Spacer(Modifier.height(12.dp))
                                            Text(category.title, fontWeight = FontWeight.SemiBold)
                                        }
                                    }
                                }
                                if (pair.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                        item {
                            Text("Popular things to learn", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            listOf("How does a smartphone work?", "How to publish an Android app?", "How to make vegetable biryani?", "How does artificial intelligence work?").forEach { suggestion ->
                                Surface(
                                    color = Panel, shape = RoundedCornerShape(15.dp),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                        .clickable { selectedCategory = "Everything"; ask(suggestion) }
                                ) {
                                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Lightbulb, null, tint = Accent)
                                        Spacer(Modifier.width(10.dp))
                                        Text(suggestion, modifier = Modifier.weight(1f), fontSize = 14.sp)
                                        Icon(Icons.Default.ChevronRight, null, tint = SoftText)
                                    }
                                }
                            }
                        }
                    }
                } else if (tab == "Saved") {
                    LazyColumn(Modifier.weight(1f).padding(18.dp)) {
                        item { Text("Saved lessons", fontSize = 22.sp, fontWeight = FontWeight.Bold) }
                        if (saved.isEmpty()) item { Text("Your saved lessons will appear here.", color = SoftText, modifier = Modifier.padding(top = 20.dp)) }
                        items(saved) { item ->
                            Surface(color = Panel, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(top = 10.dp).clickable { query = item.first; answer = item.second; tab = "Home" }) {
                                Text(item.first, modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                } else {
                    Column(Modifier.weight(1f).padding(22.dp)) {
                        Text("Your learning space", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Search for any topic to start a lesson. Your recent learning activity will be added here in a future update.", color = SoftText, modifier = Modifier.padding(top = 12.dp))
                    }
                }

                if (answer.isNotBlank() || loading || error.isNotBlank()) {
                    Surface(color = Color(0xF21A2232), shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(18.dp).heightIn(max = 430.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, null, tint = Accent)
                                Spacer(Modifier.width(8.dp))
                                Text("HOWVERSE AI LESSON", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                if (answer.isNotBlank()) {
                                    IconButton(onClick = { saved = saved + (query to answer) }) { Icon(Icons.Default.BookmarkAdd, "Save lesson", tint = Accent) }
                                    IconButton(onClick = { tts?.speak(answer.take(3500), TextToSpeech.QUEUE_FLUSH, null, "howverse") }) { Icon(Icons.Default.VolumeUp, "Read aloud", tint = Accent) }
                                }
                                IconButton(onClick = { answer = ""; error = ""; sources = emptyList() }) { Icon(Icons.Default.Close, "Close") }
                            }
                            if (loading) LinearProgressIndicator(Modifier.fillMaxWidth(), color = Accent)
                            if (error.isNotBlank()) Text(error, color = Color(0xFFFFB4AB))
                            if (answer.isNotBlank()) {
                                LazyColumn(Modifier.weight(1f, fill = false)) {
                                    item { Text(answer, color = Color(0xFFE6EAF2), lineHeight = 22.sp) }
                                    if (sources.isNotEmpty()) item {
                                        Spacer(Modifier.height(12.dp))
                                        Text("Web sources used", fontWeight = FontWeight.Bold, color = Accent)
                                        sources.forEach { Text("• $it", color = SoftText, fontSize = 12.sp) }
                                    }
                                }
                            }
                        }
                    }
                }

                NavigationBar(containerColor = Color(0xEE101725)) {
                    listOf("Home" to Icons.Default.Home, "Explore" to Icons.Default.Explore, "AI" to Icons.Default.AutoAwesome, "Saved" to Icons.Default.Bookmarks, "Profile" to Icons.Default.Person).forEach { (name, icon) ->
                        NavigationBarItem(
                            selected = tab == name,
                            onClick = {
                                tab = name
                                if (name == "AI") { selectedCategory = "Everything"; query = "Help me choose something useful to learn"; ask(query) }
                            },
                            icon = { Icon(icon, null) }, label = { Text(name) },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = Accent, indicatorColor = Color(0xFF293954))
                        )
                    }
                }
            }
        }
    }
}
