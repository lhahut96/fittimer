package com.fittimer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fittimer.ui.theme.KotLinProjectTheme
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.Connector;
import com.spotify.protocol.types.Track
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class MainActivity : ComponentActivity() {
//    private val clientId = "44b51861d5ab485798d2f05ec0546b66"
//    private val redirectURI = "com.fittimer://auth"
//    var spotifyAppRemote: SpotifyAppRemote? = null
//    private val connectionParams = ConnectionParams.Builder(clientId)
//        .setRedirectUri(redirectURI)
//        .showAuthView(true)
//        .build();
//    private val builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.TOKEN, redirectURI)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotLinProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    FitTimerApp()
                }
            }
        }
    }
//
//    override fun onStart() {
//        super.onStart()
//        builder.setScopes(arrayOf("streaming"))
//        val request = builder.build()
//        AuthorizationClient.openLoginActivity(this, 1337, request)
//        Log.i("spotify connect", "try connecting")
//        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
//            override fun onConnected(appRemote: SpotifyAppRemote) {
//                spotifyAppRemote = appRemote
//                Log.d("MainActivity", "Connected! Yay!")
//                // Now you can start interacting with App Remote
//                connected()
//            }
//
//            override fun onFailure(throwable: Throwable) {
//                Log.e("MainActivity", throwable.message, throwable)
//                // Something went wrong when attempting to connect! Handle errors here
//            }
//        })
//    }
//    private fun connected() {
//        Log.i("test spotify", spotifyAppRemote.toString())
//    }
//
//    private fun onPlay() {
//        spotifyAppRemote?.playerApi?.resume()
//    }
}

