package com.login.base.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.login.base.R
import com.login.base.model.local.preference.PreferenceManager
import com.login.base.utils.CommonUtils
import com.login.base.utils.NetworkChangeReceiver
import com.login.base.utils.NetworkChangeReceiver.Companion.ACTION_LOCAL_CONNECTIVITY
import com.login.base.utils.NetworkChangeReceiver.Companion.CONNECTIVITY_CHANGE
import com.login.base.utils.NetworkUtils
import com.login.base.utils.SnackBarUtils
import org.koin.android.ext.android.inject
import java.util.*


abstract class BaseActivity : AppCompatActivity(), ConnectionBridge {

    private val mPreferenceManger by inject<PreferenceManager>()

    val stack = Stack<Fragment>()
    var ft: FragmentTransaction? = null

    private lateinit var localBroadcastManager: androidx.localbroadcastmanager.content.LocalBroadcastManager
    private lateinit var networkBroadcastReceiver: NetworkBroadcastReceiver
    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    protected abstract fun networkChangeState(boolean: Boolean)

    /**
     *To initialize the component you want to initialize before inflating layout
     */
    private fun preInflateInitialization() {
        /*1. Windows transition
        * 2. Permission utils initialization*/
    }


    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getRootView(): View


    abstract fun postDataBinding(binding: ViewDataBinding)

    /**
     *To initialize the activity components
     */
    protected abstract fun initializeComponent()

    // Common Handling of top bar for all fragments like header name, icon on top bar in case of moving to other fragment and coming back again
    abstract fun <T> setUpFragmentConfig(currentState: IFragmentState, keys: T?)

    override fun onCreate(savedInstanceState: Bundle?) {
        preInflateInitialization()
        val binding = DataBindingUtil.setContentView(this, getLayoutId()) as ViewDataBinding
        super.onCreate(savedInstanceState)
        postDataBinding(binding)
        initializeComponent()
        initFields()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun isNetworkAvailable(): Boolean {
        return NetworkUtils.isNetworkAvailable(this)
    }

    private inner class NetworkBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val activeConnection = intent
                .getBooleanExtra(NetworkChangeReceiver.EXTRA_IS_ACTIVE_CONNECTION, false)
            if (activeConnection) {
                networkChangeState(true)
                mPreferenceManger.setNetworkAvailable(true)
                checkShowingConnectionError()
            } else {
                networkChangeState(false)
                mPreferenceManger.setNetworkAvailable(false)
                checkShowingConnectionError()
            }
        }
    }

    override fun checkNetworkAvailableWithError(): Boolean {
        return if (!isNetworkAvailable()) {
            networkChangeState(false)
            mPreferenceManger.setNetworkAvailable(false)
            false
        } else {
            networkChangeState(true)
            mPreferenceManger.setNetworkAvailable(true)
            true
        }
    }

    protected fun checkShowingConnectionError() {
        if (!isNetworkAvailable()) {
            showSnackBar(R.string.error_no_internet)
        }
    }

    private fun initFields() {
        networkBroadcastReceiver = NetworkBroadcastReceiver()
        networkChangeReceiver = NetworkChangeReceiver()
        localBroadcastManager =
            androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this)
    }

    fun hideKeyboard() {
        CommonUtils.hideKeyboard(this)
    }


    //----------------------------- [End] ProgressDialog Utils -------------------------------------

    //----------------------------- [Start] Snack bar Utils -----------------------------------------
    fun showSnackBar(message: Int) {
        SnackBarUtils.show(getRootView(), message)
    }

    fun showSnackBar(message: String) {
        SnackBarUtils.show(getRootView(), message)
    }

    fun showSnackBar(message: Int, duration: Int) {
        SnackBarUtils.show(getRootView(), message, duration)
    }

    fun showSnackBar(message: String, duration: Int) {
        SnackBarUtils.show(getRootView(), message, duration)
    }

    override fun onResume() {
        super.onResume()
        registerBroadcastReceivers()
        checkShowingConnectionError()
    }

    override fun onPause() {
        super.onPause()
        unregisterBroadcastReceivers()
    }

    private var receiverNet = NetworkChangeReceiver()
    private fun registerBroadcastReceivers() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                registerReceiver(
                    receiverNet,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        } catch (e: Exception) {
//            LogUtils.e(e.message ?: "Error registering registerBroadcastReceivers")
        }

        localBroadcastManager.registerReceiver(
            networkBroadcastReceiver,
            IntentFilter(ACTION_LOCAL_CONNECTIVITY)
        )
        localBroadcastManager.registerReceiver(
            networkChangeReceiver,
            IntentFilter(CONNECTIVITY_CHANGE)
        )
    }

    private fun unregisterBroadcastReceivers() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                unregisterReceiver(receiverNet)
            }
        } catch (e: Exception) {
//            LogUtils.e(e.message ?: "Error unregistering unregisterBroadcastReceivers")
        }

        localBroadcastManager.unregisterReceiver(networkBroadcastReceiver)
        localBroadcastManager.unregisterReceiver(networkChangeReceiver)
    }
}