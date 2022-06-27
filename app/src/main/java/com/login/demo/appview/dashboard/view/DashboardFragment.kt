package com.login.demo.appview.dashboard.view

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.login.base.base.BaseFragment
import com.login.base.base.IFragmentState
import com.login.base.model.local.preference.PreferenceManager
import com.login.demo.R
import com.login.demo.appview.authentication.home.view.HomeActivity
import com.login.demo.databinding.DashboardFragmentBinding
import com.login.demo.utils.extension.popFragment
import kotlinx.android.synthetic.main.dashboard_fragment.*
import org.koin.android.ext.android.inject


class DashboardFragment : BaseFragment() {

    private lateinit var mFragmentBinding: DashboardFragmentBinding
    private val mPreferenceManger by inject<PreferenceManager>()


    override fun getLayoutId(): Int {
        return R.layout.dashboard_fragment
    }

    override fun preDataBinding(arguments: Bundle?) {

    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        mFragmentBinding = binding as DashboardFragmentBinding
        mFragmentBinding.lifecycleOwner = this
        return mFragmentBinding
    }


    override fun initializeComponent(view: View?) {
        btnLogout.setOnClickListener {
            mPreferenceManger.sharedPreferences.edit().clear().apply()
            // Handle fragment black button
            (requireActivity() as HomeActivity).popFragment(
                1,
                null,
                false
            )
        }
    }

    override fun <T> setUpFragmentConfig(currentState: IFragmentState, keys: T?) {

    }

    override fun pageVisible() {

    }


}
