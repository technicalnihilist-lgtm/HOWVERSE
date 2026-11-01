package com.howverse.app.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.BitmapFactory
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.howverse.app.VideoActivity
import com.howverse.app.data.Catalogue
import com.howverse.app.data.ChatMessage
import com.howverse.app.data.Downloader
import com.howverse.app.data.Language
import com.howverse.app.data.Languages
import com.howverse.app.data.LessonEngine
import com.howverse.app.data.Prefs
import com.howverse.app.data.Role
import com.howverse.app.data.SavedLesson
import com.howverse.app.data.Section
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

private sealed interface Route {
    data object Home : Route
    data object Catalogue : Route
    data object Chat : Route
    data object Saved : Route
    data object Profile : Route
    data object Downloads : Route
    data class Section(val id: String) : Route
}

private val starterQuestions = listOf(
    "How does a smartphone work?",
    "How to publish an Android app?",
    "How to make vegetable biryani?",
    "How does artificial intelligence work?"
)

private val artCache = HashMap<String, ImageBitmap?>()

@Composable
private fun sectionArt(path: String): ImageBitmap? {
    val context = LocalContext.current
    return remember(path) {
        artCache.getOrPut(path) {
            runCatching {
                context.assets.open(path).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
            }.getOrNull()
        }
    }
}

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

private fun openVideos(context: Context, query: String) {
    context.startActivity(
        Intent(context, VideoActivity::class.java).putExtra(VideoActivity.EXTRA_QUERY, query)
    )
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

    var route by remember { mutableStateOf<Route>(Route.Home) }
    var query by remember { mutableStateOf("") }
    var catalogueQuery by remember { mutableStateOf("") }
    var languageCode by remember { mutableStateOf(prefs.languageCode) }
    val language = Languages.byCode(languageCode)
    var backendUrl by remember { mutableStateOf(prefs.backendUrl) }
    var packUrl by remember { mutableStateOf(prefs.packUrl) }
    var voiceNote by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var packs by remember { mutableStateOf(Downloader.listPacks(context)) }
    var downloadStatus by remember { mutableStateOf("") }
    var packBusy by remember { mutableStateOf(false) }

    val messages = remember { mutableStateListOf<ChatMessage>() }
    val saved = remember {
        mutableStateListOf<SavedLesson>().also { list -> list.addAll(prefs.loadSaved()) }
    }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) tts?.language = Locale.forLanguageTag(language.tag)
        }
        onDispose { tts?.shutdown() }
    }

    LaunchedEffect(languageCode) {
        val engine = tts ?: return@LaunchedEffect
        val result = engine.setLanguage(Locale.forLanguageTag(language.tag))
        voiceNote = if (result == TextToSpeech.LANG_MISSING_DATA ||
            result == TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            "No offline voice for ${language.label} on this device — install it in Android " +
                "Settings › Text-to-speech, or read the lesson in English."
        } else ""
    }

    fun speak(text: String) {
        val engine = tts ?: return
        engine.setLanguage(Locale.forLanguageTag(language.tag))
        engine.speak(text.take(3500), TextToSpeech.QUEUE_FLUSH, null, "howverse")
    }

    fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun ask(question: String, askCategory: String = "Everything") {
        val text = question.trim()
        if (text.isEmpty() || loading) return
        query = ""
        route = Route.Chat
        val stamp = System.currentTimeMillis()
        messages.add(ChatMessage(id = stamp, role = Role.USER, text = text))
        messages.add(ChatMessage(id = stamp + 1, role = Role.AI, text = "", pending = true))
        loading = true
        scope.launch {
            val lesson = LessonEngine.ask(text, askCategory, prefs.backendUrl, language.label)
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
        toast("Lesson saved")
    }

    fun downloadMessage(message: ChatMessage) {
        val question = messages.lastOrNull { it.role == Role.USER && it.id < message.id }?.text
            ?: "HOWVERSE lesson"
        val body = buildString {
            appendLine("HOWVERSE — $question")
            appendLine("Language: ${language.label}")
            appendLine("Saved: " + java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US)
                .format(java.util.Date()))
            appendLine()
            appendLine(message.text)
            if (message.sources.isNotEmpty()) {
                appendLine()
                appendLine("Sources:")
                message.sources.forEach { appendLine("- $it") }
            }
        }
        downloadStatus = Downloader.saveText(context, question, body)
        toast(downloadStatus)
    }

    fun downloadSection(section: Section) {
        val body = buildString {
            appendLine("HOWVERSE — ${section.letter} · ${section.title}")
            appendLine(section.blurb)
            appendLine()
            appendLine("Topics (${section.topics.size}):")
            section.topics.forEachIndexed { i, topic -> appendLine("${i + 1}. $topic") }
            appendLine()
            appendLine("Learning modes:")
            Catalogue.modes.forEach { appendLine("- $it") }
            appendLine()
            appendLine("Ask HOWVERSE AI about any topic in the app for a step-by-step lesson.")
        }
        downloadStatus = Downloader.saveText(context, "${section.title}-topics", body)
        toast(downloadStatus)
    }

    fun changeLanguage(code: String) {
        languageCode = code
        prefs.languageCode = code
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
            AppHeader(
                route = route,
                isDark = isDark,
                onToggleTheme = { onThemeMode(if (isDark) "light" else "dark") },
                onProfile = { route = Route.Profile },
                onBack = { route = Route.Catalogue }
            )

            when (val current = route) {
                Route.Home -> HomeScreen(
                    query = query,
                    onQueryChange = { query = it },
                    onAsk = { ask(it) },
                    onOpenSection = { route = Route.Section(it) },
                    onOpenCatalogue = { route = Route.Catalogue },
                    onOpenDownloads = { route = Route.Downloads },
                    language = language,
                    onOpenLanguage = { route = Route.Profile }
                )

                Route.Catalogue -> CatalogueScreen(
                    query = catalogueQuery,
                    onQueryChange = { catalogueQuery = it },
                    onOpenSection = { route = Route.Section(it) },
                    onAskTopic = { ask(it) },
                    onVideoTopic = { openVideos(context, it) }
                )

                is Route.Section -> {
                    val section = Catalogue.byId(current.id)
                    if (section == null) {
                        route = Route.Catalogue
                    } else {
                        SectionScreen(
                            section = section,
                            language = language,
                            onAsk = { topic -> ask(topic, section.title) },
                            onVideo = { topic -> openVideos(context, topic) },
                            onDownload = { downloadSection(section) }
                        )
                    }
                }

                Route.Chat -> ChatScreen(
                    messages = messages,
                    loading = loading,
                    backendUrl = backendUrl,
                    language = language,
                    voiceNote = voiceNote,
                    onAsk = { ask(it) },
                    onSpeak = { speak(it) },
                    onSave = { saveMessage(it) },
                    onDownload = { downloadMessage(it) },
                    onClear = { messages.clear() }
                )

                Route.Saved -> SavedScreen(
                    saved = saved,
                    onOpen = { item ->
                        val stamp = System.currentTimeMillis()
                        messages.add(ChatMessage(id = stamp, role = Role.USER, text = item.question))
                        messages.add(ChatMessage(id = stamp + 1, role = Role.AI, text = item.answer))
                        route = Route.Chat
                    },
                    onDelete = { item ->
                        saved.removeAll { it.id == item.id }
                        prefs.persistSaved(saved.toList())
                    }
                )

                Route.Downloads -> DownloadsScreen(
                    packUrl = packUrl,
                    onPackUrlChange = {
                        packUrl = it
                        prefs.packUrl = it
                    },
                    packs = packs,
                    busy = packBusy,
                    status = downloadStatus,
                    onDownloadPack = {
                        if (packUrl.isBlank()) {
                            downloadStatus = "Enter a topic pack URL first"
                        } else {
                            packBusy = true
                            scope.launch {
                                val result = runCatching { Downloader.downloadPack(context, packUrl) }
                                packBusy = false
                                packs = Downloader.listPacks(context)
                                downloadStatus = result.fold(
                                    onSuccess = { "Downloaded ${it.name} (${it.length() / 1024} KB)" },
                                    onFailure = { "Download failed: ${it.message ?: "unknown error"}" }
                                )
                            }
                        }
                    },
                    onDeletePack = { file ->
                        Downloader.deletePack(file)
                        packs = Downloader.listPacks(context)
                    }
                )

                Route.Profile -> ProfileScreen(
                    themeMode = themeMode,
                    onThemeMode = onThemeMode,
                    language = language,
                    onLanguage = { changeLanguage(it) },
                    voiceNote = voiceNote,
                    backendUrl = backendUrl,
                    onBackendUrlChange = { url ->
                        backendUrl = url
                        prefs.backendUrl = url
                    },
                    savedCount = saved.size,
                    onOpenDownloads = { route = Route.Downloads },
                    onTestVoice = { speak("HOWVERSE. Learn anything. Understand everything.") }
                )
            }

            BottomBar(
                route = route,
                onTab = { tab ->
                    route = when (tab) {
                        "Home" -> Route.Home
                        "Catalogue" -> Route.Catalogue
                        "AI" -> Route.Chat
                        "Saved" -> Route.Saved
                        else -> Route.Profile
                    }
                }
            )
        }
    }
}

@Composable
private fun AppHeader(
    route: Route,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    onProfile: () -> Unit,
    onBack: () -> Unit
) {
    val p = LocalPalette.current
    val pushed = route is Route.Section || route == Route.Downloads
    val title = when (route) {
        is Route.Section -> Catalogue.byId(route.id)?.title ?: "Catalogue"
        Route.Downloads -> "Downloads"
        else -> ""
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = if (pushed) 6.dp else 22.dp, end = 14.dp, top = 14.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (pushed) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, "Back", tint = p.text)
                }
                Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = p.text)
            }
        } else {
            Column {
                Text(
                    "HOWVERSE",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = p.text
                )
                Text("${Catalogue.totalTopics} topics · A to Z", color = p.softText, fontSize = 12.sp)
            }
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
                    .clickable { onProfile() }
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
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onAsk: (String) -> Unit,
    placeholder: String = "What do you want to learn?"
) {
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
            placeholder = { Text(placeholder, color = p.softText) },
            leadingIcon = { Icon(Icons.Filled.Search, null, tint = p.accent) },
            shape = RoundedCornerShape(20.dp),
            colors = fieldColors(p),
            maxLines = 2
        )
        Spacer(Modifier.width(8.dp))
        FilledIconButton(
            onClick = { onAsk(query) },
            enabled = query.isNotBlank(),
            modifier = Modifier.size(54.dp)
        ) {
            Icon(Icons.Filled.Send, "Ask")
        }
    }
}

@Composable
private fun ColumnScope.HomeScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    onAsk: (String) -> Unit,
    onOpenSection: (String) -> Unit,
    onOpenCatalogue: () -> Unit,
    onOpenDownloads: () -> Unit,
    language: Language,
    onOpenLanguage: () -> Unit
) {
    val p = LocalPalette.current
    Column(Modifier.weight(1f)) {
        SearchBar(query = query, onQueryChange = onQueryChange, onAsk = onAsk)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Surface(
                    color = p.heroBg,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, p.heroBorder)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "Your AI learning companion",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = p.text
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(
                                    "${Catalogue.totalTopics} how-to topics, ${Catalogue.sections.size} " +
                                        "sections, ${Languages.all.size} languages.",
                                    color = p.softText,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                            Icon(
                                Icons.Filled.AutoAwesome,
                                null,
                                tint = p.accent,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SmallAction(
                                label = "A–Z catalogue",
                                icon = Icons.Filled.MenuBook,
                                filled = true,
                                onClick = onOpenCatalogue
                            )
                            SmallAction(
                                label = language.label,
                                icon = Icons.Filled.Translate,
                                filled = false,
                                onClick = onOpenLanguage
                            )
                            SmallAction(
                                label = "Downloads",
                                icon = Icons.Filled.Download,
                                filled = false,
                                onClick = onOpenDownloads
                            )
                        }
                    }
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Browse A to Z",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = p.text,
                        modifier = Modifier.weight(1f)
                    )
                    Text("${Catalogue.sections.size} sections", color = p.softText, fontSize = 12.sp)
                }
            }
            items(Catalogue.sections.chunked(2)) { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    pair.forEach { section ->
                        SectionTile(section = section, onClick = { onOpenSection(section.id) })
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
            item {
                Spacer(Modifier.height(4.dp))
                Text("Learning modes", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = p.text)
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Catalogue.modes.forEach { mode ->
                        Surface(
                            color = p.glass,
                            shape = RoundedCornerShape(999.dp),
                            border = BorderStroke(1.dp, p.cardBorder)
                        ) {
                            Text(
                                mode,
                                color = p.softText,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 13.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
            item {
                Text("Popular things to learn", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = p.text)
            }
            items(starterQuestions) { suggestion ->
                Surface(
                    color = p.panel,
                    shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(1.dp, p.cardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAsk(suggestion) }
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Lightbulb, null, tint = p.accent, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(suggestion, modifier = Modifier.weight(1f), fontSize = 14.sp, color = p.text)
                        Icon(
                            Icons.Filled.ChevronRight,
                            null,
                            tint = p.softText,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallAction(label: String, icon: ImageVector, filled: Boolean, onClick: () -> Unit) {
    val p = LocalPalette.current
    Surface(
        color = if (filled) p.accent else p.glass,
        shape = RoundedCornerShape(13.dp),
        border = BorderStroke(1.dp, if (filled) p.accent else p.cardBorder),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                null,
                tint = if (filled) p.onAccent else p.accent,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (filled) p.onAccent else p.text
            )
        }
    }
}

@Composable
private fun RowScope.SectionTile(section: Section, onClick: () -> Unit) {
    val p = LocalPalette.current
    val bitmap = sectionArt(section.art)
    Surface(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick() },
        color = p.glass,
        shape = RoundedCornerShape(19.dp),
        border = BorderStroke(1.dp, p.cardBorder)
    ) {
        Column {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = section.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                )
            }
            Column(Modifier.padding(13.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        section.letter,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = p.accent
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("${section.topics.size}", fontSize = 11.sp, color = p.softText)
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    section.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = p.text,
                    maxLines = 2,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.CatalogueScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    onOpenSection: (String) -> Unit,
    onAskTopic: (String) -> Unit,
    onVideoTopic: (String) -> Unit
) {
    val p = LocalPalette.current
    val results = remember(query) { Catalogue.search(query) }
    val sections = remember(query) { Catalogue.sectionsMatching(query) }

    Column(Modifier.weight(1f)) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onAsk = { onAskTopic(it) },
            placeholder = "Search ${Catalogue.totalTopics} topics"
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (query.isNotBlank() && results.isNotEmpty()) {
                item {
                    Text(
                        "${results.size} matching topics",
                        color = p.softText,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                items(results.take(60)) { pair ->
                    val section = pair.first
                    val topic = pair.second
                    Surface(
                        color = p.panel,
                        shape = RoundedCornerShape(15.dp),
                        border = BorderStroke(1.dp, p.cardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAskTopic(topic) }
                    ) {
                        Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                section.letter,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = p.accent,
                                modifier = Modifier.width(22.dp)
                            )
                            Column(Modifier.weight(1f)) {
                                Text(topic, fontSize = 14.sp, color = p.text)
                                Text(section.title, fontSize = 11.sp, color = p.softText)
                            }
                            IconButton(onClick = { onVideoTopic(topic) }) {
                                Icon(
                                    Icons.Filled.PlayArrow,
                                    "Videos",
                                    tint = p.accent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                if (query.isNotBlank()) {
                    item {
                        Text(
                            "No topic matches \"$query\". Ask the AI instead — it can explain anything.",
                            color = p.softText,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
                items(sections) { section ->
                    Surface(
                        color = p.glass,
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, p.cardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenSection(section.id) }
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            val bitmap = sectionArt(section.art)
                            Surface(shape = RoundedCornerShape(12.dp), color = p.indicator) {
                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(56.dp)
                                    )
                                } else {
                                    Box(Modifier.size(56.dp))
                                }
                            }
                            Spacer(Modifier.width(13.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "${section.letter} · ${section.title}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = p.text
                                )
                                Text(
                                    "${section.topics.size} topics · ${section.blurb}",
                                    fontSize = 11.sp,
                                    color = p.softText,
                                    maxLines = 2,
                                    lineHeight = 15.sp
                                )
                            }
                            Icon(
                                Icons.Filled.ChevronRight,
                                null,
                                tint = p.softText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.SectionScreen(
    section: Section,
    language: Language,
    onAsk: (String) -> Unit,
    onVideo: (String) -> Unit,
    onDownload: () -> Unit
) {
    val p = LocalPalette.current
    val bitmap = sectionArt(section.art)
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 6.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(shape = RoundedCornerShape(20.dp), color = p.panel) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = section.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
            }
        }
        item {
            Text(
                "${section.letter} · ${section.title}",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = p.text
            )
            Spacer(Modifier.height(4.dp))
            Text(section.blurb, color = p.softText, fontSize = 13.sp, lineHeight = 18.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                "${section.topics.size} topics · taught in ${language.label}",
                color = p.accent,
                fontSize = 12.sp
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SmallAction(
                    label = "Ask AI",
                    icon = Icons.Filled.AutoAwesome,
                    filled = true,
                    onClick = { onAsk("Teach me the basics of ${section.title}") }
                )
                SmallAction(
                    label = "Videos",
                    icon = Icons.Filled.PlayArrow,
                    filled = false,
                    onClick = { onVideo(section.title) }
                )
                SmallAction(
                    label = "Download",
                    icon = Icons.Filled.Download,
                    filled = false,
                    onClick = onDownload
                )
            }
        }
        item {
            Text("Learning modes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = p.text)
            Spacer(Modifier.height(6.dp))
            Row(
                Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Catalogue.modes.forEach { mode ->
                    Surface(
                        color = p.glass,
                        shape = RoundedCornerShape(999.dp),
                        border = BorderStroke(1.dp, p.cardBorder)
                    ) {
                        Text(
                            mode,
                            color = p.softText,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }
        item {
            Text(
                "Topics",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = p.text,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        items(section.topics) { topic ->
            Surface(
                color = p.panel,
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(1.dp, p.cardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAsk(topic) }
            ) {
                Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.School, null, tint = p.accent, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(11.dp))
                    Text(topic, modifier = Modifier.weight(1f), fontSize = 14.sp, color = p.text)
                    IconButton(onClick = { onVideo(topic) }) {
                        Icon(
                            Icons.Filled.PlayArrow,
                            "Videos",
                            tint = p.accent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Icon(Icons.Filled.ChevronRight, null, tint = p.softText, modifier = Modifier.size(18.dp))
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
    language: Language,
    voiceNote: String,
    onAsk: (String) -> Unit,
    onSpeak: (String) -> Unit,
    onSave: (ChatMessage) -> Unit,
    onDownload: (ChatMessage) -> Unit,
    onClear: () -> Unit
) {
    val p = LocalPalette.current
    val listState = rememberLazyListState()
    var input by remember { mutableStateOf("") }

    LaunchedEffect(messages.size, messages.lastOrNull()?.pending) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Column(Modifier.weight(1f)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 10.dp, top = 2.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.AutoAwesome, null, tint = p.accent, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("HOWVERSE AI", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = p.text)
            Spacer(Modifier.width(8.dp))
            StatusChip(live = backendUrl.isNotBlank(), label = language.label)
            Spacer(Modifier.weight(1f))
            if (messages.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        Icons.Filled.Delete,
                        "Clear conversation",
                        tint = p.softText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 10.dp),
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
                            Text(
                                "Hi, I'm HOWVERSE AI",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = p.text
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Ask me about any of the ${Catalogue.totalTopics} topics in the " +
                                    "A–Z catalogue — or anything else. I answer in ${language.label}, " +
                                    "step by step.",
                                color = p.softText,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                            if (voiceNote.isNotBlank()) {
                                Spacer(Modifier.height(8.dp))
                                Text(voiceNote, color = p.softText, fontSize = 11.sp, lineHeight = 15.sp)
                            }
                        }
                    }
                }
                item {
                    Column(Modifier.padding(top = 4.dp)) {
                        Text(
                            "Try asking",
                            color = p.softText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
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
                                    Icon(
                                        Icons.Filled.Lightbulb,
                                        null,
                                        tint = p.accent,
                                        modifier = Modifier.size(18.dp)
                                    )
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
                    AiBubble(message = message, onSpeak = onSpeak, onSave = onSave, onDownload = onDownload)
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
                placeholder = {
                    Text("Ask HOWVERSE in ${language.label}…", color = p.softText, fontSize = 14.sp)
                },
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
private fun StatusChip(live: Boolean, label: String) {
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
                if (live) label else "Offline · $label",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = if (live) p.accent else p.softText
            )
        }
    }
}

@Composable
private fun AiBubble(
    message: ChatMessage,
    onSpeak: (String) -> Unit,
    onSave: (ChatMessage) -> Unit,
    onDownload: (ChatMessage) -> Unit
) {
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
                    Text(message.text, color = p.text, fontSize = 14.sp, lineHeight = 21.sp)
                    if (message.sources.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "Web sources used",
                            fontWeight = FontWeight.Bold,
                            color = p.accent,
                            fontSize = 12.sp
                        )
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
                            Text(
                                message.notice,
                                color = p.softText,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onSpeak(message.text) }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Filled.VolumeUp,
                                "Read aloud",
                                tint = p.accent,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        IconButton(onClick = { onSave(message) }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Filled.BookmarkAdd,
                                "Save lesson",
                                tint = p.accent,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        IconButton(onClick = { onDownload(message) }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Filled.Download,
                                "Download lesson",
                                tint = p.accent,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        IconButton(
                            onClick = { clipboard.setText(AnnotatedString(message.text)) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Filled.ContentCopy,
                                "Copy",
                                tint = p.softText,
                                modifier = Modifier.size(16.dp)
                            )
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
private fun ColumnScope.SavedScreen(
    saved: List<SavedLesson>,
    onOpen: (SavedLesson) -> Unit,
    onDelete: (SavedLesson) -> Unit
) {
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
                            "Ask something in the AI tab and tap the bookmark icon on any answer.",
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
                        Icon(
                            Icons.Filled.Delete,
                            "Delete",
                            tint = p.softText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.DownloadsScreen(
    packUrl: String,
    onPackUrlChange: (String) -> Unit,
    packs: List<File>,
    busy: Boolean,
    status: String,
    onDownloadPack: () -> Unit,
    onDeletePack: (File) -> Unit
) {
    val p = LocalPalette.current
    var draft by remember { mutableStateOf(packUrl) }

    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SettingsCard(
                title = "Save lessons to your phone",
                subtitle = "Every AI answer has a download button. Tap it and the lesson is written " +
                    "as a .txt file in Downloads/HOWVERSE — in whatever language you chose."
            ) {
                Text(
                    "Sections can be downloaded too: open any A–Z section and tap Download to get " +
                        "all of its topics as a checklist you can keep offline.",
                    color = p.softText,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
        item {
            SettingsCard(
                title = "Topic packs",
                subtitle = "Point HOWVERSE at a JSON pack URL and it stores the pack on the device " +
                    "for offline browsing."
            ) {
                OutlinedTextField(
                    value = draft,
                    onValueChange = {
                        draft = it
                        onPackUrlChange(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("https://example.com/pack.json", color = p.softText, fontSize = 13.sp)
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors(p),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = p.accent,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable(enabled = !busy) { onDownloadPack() }
                    ) {
                        Text(
                            if (busy) "Downloading…" else "Download pack",
                            color = p.onAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    if (status.isNotBlank()) {
                        Text(status, color = p.softText, fontSize = 11.sp, lineHeight = 15.sp)
                    }
                }
            }
        }
        item {
            Text("Downloaded packs", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = p.text)
        }
        if (packs.isEmpty()) {
            item {
                Text(
                    "No packs yet. Lessons and sections you download go to your phone's Downloads " +
                        "folder, not here.",
                    color = p.softText,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
        items(packs) { file ->
            Surface(
                color = p.panel,
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(1.dp, p.cardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Download, null, tint = p.accent, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(file.name, color = p.text, fontSize = 13.sp)
                        Text(
                            "${file.length() / 1024} KB · stored on device",
                            color = p.softText,
                            fontSize = 11.sp
                        )
                    }
                    IconButton(onClick = { onDeletePack(file) }) {
                        Icon(
                            Icons.Filled.Delete,
                            "Delete pack",
                            tint = p.softText,
                            modifier = Modifier.size(18.dp)
                        )
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
    language: Language,
    onLanguage: (String) -> Unit,
    voiceNote: String,
    backendUrl: String,
    onBackendUrlChange: (String) -> Unit,
    savedCount: Int,
    onOpenDownloads: () -> Unit,
    onTestVoice: () -> Unit
) {
    val p = LocalPalette.current
    var draft by remember { mutableStateOf(backendUrl) }
    var savedFlag by remember { mutableStateOf(false) }
    var showLanguages by remember { mutableStateOf(false) }

    if (showLanguages) {
        AlertDialog(
            onDismissRequest = { showLanguages = false },
            confirmButton = {
                TextButton(onClick = { showLanguages = false }) { Text("Done", color = p.accent) }
            },
            title = { Text("Teaching language", color = p.text) },
            text = {
                LazyColumn(Modifier.height(400.dp)) {
                    items(Languages.all) { item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onLanguage(item.code)
                                    showLanguages = false
                                }
                                .padding(vertical = 11.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                item.label,
                                modifier = Modifier.weight(1f),
                                color = if (item.code == language.code) p.accent else p.text,
                                fontSize = 14.sp,
                                fontWeight = if (item.code == language.code) {
                                    FontWeight.SemiBold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                            Text(item.tag, color = p.softText, fontSize = 11.sp)
                        }
                    }
                }
            },
            containerColor = p.panel
        )
    }

    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = p.text)
            Text("Theme, language, AI backend and downloads.", color = p.softText, fontSize = 12.sp)
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
                title = "Language",
                subtitle = "Lessons are requested in this language and read aloud with the matching " +
                    "device voice. ${Languages.all.size} languages available."
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = p.accent,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { showLanguages = true }
                    ) {
                        Row(
                            Modifier.padding(horizontal = 15.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Translate,
                                null,
                                tint = p.onAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(7.dp))
                            Text(
                                language.label,
                                color = p.onAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Test voice",
                        color = p.accent,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { onTestVoice() }
                    )
                }
                if (voiceNote.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(voiceNote, color = p.softText, fontSize = 11.sp, lineHeight = 15.sp)
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
                    placeholder = {
                        Text("https://your-ai-server.example", color = p.softText, fontSize = 13.sp)
                    },
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
                            else -> "Connected: " + backendUrl
                                .removePrefix("https://")
                                .removePrefix("http://")
                        },
                        color = if (savedFlag) p.good else p.softText,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
        item {
            SettingsCard(title = "Downloads", subtitle = "Saved lessons and topic packs.") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onOpenDownloads() }
                ) {
                    Icon(Icons.Filled.Download, null, tint = p.accent, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(9.dp))
                    Text(
                        "Open downloads",
                        color = p.text,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Filled.ChevronRight, null, tint = p.softText, modifier = Modifier.size(20.dp))
                }
            }
        }
        item {
            SettingsCard(title = "About", subtitle = "HOWVERSE 2.0") {
                Text(
                    "${Catalogue.totalTopics} topics across ${Catalogue.sections.size} sections, " +
                        "${Catalogue.modes.size} learning modes and ${Languages.all.size} languages. " +
                        "$savedCount saved lesson(s) on this device. Offline answers come from a " +
                        "hand-written library of guides; connect an AI backend for live, " +
                        "model-generated lessons in any language.",
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
private fun BottomBar(route: Route, onTab: (String) -> Unit) {
    val p = LocalPalette.current
    val active = when (route) {
        Route.Home -> "Home"
        Route.Catalogue, is Route.Section -> "Catalogue"
        Route.Chat -> "AI"
        Route.Saved -> "Saved"
        else -> "Profile"
    }
    NavigationBar(containerColor = p.navBg) {
        listOf(
            "Home" to Icons.Filled.Home,
            "Catalogue" to Icons.Filled.MenuBook,
            "AI" to Icons.Filled.AutoAwesome,
            "Saved" to Icons.Filled.Bookmarks,
            "Profile" to Icons.Filled.Person
        ).forEach { (name, icon) ->
            NavigationBarItem(
                selected = active == name,
                onClick = { onTab(name) },
                icon = { Icon(icon, null) },
                label = { Text(name, fontSize = 10.sp) },
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
