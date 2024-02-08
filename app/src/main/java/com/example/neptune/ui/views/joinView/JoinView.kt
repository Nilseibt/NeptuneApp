package com.example.neptune.ui.views.joinView

import NeptuneOutlinedTextField
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.neptune.R
import com.example.neptune.ui.commons.TopBar
import com.example.neptune.ui.theme.InvalidInputWarningColor
import com.example.neptune.ui.theme.NeptuneTheme
import com.example.neptune.ui.views.util.viewModelFactory
import kotlinx.coroutines.delay
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinView(navController: NavController) {

    val joinViewModel = viewModel<JoinViewModel>(
        factory = viewModelFactory {
            JoinViewModel(
                //NeptuneApp.appState
            )
        }
    )

    BackHandler {
        joinViewModel.onBack(navController)
    }

    NeptuneTheme {

        JoinViewContent(joinViewModel = joinViewModel, navController = navController)

    }
}

@Composable
private fun JoinViewContent(joinViewModel: JoinViewModel, navController: NavController) {


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(modifier = Modifier
            .fillMaxSize()
        ) {

            TopBar(onBack = { joinViewModel.onBack(navController) })

        }

        Column (modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){

            SessionCodeInputField(joinViewModel = joinViewModel)

            if(joinViewModel.wasLastCodeInvalid()) {

                InvalidSessionCodeText()
            }


            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ){
                // This is a guard for prohibiting double clicking the join button
                var joinButtonEnabled by remember { mutableStateOf(true) }
                LaunchedEffect(joinButtonEnabled) {
                    if (joinButtonEnabled) {
                        return@LaunchedEffect
                    } else {
                        delay(1000)
                        joinButtonEnabled = true
                    }
                }

                ConfirmationButton(joinViewModel = joinViewModel, navController = navController, joinButtonEnabled = joinButtonEnabled)


                OpenQRCodeReaderButton(joinViewModel = joinViewModel, navController = navController)

            }
        }
    }
}


@Composable
private fun SessionCodeInputField(joinViewModel: JoinViewModel) {
    NeptuneOutlinedTextField(
        joinViewModel,
        labelText = stringResource(id = R.string.enter_six_digit_code))
}

@Composable
private fun InvalidSessionCodeText() {
    Text(
        modifier = Modifier.padding(8.dp),
        text = stringResource(id = R.string.invalid_session_code),
        color = InvalidInputWarningColor)
}
@Composable
private fun ConfirmationButton(joinViewModel: JoinViewModel, navController: NavController,joinButtonEnabled: Boolean) {
    var buttonEnabled = joinButtonEnabled
    Button(
        onClick = {
            if (buttonEnabled) {
                buttonEnabled = false
                joinViewModel.onConfirmSessionCode(navController)
            }
        },
        enabled = joinViewModel.isCodeInputFormValid()
    )
    {
        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
    }
}

@Composable
fun OpenQRCodeReaderButton(joinViewModel: JoinViewModel, navController: NavController) {
    val context = LocalContext.current
    var isCameraViewVisible by remember { mutableStateOf(false) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            isCameraViewVisible = true
        } else {
            // Handle the case where the user denies the permission request
        }
    }

    Button(onClick = {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                isCameraViewVisible = true
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }) {
        Text("QR-Code Reader öffnen")
    }

    if (isCameraViewVisible) {
        CameraPreviewWithQRCodeScanner { qrCodeText ->
            joinViewModel.onQRCodeDetected(navController, qrCodeText)
        }
    }
}

@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
fun CameraPreviewWithQRCodeScanner(onQRCodeDetected: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    val previewView = remember { PreviewView(context) }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                        val scanner = BarcodeScanning.getClient()

                        scanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    if (barcode.format == Barcode.FORMAT_QR_CODE) {
                                        val text = barcode.rawValue
                                        if (text != null) {
                                            onQRCodeDetected(text)
                                        }
                                    }
                                }
                            }
                            .addOnFailureListener {
                                // Handle any errors
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    }
                })
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
        } catch (exc: Exception) {
            // Handle any errors
        }
    }, ContextCompat.getMainExecutor(context))

    // Add the PreviewView to the Compose hierarchy
    AndroidView({ previewView }) { view ->
        // Update the view's state here
    }
}