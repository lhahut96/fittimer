package com.fittimer

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.media.MediaPlayer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fittimer.ui.FitTimerClockState
import com.fittimer.ui.FitTimerViewModel
import com.fittimer.ui.ProgressingScreen
import com.fittimer.ui.StartScreen
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class FitTimerScreen(@StringRes val screenName: Int) {
    Start(R.string.fittimer), Progressing(R.string.workout)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FitTimerAppBar(
    currentScreen: FitTimerScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onMusicIcon: () -> Unit,
    isMusicPlaying: Boolean
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text((stringResource(currentScreen.screenName)))
                IconButton(onClick = { onMusicIcon() }) {
                    Icon(
                        painter = painterResource(id = if (isMusicPlaying) R.drawable.pause_button_svgrepo_com else R.drawable.play_button_svgrepo_com),
                        contentDescription = "Play pause Button",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = colorResource(id = R.color.primary),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        }
    )
}

@Composable
fun FitTimerApp(
    viewModel: FitTimerViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val scope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val startMediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.start)
    val notifMediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.notif)
    val currentScreen =
        FitTimerScreen.valueOf(backStackEntry?.destination?.route ?: FitTimerScreen.Start.name)

    val uiState = viewModel.uiState.collectAsState()
    val clockState = uiState.value.clockState

    var isSpotifyPlaying by remember { mutableStateOf(false) }

    // Spotify API
    val clientId = "44b51861d5ab485798d2f05ec0546b66"
    val redirectURI = "com.fittimer://auth"
    var spotifyAppRemote: SpotifyAppRemote? = null
    val connectionParams = ConnectionParams.Builder(clientId)
        .setRedirectUri(redirectURI)
        .showAuthView(true)
        .build()
    val builder =
        AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectURI)

    builder.setScopes(arrayOf("streaming"))
    val request = builder.build()
    val context = LocalContext.current.getActivity()
    LaunchedEffect(context) {
        AuthorizationClient.openLoginActivity(context, 1337, request)
    }

    SpotifyAppRemote.connect(
        context,
        connectionParams,
        object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                // Now you can start interacting with App Remote
                appRemote.playerApi.subscribeToPlayerState().setEventCallback { playerState ->
                    run {
                        isSpotifyPlaying = !playerState.isPaused
                    }
                }
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        }
    )


    val playPauseMusic: () -> Unit = {
        if (isSpotifyPlaying) {
            spotifyAppRemote?.playerApi?.pause()
        } else {
            spotifyAppRemote?.playerApi?.resume()
        }
    }
    val workoutState = uiState.value.workState

    LaunchedEffect(workoutState) {
        if (clockState == FitTimerClockState.Progressing)
        notifMediaPlayer.start()
    }


    LaunchedEffect(clockState) {
        if (clockState == FitTimerClockState.Stop) {
//            startMediaPlayer.start()
            navController.navigateUp()
            viewModel.resetTimer()
        }
    }

    Scaffold(
        topBar = {
            FitTimerAppBar(
                currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    navController.navigateUp()
                    viewModel.resetTimer()
                },
                onMusicIcon = {
                    playPauseMusic()
                },
                isMusicPlaying = isSpotifyPlaying
            )
        },
        modifier = Modifier.imePadding()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(300)
                )
            },
            startDestination = FitTimerScreen.Start.name,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(FitTimerScreen.Start.name) {
                StartScreen(viewModel,
                    onStartButton =
                    {
                        scope.launch {
                            startMediaPlayer.start()
                            delay(4000L)
                            navController.navigate(FitTimerScreen.Progressing.name)
                        }
                    })

            }

            composable(FitTimerScreen.Progressing.name) {
                ProgressingScreen(viewModel)
            }
        }

    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

fun Context.getActivity(): Activity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}