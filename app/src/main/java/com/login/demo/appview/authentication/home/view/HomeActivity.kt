package com.login.demo.appview.authentication.home.view

import android.view.View
import androidx.databinding.ViewDataBinding
import com.login.base.base.BaseActivity
import com.login.base.base.BaseFragment
import com.login.base.base.IFragmentState
import com.login.base.model.local.preference.PreferenceManager
import com.login.demo.R
import com.login.demo.utils.FragmentState
import com.login.demo.utils.extension.addFragment
import com.login.demo.utils.extension.getLastFragment
import com.login.demo.utils.extension.notifyFragment
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject

/**
 * Purpose  - Login demo.
 * @author  - gopal.gohel
 * Created  - 22/06/2022
 */
class HomeActivity : BaseActivity() {

    private val mPreferenceManger by inject<PreferenceManager>()

    override fun networkChangeState(boolean: Boolean) {
        if (!boolean) {
            showSnackBar(R.string.error_no_internet)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun getRootView(): View {
        return clRoot
    }

    override fun postDataBinding(binding: ViewDataBinding) {

    }

    override fun initializeComponent() {
        if (mPreferenceManger.isLoggedIn()) {
            // navigate to one fragment to another fragment
            addFragment<Any>(R.id.flContainer, FragmentState.F_DASHBOARD, null, true)
        } else {
            addFragment<Any>(R.id.flContainer, FragmentState.F_SIGNIN, null, true)
        }
    }

    // Handle back press event
    override fun onBackPressed() {
        hideKeyboard()
        val currentFragment: BaseFragment? = getLastFragment() as BaseFragment?
        val handled = currentFragment?.onBackPressed()
        handled?.let {
            if (!it) {
                when {
                    stack.size > 1 -> {
                        notifyFragment(true)
                    }
                    else -> super.onBackPressed()
                }
            }
        }
    }

    override fun <T> setUpFragmentConfig(currentState: IFragmentState, keys: T?) {

    }


}
