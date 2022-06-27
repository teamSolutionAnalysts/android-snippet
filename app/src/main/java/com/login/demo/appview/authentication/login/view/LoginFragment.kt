package com.login.demo.appview.authentication.login.view

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.login.base.base.BaseFragment
import com.login.base.base.IFragmentState
import com.login.base.model.Resource
import com.login.base.model.local.preference.PreferenceManager
import com.login.base.utils.CommonUtils
import com.login.demo.R
import com.login.demo.appview.authentication.login.viewmodel.LoginViewModel
import com.login.demo.appview.authentication.home.view.HomeActivity
import com.login.demo.customview.CustomDialog
import com.login.demo.databinding.FragmentLoginBinding
import com.login.demo.utils.*
import com.login.demo.utils.extension.addFragment
import kotlinx.android.synthetic.main.custom_edittext.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.toolbar_with_title_lrarrow.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment() {

    private lateinit var mFragmentBinding: FragmentLoginBinding
    private val mPreferenceManger by inject<PreferenceManager>()
    private val mViewModel by viewModel<LoginViewModel>()


    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun preDataBinding(arguments: Bundle?) {

    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        mFragmentBinding = binding as FragmentLoginBinding
        mFragmentBinding.viewModel = mViewModel
        mFragmentBinding.lifecycleOwner = this
        return mFragmentBinding
    }

    override fun initializeComponent(view: View?) {
        toolbarTitleTxt.visibility = View.GONE
        toolbarImgBack.setOnClickListener {

        }

        customViewClickListeners()
        // Handle spinner click event
        customMobileNumber?.txtSpinner?.setOnClickListener {
                ViewUtils.showCountryPicker(requireContext(),
                    object : ViewUtils.CountrySelectedListener {
                        override fun onCountrySelect(countryCode: String) {
                            mViewModel.countryCode.set(countryCode)
                        }
                    })
        }

        mViewModel.notifyClick.observe(this, Observer {
            when (it) {
                R.id.btnLogin -> {
                    if ((requireActivity() as HomeActivity).isNetworkAvailable()) {
                        if (validateLogInFields()) {
                            mViewModel.apiLogin()
                        }
                    } else {
                        (requireActivity() as HomeActivity).showSnackBar(R.string.error_no_internet)
                    }
                }
                R.id.btnRegister -> {
                        navigateToRegister()
                }
            }
        })



        mViewModel.resultLiveData.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    ProgressUtils.closeProgressIndicator()
                    mPreferenceManger.setLoggedIn(true)
                    (requireActivity() as HomeActivity).addFragment<Any>(
                        R.id.flContainer,
                        FragmentState.F_DASHBOARD,
                        null,
                        true
                    )
                }

                is Resource.Error -> {
                    //handle progress visibility
                    ProgressUtils.closeProgressIndicator()
                    it.error.message?.let { errorMsg ->
                        ViewUtils.showCustomDialog(requireActivity(),
                            resources.getString(R.string.app_name),
                            errorMsg,
                            resources.getString(R.string.str_ok),
                            null,
                            object : CustomDialog.ButtonClickListener {
                                override fun onPositiveClick(dialog: CustomDialog) {
                                    dialog.dismiss()
                                }

                                override fun onNegativeClick(dialog: CustomDialog) {

                                }
                            })
                    }
                }

                is Resource.Loading -> {
                    ProgressUtils.showProgressIndicator(context)
                }
            }
        })
    }

    private fun navigateToRegister() {
        (requireActivity() as HomeActivity).addFragment<Any>(
            R.id.flContainer,
            FragmentState.F_REGISTER,
            null,
            true
        )

    }


    private fun validateLogInFields(): Boolean {
        if (mViewModel.logInFields.mobile.trim().isEmpty()) {
            mViewModel.errMobile.set(resources.getString(R.string.error_enter_mobile))
            mViewModel.isMobileValid = false
        } else if (!CommonUtils.isValidPhoneNumber(
                mViewModel.utilPhoneNumberUtil!!,
                mViewModel.countryCode.get() + mViewModel.logInFields.mobile
            )
        ) {
            mViewModel.errMobile.set(resources.getString(R.string.error_invalid_mobile_num))
            mViewModel.isMobileValid = false
        } else {
            mViewModel.errMobile.set("")
            mViewModel.isMobileValid = true
        }

        if (mViewModel.logInFields.password.trim().isEmpty()) {
            mViewModel.errPassword.set(resources.getString(R.string.error_enter_password))
            mViewModel.isPasswordValid = false
        } else {
            mViewModel.errPassword.set("")
            mViewModel.isPasswordValid = true
        }

        return mViewModel.isMobileValid && mViewModel.isPasswordValid
    }


    private fun customViewClickListeners() {
        customPassword.iconRight.setOnClickListener {
            if (mViewModel?.isPasswordVisible.value == 1) {
                mViewModel?.isPasswordVisible.value = 0
            } else {
                mViewModel?.isPasswordVisible.value = 1
            }
        }
    }



    override fun <T> setUpFragmentConfig(currentState: IFragmentState, keys: T?) {

    }

    override fun pageVisible() {

    }



}
