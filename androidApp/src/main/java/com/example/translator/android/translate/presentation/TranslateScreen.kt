package com.example.translator.android.translate.presentation

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.example.translator.android.R
import com.example.translator.android.translate.presentation.components.LanguageDropDown
import com.example.translator.android.translate.presentation.components.SwapLanguagesButton
import com.example.translator.android.translate.presentation.components.TranslateHistoryItem
import com.example.translator.android.translate.presentation.components.TranslateTextField
import com.example.translator.android.translate.presentation.components.rememberTextToSpeech
import com.example.translator.translate.domain.translate.TranslateError
import com.example.translator.translate.presentation.TranslateEvent
import com.example.translator.translate.presentation.TranslateState
import java.util.Locale

@Composable
fun TranslateScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(
        state.error
    ) {
        val message = when (state.error) {
            TranslateError.SERVICE_UNAVAILABLE -> context.getString(R.string.error_service_unavailable)
            TranslateError.CLIENT_ERROR -> context.getString(R.string.error_client)
            TranslateError.SERVER_ERROR -> context.getString(R.string.error_server)
            TranslateError.UNKNOWN_ERROR -> context.getString(R.string.error_unknown)
            else -> null
        }
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            onEvent(TranslateEvent.OnErrorSeen)
        }
    }
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(TranslateEvent.RecordAudio) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .size(75.dp)
            ) {
               Icon(
                   imageVector = ImageVector.vectorResource(
                       R.drawable.mic
                   ),
                   contentDescription = stringResource(R.string.record_audio)
               )
            }

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)

                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LanguageDropDown(
                        language = state.fromLanguage,
                        isOpen = state.isChoosingFromLanguage,
                        onClick = { onEvent(TranslateEvent.OpenFromLanguageDropDown) },
                        onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
                        onSelectLanguage = { language ->
                            onEvent(TranslateEvent.ChooseFromLanguage(language))
                        },
                    )
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    SwapLanguagesButton(
                        onClick = { onEvent(TranslateEvent.SwapLanguages) },
                    )
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    LanguageDropDown(
                        language = state.toLanguage,
                        isOpen = state.isChoosingToLanguage,
                        onClick = { onEvent(TranslateEvent.OpenToLanguageDropDown) },
                        onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
                        onSelectLanguage = { language ->
                            onEvent(TranslateEvent.ChooseToLanguage(language))
                        },
                    )
                }
            }
            item {
                val clipboardManager = LocalClipboardManager.current
                val keyboardController = LocalSoftwareKeyboardController.current
                val tts = rememberTextToSpeech()
                TranslateTextField(
                    fromText = state.fromText,
                    toText = state.toText,
                    isTranslating = state.isTranslating,
                    fromLanguage = state.fromLanguage,
                    toLanguage = state.toLanguage,
                    onTranslateClick = {
                        keyboardController?.hide()
                        onEvent(TranslateEvent.Translate)
                    },
                    onTextChange = { text -> onEvent(TranslateEvent.ChangeTranslationText(text)) },
                    onCopyClick = { text ->
                        clipboardManager.setText(
                            buildAnnotatedString {
                                append(text)
                            }
                        )
                        Toast.makeText(
                            context,
                            context.getString(R.string.copied_to_clipboard),
                            Toast.LENGTH_SHORT
                        ).show()

                    },
                    onCloseClick = { onEvent(TranslateEvent.CloseTranslation) },
                    onSpeakerClick = {
                        tts.language = state.toLanguage.toLocale() ?: Locale.ENGLISH
                        tts.speak(state.toText, TextToSpeech.QUEUE_FLUSH, null, null)
                    },
                    onTextFieldClick = { onEvent(TranslateEvent.EditTranslation) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                if (state.history.isNotEmpty())
                    Text(
                        text = context.getString(R.string.history),
                        style = MaterialTheme.typography.h2
                    )
            }
            items(state.history) { item ->
                TranslateHistoryItem(
                    item = item,
                    onClick = { onEvent(TranslateEvent.SelectHistoryItem(item)) },
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }
    }
}