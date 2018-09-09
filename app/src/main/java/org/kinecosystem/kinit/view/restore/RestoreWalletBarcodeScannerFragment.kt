package org.kinecosystem.kinit.view.restore

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.core.util.size
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.barcode_scanner_fragment.*
import org.kinecosystem.kinit.KinitApplication
import org.kinecosystem.kinit.R
import org.kinecosystem.kinit.util.Scheduler
import org.kinecosystem.kinit.view.BaseFragment
import java.io.IOException
import javax.inject.Inject

class RestoreWalletBarcodeScannerFragment : BaseFragment() {
    @Inject
    lateinit var scheduler: Scheduler

    private lateinit var actions: RestoreWalletActions
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private val scannerCallback = object : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) = Unit
        override fun surfaceDestroyed(holder: SurfaceHolder?) = Unit
        override fun surfaceCreated(holder: SurfaceHolder?) {
            try {
                context?.let {
                    if (ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(scanner.holder)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private val qrProcessor = object : Detector.Processor<Barcode> {
        override fun release() = Unit
        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            val barcode: SparseArray<Barcode>? = detections?.detectedItems
            if (barcode?.size != 0) {
                scheduler.post {
                    listener?.onQrDecoded(barcode?.valueAt(0)?.rawValue.toString())
                    stopScanner()
                }
            }
        }
    }

    var listener: BarcodeScannerListener? = null

    interface BarcodeScannerListener {
        fun onQrDecoded(qrCode: String)
    }

    companion object {
        val TAG = RestoreWalletBarcodeScannerFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): RestoreWalletBarcodeScannerFragment {
            return RestoreWalletBarcodeScannerFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            actions = activity as RestoreWalletActions
        } catch (e: Exception) {
            Log.e(TAG, "activity must be RestoreWalletBarcodeScannerFragment and implement WalletCreationActions")
            activity?.finish()
        }
        return inflater.inflate(R.layout.barcode_scanner_fragment, container, false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        KinitApplication.coreComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    private fun initialiseDetectorsAndSources() {
        barcodeDetector = BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()

        cameraSource = CameraSource.Builder(context, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build()

        scanner.holder.addCallback(scannerCallback)
        barcodeDetector.setProcessor(qrProcessor)
    }

    private fun stopScanner() {
        try {
            cameraSource.release()
            barcodeDetector.release()
        } catch (e: Exception) {
            Log.d(TAG, "no camera was in use")
        }
    }

    override fun onResume() {
        super.onResume()
        initialiseDetectorsAndSources()
    }

    override fun onPause() {
        super.onPause()
        stopScanner()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity !is RestoreWalletActions) {
            Log.e(TAG, "Activity must implement RestoreActions")
            activity?.finish()
        }
        backBtn.setOnClickListener { actions.moveBack() }
    }
}