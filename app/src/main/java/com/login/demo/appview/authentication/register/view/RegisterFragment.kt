package com.login.demo.appview.authentication.register.view

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.login.base.base.BaseFragment
import com.login.base.base.IFragmentState
import com.login.base.model.Resource
import com.login.base.model.local.preference.PreferenceManager
import com.login.base.utils.CommonUtils
import com.login.base.utils.CommonUtils.isValidPassword
import com.login.demo.R
import com.login.demo.appview.authentication.register.viewmodel.RegisterViewModel
import com.login.demo.appview.authentication.home.view.HomeActivity
import com.login.demo.customview.CustomDialog
import com.login.demo.databinding.RegisterFragmentBinding
import com.login.demo.utils.*
import com.login.demo.utils.extension.addFragment
import kotlinx.android.synthetic.main.custom_edittext.view.*
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.android.synthetic.main.toolbar_with_title_lrarrow.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : BaseFragment() {

    private lateinit var mFragmentBinding: RegisterFragmentBinding
    private val mPreferenceManger by inject<PreferenceManager>()
    private val mViewModel by viewModel<RegisterViewModel>()


    override fun getLayoutId(): Int {
        return R.layout.register_fragment
    }

    override fun preDataBinding(arguments: Bundle?) {

    }

    override fun postDataBinding(binding: ViewDataBinding): RegisterFragmentBinding {
        mFragmentBinding = binding as RegisterFragmentBinding
        mFragmentBinding.viewModel = mViewModel
        mFragmentBinding.lifecycleOwner = this
        return mFragmentBinding
    }

    override fun initializeComponent(view: View?) {

        toolbarTitleTxt.visibility = View.GONE
        toolbarImgBack.setOnClickListener {

        }
        customClickListener()
        mViewModel.formField.title = resources.getString(R.string.str_title)
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



        mViewModel.notifyClick.observe(this, Observer {
            when (it) {
                R.id.btnRegister -> {
                    hideKeyboard()
                    if ((requireActivity() as HomeActivity).isNetworkAvailable()) {
                        if (validateFields()) {
                            mViewModel.apiSignUp()
                        }
                    } else {
                        (requireActivity() as HomeActivity).showSnackBar(R.string.error_no_internet)
                    }
                }
            }
        })

        listenCustomViewClick()
        setDefaultCountryCode()
    }




    private fun listenCustomViewClick() {
        customTitle?.txtSpinner?.setOnClickListener {
            ViewUtils.showTitlePopUp(it, object : ViewUtils.PopUpItemClickListener {
                override fun selectedItem(selectedItem: String) {
                    mViewModel.formField.apply {
                        this.title = selectedItem
                        mViewModel.engTitle = selectedItem
                        this.notifyChange()
                        mViewModel.errTitle.set("")
                    }
                }
            })
        }

        // click event for country code
        customMobileNumber?.txtSpinner?.setOnClickListener {
            ViewUtils.showCountryPicker(requireContext(),
                object : ViewUtils.CountrySelectedListener {
                    override fun onCountrySelect(countryCode: String) {
                        mViewModel.countryCode.set(countryCode)
                    }
                })
        }

    }


    private fun validateFields(): Boolean {
        if (mViewModel.formField.title.equals(resources.getString(R.string.str_title))) {
            mViewModel.errTitle.set(resources.getString(R.string.error_select_title))
            mViewModel.isTitleValid = false
        } else {
            mViewModel.errTitle.set("")
            mViewModel.isTitleValid = true
        }

        if (mViewModel.formField.firstName.trim().isEmpty()) {
            mViewModel.errFirstName.set(resources.getString(R.string.error_enter_first_name))
            mViewModel.isFirsNameValid = false
        } else {
            mViewModel.errFirstName.set("")
            mViewModel.isFirsNameValid = true
        }

        if (mViewModel.formField.lastName.trim().isEmpty()) {
            mViewModel.errLastName.set(resources.getString(R.string.error_enter_last_name))
            mViewModel.isLastNameValid = false
        } else {
            mViewModel.errLastName.set("")
            mViewModel.isLastNameValid = true
        }

        if (mViewModel.formField.mobile.trim().isEmpty()) {
            mViewModel.errMobile.set(resources.getString(R.string.error_enter_mobile))
            mViewModel.isMobileValid = false
        } else if (!CommonUtils.isValidPhoneNumber(
                mViewModel.utilPhoneNumberUtil!!,
                mViewModel.countryCode.get() + mViewModel.formField.mobile
            )
        ) {
            mViewModel.errMobile.set(resources.getString(R.string.error_invalid_mobile_num))
            mViewModel.isMobileValid = false
        } else {
            mViewModel.errMobile.set("")
            mViewModel.isMobileValid = true
        }

        if (mViewModel.formField.email.trim().isEmpty()) {
            mViewModel.errEmail.set(resources.getString(R.string.error_enter_email))
            mViewModel.isEmailValid = false
        } else if (!CommonUtils.isValidEmail(mViewModel.formField.email.trim())) {
            mViewModel.errEmail.set(resources.getString(R.string.error_enter_valid_email))
            mViewModel.isEmailValid = false
        } else {
            mViewModel.errEmail.set("")
            mViewModel.isEmailValid = true
        }

        if (mViewModel.formField.password.trim().isEmpty()) {
            mViewModel.errPassword.set(resources.getString(R.string.error_enter_password))
            mViewModel.isPasswordValid = false
        } else if (mViewModel.formField.password.trim().length < AppConstant.PASSWORD_LENGTH) {
            mViewModel.errPassword.set(
                String.format(
                    resources.getString(R.string.error_invalid_n_password_length),
                    AppConstant.PASSWORD_LENGTH
                )
            )
            mViewModel.isPasswordValid = false
        } else if (!isValidPassword(mViewModel.formField.password)) {
            mViewModel.errPassword.set(resources.getString(R.string.str_password_instruction))
            mViewModel.isPasswordValid = false
        } else {
            mViewModel.errPassword.set("")
            mViewModel.isPasswordValid = true
        }

        if (mViewModel.formField.confirmPassword.trim().isEmpty()) {
            mViewModel.errConfirmPassword.set(resources.getString(R.string.error_enter_confirm_password))
            mViewModel.isConfirmPassValid = false
        } else if (!mViewModel.formField.password.trim().equals(mViewModel.formField.confirmPassword.trim())) {
            mViewModel.errConfirmPassword.set(resources.getString(R.string.error_password_does_not_match))
            mViewModel.isConfirmPassValid = false
        } else {
            mViewModel.errConfirmPassword.set("")
            mViewModel.isConfirmPassValid = true
        }

        return mViewModel.isTitleValid && mViewModel.isFirsNameValid && mViewModel.isLastNameValid && mViewModel.isMobileValid && mViewModel.isEmailValid && mViewModel.isPasswordValid && mViewModel.isConfirmPassValid
    }

    private fun setDefaultCountryCode() {
        mViewModel.countryCode.set(AppConstant.DEFAULT_COUNTRY_CODE)
    }

    override fun <T> setUpFragmentConfig(currentState: IFragmentState, keys: T?) {

    }

    private fun customClickListener() {

        customPassword.iconRight.setOnClickListener {
            if (mViewModel?.isPasswordVisible.value == 1) {
                mViewModel?.isPasswordVisible.value = 0
            } else {
                mViewModel?.isPasswordVisible.value = 1
            }
        }

    }

    override fun pageVisible() {

    }

    override fun onResume() {
        super.onResume()
        activity?.window
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
}
