package ru.example.apiclient.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.example.apiclient.databinding.FragmentCalcBinding
import ru.example.apiserver.ISubService

class CalcFragment : Fragment() {
    companion object {
        fun newInstance() = CalcFragment()
        const val ACTION_NAME = "connect_to_aidl_service"
    }

    private var mService: ISubService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = ISubService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCalcBinding.inflate(layoutInflater).apply {
        btnBind.setOnClickListener {
            requireContext().bindService(
                intentService(),
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
        btnCalc.setOnClickListener {
            val firstNum = editFirst.text.toString()
            val secondNum = editSecond.text.toString()
            try {
                val sub = mService?.sub(firstNum.toDouble(), secondNum.toDouble()) ?: 0
                result.text = sub.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }.root

    private fun intentService(): Intent {
        val intent = Intent(ACTION_NAME)
        val service = requireContext().packageManager.queryIntentServices(intent, 0)
        if (service.isEmpty()) {
            throw IllegalStateException("Приложение-сервер не установлен")
        }
        return Intent(intent).apply {
            val resolveInfo = service[0]
            val packageName = resolveInfo.serviceInfo.packageName
            val className = resolveInfo.serviceInfo.name
            component = ComponentName(packageName, className)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unbindService(serviceConnection)
    }

}