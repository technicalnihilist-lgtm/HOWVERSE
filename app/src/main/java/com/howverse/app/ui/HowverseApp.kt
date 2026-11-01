package com.howverse.app.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.howverse.app.data.ChatMessage
import com.howverse.app.data.LessonEngine
import com.howverse.app.data.Prefs
import com.howverse.app.data.Role
import com.howverse.app.data.SavedLesson
import kotlinx.coroutines.launch
import java.util.Locale

private data class LearningCategory(val title: String, val icon: ImageVector, val blurb: String)

private val categories = listOf(
    LearningCategory("Technology", Icons.Filled.PhoneAndroid, "Devices, apps and how they work"),
    LearningCategory("Vehicles", Icons.Filled.DirectionsCar, "Cars, bikes and maintenance"),
    LearningCategory("Cooking", Icons.Filled.Restaurant, "Recipes and kitchen technique"),
    LearningCategory("Coding & AI", Icons.Filled.Code, "Programming and machine learning"),
    LearningCategory("Construction", Icons.Filled.Home, "Build, repair and renovate"),
    LearningCategory("Business", Icons.Filled.Storefront, "Selling, pricing and growth"),
    LearningCategory("Money", Icons.Filled.AccountBalanceWallet, "Budgets, saving and investing"),
    LearningCategory("Science", Icons.Filled.Science, "Physics, biology and chemistry"),
    LearningCategory("Home skills", Icons.Filled.Handyman, "Plumbing, paint and tools"),
    LearningCategory("Publishing", Icons.Filled.Publish, "Apps, books and going public")
)

private val starterQuestions = listOf(
    "How does a smartphone work?",
    "How to publish an Android app?",
    "How to make vegetable biryani?",
    "How does artificial intelligence work?"
)

private const val TAB_HOME = "Home"
private const val TAB_EXPLORE = "Explore"
private const val TAB_AI = "AI"
private const val TAB_SAVED = "Saved"
private const val TAB_PROFILE = "Profile"

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun HowverseApp(prefs: Prefs) {
    val systemDark = isSystemInDarkTheme()
    var themeMode by remember { mutableStateOf(prefs.themeMode) }
    val isDark = when (themeMode) {
        "light" -> false
        "dark" -> true
        else -> systemDark
    }
    val context = LocalContext.current

    LaunchedEffect(isDark) {
        val window = context.findActivity()?.window ?: return@LaunchedEffect
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.isAppearanceLightStatusBars = !isDark
        controller.isAppearanceLightNavigationBars = !isDark
    }

    HowverseTheme(isDark = isDark) {
        HowverseScaffold(
            prefs = prefs,
            isDark = isDark,
            themeMode = themeMode,
            onThemeMode = { mode ->
                themeMode = mode
                prefs.themeMode = mode
            }
        )
    }
}

@Composable
private fun HowverseScaffold(
    prefs: Prefs,
    isDark: Boolean,
    themeMode: String,
    onThemeMode: (String) -> Unit
) {
    val p = LocalPalette.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var tab by remember { mutableStateOf(TAB_HOME) }
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Everything") }
    var loading by remember { mutableStateOf(false) }
    var backendUrl by remember { mutableStateOf(prefs.backendUrl) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val messages = remember { mutableStateListOf<ChatMessage>() }
    val saved = remember {
        mutableStateListOf<SavedLesson>().also { list -> list.addAll(prefs.loadSaved()) }
    }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) tts?.language = Locale("en", "IN")
        }
        onDispose { tts?.shutdown() }
    }

    fun speak(text: String) {
        tts?.speak(text.take(3500), TextToSpeech.QUEUE_FLUSH, null, "howverse")
    }

    fun ask(question: String, askCategory: String = category) {
        val text = question.trim()
        if (text.isEmpty() || loading) return
        category = askCategory
        query = ""
        tab = TAB_AI
        val stamp = System.currentTimeMillis()
        messages.add(ChatMessage(id = stamp, role = Role.USER, text = text))
        messages.add(ChatMessage(id = stamp + 1, role = Role.AI, text = "", pending = true))
        loading = true
        scope.launch {
            val lesson = LessonEngine.ask(text, askCategory, prefs.backendUrl)
            val index = messages.indexOfFirst { it.id == stamp + 1 }
            if (index >= 0) {
                messages[index] = ChatMessage(
                    id = stamp + 1,
                    role = Role.AI,
                    text = lesson.answer,
                    sources = lesson.sources,
                    fromAI = lesson.fromAI,
                    notice = lesson.notice
                )
            }
            loading = false
        }
    }

    fun saveMessage(message: ChatMessage) {
        if (message.text.isBlank()) return
        val question = messages.lastOrNull { it.role == Role.USER && it.id < message.id }?.text
            ?: "HOWVERSE lesson"
        if (saved.any { it.answer == message.text }) return
        saved.add(0, SavedLesson(System.currentTimeMillis(), question, message.text, System.currentTimeMillis()))
        prefs.persistSaved(saved.toList())
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(p.gradient))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .imePadding()
        ) {
            Header(
                isDark = isDark,
                onToggleTheme = { onThemeMode(if (isDark) "light" else "dark") },
                onOpenProfile = { tab = TAB_PROFILE }
            )

            when (tab) {
                TAB_HOME -> HomeScreen(
                    query = query,
                    onQueryChange = { query = it },
                    onAsk = { ask(it, category) },
                    onOpenCategory = { title -> ask("Teach me the basics of $title", title) },
                    onOpenSuggestion = { ask(it, "Everything") }
                )

                TAB_EXPLORE -> ExploreScreen(
                    onOpenCategory = { title -> ask("Teach me the basics of $title", title) },
                    onOpenSuggestion = { ask(it, "Everything") }
                )

                TAB_AI -> ChatScreen(
                    messages = messages,
                    loading = loading,
                    backendUrl = backendUrl,
                    onAsk = { ask(it) },
                    onSpeak = { speak(it) },
                    onSave = { saveMessage(it) },
                    onClear = { messages.clear() }
                )

                TAB_SAVED -> SavedScreen(
                    saved = saved,
                    onOpen = { item ->
                        val stamp = System.currentTimeMillis()
                        messages.add(ChatMessage(id = stamp, role = Role.USER, text = item.question))
                        messages.add(ChatMessage(id = stamp + 1, role = Role.AI, text = item.answer))
                        tab = TAB_AI
                    },
                    onDelete = { item ->
                        saved.removeAll { it.id == item.id }
                        prefs.persistSaved(saved.toList())
                    }
                )

                else -> ProfileScreen(
                    themeMode = themeMode,
                    onThemeMode = onThemeMode,
                    backendUrl = backendUrl,
                    onBackendUrlChange = { url ->
                        backendUrl = url
                        prefs.backendUrl = url
                    },
                    savedCount = saved.size
                )
            }

            BottomBar(tab = tab, onTab = { tab = it })
        }
    }
}

@Composable
private fun Header(isDark: Boolean, onToggleTheme: () -> Unit, onOpenProfile: () -> Unit) {
    val p = LocalPalette.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 22.dp, end = 14.dp, top = 16.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "HOWVERSE",
                fontSize = 25.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = p.text
            )
            Text("Learn anything. Understand everything.", color = p.softText, fontSize = 12.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onToggleTheme) {
                Icon(
                    if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                    contentDescription = if (isDark) "Switch to light mode" else "Switch to dark mode",
                    tint = p.accent
                )
            }
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = p.glass,
                border = BorderStroke(1.dp, p.cardBorder),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable { onOpenProfile() }
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile and settings",
                    tint = p.accent,
                    modifier = Modifier.padding(11.dp)
                )
            }
        }
    }
}

@Composable
private fun fieldColors(p: Palette): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = p.accent,
    unfocusedBorderColor = p.glassBorder,
    focusedContainerColor = p.glass,
    unfocusedContainerColor = p.glass,
    focusedTextColor = p.text,
    unfocusedTextColor = p.text,
    cursorColor = p.accent,
    focusedLeadingIconColor = p.accent,
    unfocusedLeadingIconColor = p.accent,
    focusedPlaceholderColor = p.softText,
    unfocusedPlaceholderColor = p.softText
)

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit, onAsk: (String) -> Unit, enabled: Boolean) {
    val p = LocalPalette.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("What do you want to learn?", color = p.softText) },
            leadingIcon = { Icon(Icons.Filled.Search, null, tint = p.accent) },
            shape = RoundedCornerShape(20.dp),
            colors = fieldColors(p),
            maxLines = 2
        )
        Spacer(Modifier.width(8.dp))
        FilledIconButton(
            onClick = { onAsk(query) },
            enabled = query.isNotBlank() && enabled,
            modifier = Modifier.size(54.dp)
        ) {
            Icon(Icons.Filled.ArrowForward, "Ask")
        }
    }
}

@Composable
private fun ColumnScope.HomeScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    onAsk: (String) -> Unit,
    onOpenCategory: (String) -> Unit,
    onOpenSuggestion: (String) -> Unit
) {
    val p = LocalPalette.current
    Column(Modifier.weight(1f)) {
        SearchBar(query = query, onQueryChange = onQueryChange, onAsk = onAsk, enabled = true)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Surface(
                    color = p.heroBg,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, p.heroBorder)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Your AI learning companion", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = p.text)
                            Spacer(Modifier.height(5.dp))
                            Text(
                                "Ask how to make, repair, understand or create something.",
                                color = p.softText,
                                fontSize = 13.sp
                            )
                        }
                        Icon(Icons.Filled.AutoAwesome, null, tint = p.accent, modifier = Modifier.size(38.dp))
                    }
                }
            }
            item { SectionTitle("Explore categories") }
            items(categories.chunked(2)) { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    pair.forEach { category ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onOpenCategory(category.title) },
                            color = p.glass,
                            shape = RoundedCornerShape(19.dp),
                            border = BorderStroke(1.dp, p.cardBorder)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Icon(category.icon, null, tint = p.accent, modifier = Modifier.size(25.dp))
                                Spacer(Modifier.height(12.dp))
                                Text(category.title, fontWeight = FontWeight.SemiBold, color = p.text, fontSize = 14.sp)
                            }
                        }
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
            item { SectionTitle("Popular things to learn") }
            items(starterQuestions) { suggestion ->
                Surface(
                    color = p.panel,
                    shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(1.dp, p.cardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenSuggestion(suggestion) }
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Lightbulb, null, tint = p.accent, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(suggestion, modifier = Modifier.weight(1f), fontSize = 14.sp, color = p.text)
                        Icon(Icons.Filled.ChevronRight, null, tint = p.softText, modifier = Modifier.size(22.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    val p = LocalPalette.current
    Text(text, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = p.text)
}

@Composable
private fun ColumnScope.ExploreScreen(onOpenCategory: (String) -> Unit, onOpenSuggestion: (String) -> Unit) {
    val p = LocalPalette.current
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Your learning space", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = p.text)
            Spacer(Modifier.height(6.dp))
            Text(
                "Ten categories, each with a one-tap lesson. Tap any card and HOWVERSE AI starts teaching.",
                color = p.softText,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(10.dp))
        }
        items(categories) { category ->
            Surface(
                color = p.glass,
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, p.cardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenCategory(category.title) }
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(color = p.indicator, shape = RoundedCornerShape(13.dp)) {
                        Icon(
                            category.icon,
                            null,
                            tint = p.accent,
                            modifier = Modifier.padding(9.dp).size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(category.title, fontWeight = FontWeight.SemiBold, color = p.text, fontSize = 15.sp)
                        Text(category.blurb, color = p.softText, fontSize = 12.sp)
                    }
                    Icon(Icons.Filled.ChevronRight, null, tint = p.softText, modifier = Modifier.size(22.dp))
                }
            }
        }
        item {
            Spacer(Modifier.height(6.dp))
            SectionTitle("Try one of these")
            Spacer(Modifier.height(8.dp))
        }
        items(starterQuestions) { suggestion ->
            Surface(
                color = p.panel,
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(1.dp, p.cardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenSuggestion(suggestion) }
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Lightbulb, null, tint = p.accent, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(suggestion, modifier = Modifier.weight(1f), fontSize = 14.sp, color = p.text)
                    Icon(Icons.Filled.ChevronRight, null, tint = p.softText, modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.ChatScreen(
    messages: List<ChatMessage>,
    loading: Boolean,
    backendUrl: String,
    onAsk: (String) -> Unit,
    onSpeak: (String) -> Unit,
    onSave: (ChatMessage) -> Unit,
    onClear: () -> Unit
) {
    val p = LocalPalette.current
    val listState = rememberLazyListState()
    var input by remember { mutableStateOf("") }

    LaunchedEffect(messages.size, messages.lastOrNull()?.pending) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(Modifier.weight(1f)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 10.dp, top = 4.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.AutoAwesome, null, tint = p.accent, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("HOWVERSE AI", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = p.text)
            Spacer(Modifier.width(8.dp))
            StatusChip(live = backendUrl.isNotBlank())
            Spacer(Modifier.weight(1f))
            if (messages.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Filled.Delete, "Clear conversation", tint = p.softText, modifier = Modifier.size(20.dp))
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Surface(
                        color = p.glass,
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(1.dp, p.cardBorder)
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            Text("Hi, I'm HOWVERSE AI", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = p.text)
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Ask me how to make, repair, understand or create anything. " +
                                    "I answer in plain language, step by step.",
                                color = p.softText,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
                item {
                    Column(Modifier.padding(top = 4.dp)) {
                        Text("Try asking", color = p.softText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        starterQuestions.forEach { suggestion ->
                            Surface(
                                color = p.panel,
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, p.cardBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .clickable { onAsk(suggestion) }
                            ) {
                                Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Lightbulb, null, tint = p.accent, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(9.dp))
                                    Text(suggestion, fontSize = 13.sp, color = p.text)
                                }
                            }
                        }
                    }
                }
            }

            items(messages, key = { it.id }) { message ->
                if (message.role == Role.USER) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Surface(
                            color = p.bubbleUser,
                            shape = RoundedCornerShape(18.dp, 18.dp, 6.dp, 18.dp),
                            modifier = Modifier.widthIn(max = 300.dp)
                        ) {
                            Text(
                                message.text,
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 11.dp),
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = p.onBubbleUser
                            )
                        }
                    }
                } else {
                    AiBubble(message = message, onSpeak = onSpeak, onSave = onSave)
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask HOWVERSE anything…", color = p.softText, fontSize = 14.sp) },
                shape = RoundedCornerShape(20.dp),
                colors = fieldColors(p),
                maxLines = 3
            )
            Spacer(Modifier.width(8.dp))
            FilledIconButton(
                onClick = {
                    val text = input
                    input = ""
                    onAsk(text)
                },
                enabled = input.isNotBlank() && !loading,
                modifier = Modifier.size(52.dp)
            ) {
                Icon(Icons.Filled.Send, "Send")
            }
        }
    }
}

@Composable
private fun StatusChip(live: Boolean) {
    val p = LocalPalette.current
    Surface(
        color = if (live) p.indicator else p.panel,
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(1.dp, if (live) p.accent.copy(alpha = 0.5f) else p.cardBorder)
    ) {
        Row(
            Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!live) {
                Icon(Icons.Filled.WifiOff, null, tint = p.softText, modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
            }
            Text(
                if (live) "Live AI" else "Offline",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = if (live) p.accent else p.softText
            )
        }
    }
}

@Composable
private fun AiBubble(message: ChatMessage, onSpeak: (String) -> Unit, onSave: (ChatMessage) -> Unit) {
    val p = LocalPalette.current
    val clipboard = LocalClipboardManager.current
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Surface(
            color = p.bubbleAi,
            shape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 6.dp),
            border = BorderStroke(1.dp, p.cardBorder),
            modifier = Modifier.widthIn(max = 330.dp)
        ) {
            Column(Modifier.padding(15.dp)) {
                if (message.pending) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(15.dp),
                            strokeWidth = 2.dp,
                            color = p.accent
                        )
                        Spacer(Modifier.width(10.dp))
                        Text("Thinking…", color = p.softText, fontSize = 13.sp)
                    }
                } else {
                    Text(
                        message.text,
                        color = p.text,
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                    if (message.sources.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        Text("Web sources used", fontWeight = FontWeight.Bold, color = p.accent, fontSize = 12.sp)
                        message.sources.forEach { source ->
                            Text("• $source", color = p.softText, fontSize = 11.sp, lineHeight = 16.sp)
                        }
                    }
                    if (message.notice.isNotBlank()) {
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Filled.WifiOff,
                                null,
                                tint = p.softText,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(message.notice, color = p.softText, fontSize = 11.sp, lineHeight = 15.sp)
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onSpeak(message.text) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.VolumeUp, "Read aloud", tint = p.accent, modifier = Modifier.size(17.dp))
                        }
                        IconButton(onClick = { onSave(message) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.BookmarkAdd, "Save lesson", tint = p.accent, modifier = Modifier.size(17.dp))
                        }
                        IconButton(
                            onClick = { clipboard.setText(AnnotatedString(message.text)) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Filled.ContentCopy, "Copy", tint = p.softText, modifier = Modifier.size(16.dp))
                        }
                        Spacer(Modifier.weight(1f))
                        Text(
                            if (message.fromAI) "AI" else "Offline",
                            fontSize = 10.sp,
                            color = p.softText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.SavedScreen(saved: List<SavedLesson>, onOpen: (SavedLesson) -> Unit, onDelete: (SavedLesson) -> Unit) {
    val p = LocalPalette.current
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Saved lessons", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = p.text)
            Text(
                if (saved.isEmpty()) "Nothing saved yet." else "${saved.size} lesson(s) kept on this device.",
                color = p.softText,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(Modifier.height(8.dp))
        }
        if (saved.isEmpty()) {
            item {
                Surface(
                    color = p.glass,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, p.cardBorder)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text("Your saved lessons will appear here.", color = p.text, fontSize = 14.sp)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Ask something in the AI tab and tap the bookmark icon on any answer. " +
                                "Saved lessons now survive an app restart.",
                            color = p.softText,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }
        items(saved, key = { it.id }) { item ->
            Surface(
                color = p.panel,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, p.cardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpen(item) }
            ) {
                Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(item.question, color = p.text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(3.dp))
                        Text(
                            item.answer.take(80).replace("\n", " ") + "…",
                            color = p.softText,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                    IconButton(onClick = { onDelete(item) }) {
                        Icon(Icons.Filled.Delete, "Delete", tint = p.softText, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.ProfileScreen(
    themeMode: String,
    onThemeMode: (String) -> Unit,
    backendUrl: String,
    onBackendUrlChange: (String) -> Unit,
    savedCount: Int
) {
    val p = LocalPalette.current
    var draft by remember { mutableStateOf(backendUrl) }
    var savedFlag by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = p.text)
            Text("Theme, AI backend and app details.", color = p.softText, fontSize = 12.sp)
        }

        item {
            SettingsCard(title = "Appearance", subtitle = "Choose how HOWVERSE looks.") {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ThemeOption("System", "system", themeMode, onThemeMode, Icons.Filled.Settings)
                    ThemeOption("Light", "light", themeMode, onThemeMode, Icons.Filled.LightMode)
                    ThemeOption("Dark", "dark", themeMode, onThemeMode, Icons.Filled.DarkMode)
                }
            }
        }

        item {
            SettingsCard(
                title = "AI backend",
                subtitle = "HOWVERSE uses your own AI server. Leave it empty to use the built-in offline library."
            ) {
                OutlinedTextField(
                    value = draft,
                    onValueChange = {
                        draft = it
                        savedFlag = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("https://your-ai-server.example", color = p.softText, fontSize = 13.sp) },
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors(p),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = p.accent,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable {
                            onBackendUrlChange(draft.trim())
                            savedFlag = true
                        }
                    ) {
                        Text(
                            "Save URL",
                            color = p.onAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        when {
                            savedFlag -> "Saved"
                            backendUrl.isBlank() -> "Offline mode"
                            else -> "Connected: ${backendUrl.removePrefix("https://").removePrefix("http://")}"
                        },
                        color = if (savedFlag) p.good else p.softText,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "The app calls POST <url>/api/learn with {question, category, language}. " +
                        "Keep your provider key on the server — never in the app.",
                    color = p.softText,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        item {
            SettingsCard(title = "About", subtitle = "HOWVERSE 1.1") {
                Text(
                    "Kotlin + Jetpack Compose. $savedCount saved lesson(s) on this device. " +
                        "Offline answers come from a hand-written library of guides; connect an AI " +
                        "backend for live, model-generated lessons.",
                    color = p.softText,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun ThemeOption(
    label: String,
    value: String,
    selected: String,
    onSelect: (String) -> Unit,
    icon: ImageVector
) {
    val p = LocalPalette.current
    val active = value == selected
    Surface(
        color = if (active) p.indicator else p.glass,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, if (active) p.accent.copy(alpha = 0.6f) else p.cardBorder),
        modifier = Modifier.clickable { onSelect(value) }
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = if (active) p.accent else p.softText, modifier = Modifier.size(17.dp))
            Spacer(Modifier.width(7.dp))
            Text(
                label,
                fontSize = 13.sp,
                fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
                color = if (active) p.accent else p.text
            )
        }
    }
}

@Composable
private fun SettingsCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    val p = LocalPalette.current
    Surface(
        color = p.glass,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, p.cardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = p.text)
            Spacer(Modifier.height(3.dp))
            Text(subtitle, color = p.softText, fontSize = 11.sp, lineHeight = 16.sp)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun BottomBar(tab: String, onTab: (String) -> Unit) {
    val p = LocalPalette.current
    NavigationBar(containerColor = p.navBg) {
        listOf(
            TAB_HOME to Icons.Filled.Home,
            TAB_EXPLORE to Icons.Filled.Explore,
            TAB_AI to Icons.Filled.AutoAwesome,
            TAB_SAVED to Icons.Filled.Bookmarks,
            TAB_PROFILE to Icons.Filled.Person
        ).forEach { (name, icon) ->
            NavigationBarItem(
                selected = tab == name,
                onClick = { onTab(name) },
                icon = { Icon(icon, null) },
                label = { Text(name) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = p.accent,
                    selectedTextColor = p.accent,
                    unselectedIconColor = p.softText,
                    unselectedTextColor = p.softText,
                    indicatorColor = p.indicator
                )
            )
        }
    }
}
