package com.login.demo.utils

import com.login.base.base.BaseFragment
import com.login.base.base.IFragmentState
import com.login.demo.appview.authentication.login.view.LoginFragment
import com.login.demo.appview.authentication.register.view.RegisterFragment
import com.login.demo.appview.dashboard.view.DashboardFragment

enum class FragmentState(var fragment: Class<out BaseFragment>) :
    IFragmentState {

    F_SIGNIN(LoginFragment::class.java),
    F_DASHBOARD(DashboardFragment::class.java),
    F_REGISTER(RegisterFragment::class.java);


    companion object {
        // To get AppFragmentState  enum from class name
        fun getValue(value: Class<*>): FragmentState {
            return values().firstOrNull { it.fragment == value }
                ?: F_SIGNIN
        }
    }
}