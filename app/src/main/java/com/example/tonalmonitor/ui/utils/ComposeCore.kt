package com.example.tonalmonitor.ui.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestAndroidPermission(permissionState: PermissionState) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                permissionState.launchPermissionRequest()
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ABXFloatingButton(onClick: () -> Unit, contentDescription: String, imageVector: ImageVector) {
    val contentColor = Color.Black//MaterialTheme.colorScheme.primaryContainer
    val context: Context = LocalContext.current

    Surface(
        onClick = onClick,
        shape = FloatingActionButtonDefaults.shape,
        color = FloatingActionButtonDefaults.containerColor,
        contentColor = contentColor,
//        tonalElevation = FloatingActionButtonDefaults.elevation(),
//        shadowElevation = elevation.shadowElevation(interactionSource = interactionSource).value,
//        interactionSource = interactionSource,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            // Adding the text style from [ExtendedFloatingActionButton] to all FAB variations. In
            // the majority of cases this will have no impact, because icons are expected, but if a
            // developer decides to put some short text to emulate an icon, (like "?") then it will
            // have the correct styling.
            ProvideTextStyle(
                MaterialTheme.typography.labelLarge
            ) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = 56.dp,
                            minHeight = 56.dp,
                        ).pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    onClick()
                                },
                                onLongPress = {
                                    Toast.makeText(context, contentDescription, Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(imageVector = imageVector, contentDescription = contentDescription)
                }
            }
        }
    }
}
