package com.example.kotlinproject

import android.media.MediaPlayer
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kotlinproject.ui.FitTimerViewModel
import com.example.kotlinproject.ui.ProgressingScreen
import com.example.kotlinproject.ui.StartScreen
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
) {
    CenterAlignedTopAppBar(
        title = { Text((stringResource(currentScreen.screenName))) },
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
    navController: NavHostController = rememberNavController()
) {
    val scope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val startMediaPlayer = MediaPlayer.create(LocalContext.current, R.raw.start)
    val currentScreen =
        FitTimerScreen.valueOf(backStackEntry?.destination?.route ?: FitTimerScreen.Start.name)
    Scaffold(
        topBar = {
            FitTimerAppBar(currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() })
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