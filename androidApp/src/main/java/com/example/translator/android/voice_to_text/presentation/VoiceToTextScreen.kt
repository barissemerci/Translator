package com.example.translator.android.voice_to_text.presentation

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.translator.android.R
import com.example.translator.android.core.theme.LightBlue
import com.example.translator.android.voice_to_text.presentation.components.VoiceRecorderDisplay
import com.example.translator.voice_to_text.presentation.DisplayState
import com.example.translator.voice_to_text.presentation.VoiceToTextEvent
import com.example.translator.voice_to_text.presentation.VoiceToTextState

@Composable
fun VoiceToTextScreen(
    state: VoiceToTextState,
    languageCode: String,
    onResult: (String) -> Unit,
    onEvent: (VoiceToTextEvent) -> Unit,
) {
    val context = LocalContext.current
    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onEvent(
                VoiceToTextEvent.PermissionResult(
                    isGranted = isGranted,
                    isPermanentlyDeclined = !isGranted && !(context as ComponentActivity).shouldShowRequestPermissionRationale(
                        android.Manifest.permission.RECORD_AUDIO
                    )
                )
            )

        }
    )
    LaunchedEffect(recordAudioLauncher) {
        recordAudioLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                FloatingActionButton(
                    onClick = {
                        if (state.displayState != DisplayState.DISPLAYING_RESULTS) {
                            onEvent(
                                VoiceToTextEvent.ToggleRecording(
                                    languageCode = languageCode
                                )
                            )
                        } else {
                            onResult(state.spokenText)
                        }
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .size(75.dp)

                ) {
                    AnimatedContent(
                        targetState = state.displayState,
                    ) { displayState ->
                        when (displayState) {
                            DisplayState.DISPLAYING_RESULTS -> {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = stringResource(R.string.apply),
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                            DisplayState.SPEAKING -> {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = stringResource(R.string.stop_recording),
                                    modifier = Modifier.size(50.dp)
                                )
                            }


                            else -> {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.mic),
                                    contentDescription = stringResource(R.string.record_audio),
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }

                }


                if (state.displayState == DisplayState.DISPLAYING_RESULTS) {
                    IconButton(
                        onClick = {
                            onEvent(
                                VoiceToTextEvent.ToggleRecording(
                                    languageCode = languageCode
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = stringResource(R.string.record_again),
                            tint = LightBlue,
                        )
                    }
                }


            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 16.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = {
                        onEvent(VoiceToTextEvent.Close)
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.close)
                    )
                }
                if (state.displayState == DisplayState.SPEAKING) {
                    Text(
                        text = stringResource(R.string.listening),
                        color = LightBlue,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 100.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(state.displayState) { displayState ->
                    when (displayState) {
                        DisplayState.WAITING_TO_TALK -> {
                            Text(
                                text = stringResource(R.string.start_talking),
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center,
                            )
                        }

                        DisplayState.DISPLAYING_RESULTS -> {
                            Text(
                                text = state.spokenText,
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center,
                            )
                        }

                        DisplayState.SPEAKING -> {
                            VoiceRecorderDisplay(
                                powerRatios = state.powerRatios,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            )
                        }

                        DisplayState.ERROR -> {
                            Text(
                                text = state.recordErrorText ?: stringResource(R.string.error),
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.error
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}