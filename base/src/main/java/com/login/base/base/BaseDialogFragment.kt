package com.login.base.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.login.base.utils.Constant
import java.util.*


abstract class BaseDialogFragment : DialogFragment(), View.OnClickListener {
    val stack = Stack<Fragment>()
    var ft: FragmentTransaction? = null

    private var lastClickTime: Long = 0//To prevent double click

    protected var mBaseActivity: BaseActivity? = null

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * To perform operation before data binding like initialize view model, get extras etc
     */
    abstract fun preDataBinding(arguments: Bundle?)

    /**
     * To perform operation after data binding like setting view model and lifecycle owner
     */
    abstract fun postDataBinding(binding: ViewDataBinding): ViewDataBinding

    /**
     *To initialize the fragments components
     */
    protected abstract fun initializeComponent(view: View?)

    // Common Handling of top bar for all fragments like header name, icon on top bar in case of moving to other fragment and coming back again
    abstract fun <T> setUpFragmentConfig(currentState: IFragmentState, keys: T?)

    /**
     *This will be called as soon as fragment visible to
     */
    protected abstract fun pageVisible()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mBaseActivity = context as BaseActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        preDataBinding(arguments)
        var mViewDataBinding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, getLayoutId(), container, false)
        mViewDataBinding = postDataBinding(mViewDataBinding)
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initializeComponent(view)
    }

    /**
     * @param isVisibleToUser
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            pageVisible()
        }
    }

    /**
     * Could handle back press.
     * @return true if back press was handled
     */
    open fun onBackPressed(): Boolean {
        return false
    }

    /**
     * Logic to Prevent the Launch of the Fragment Twice if User makes the Tap(Click) very Fast.
     */
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < Constant.MAX_CLICK_INTERVAL) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
    }

    fun after(delay: Long, process: () -> Unit) {
        Handler().postDelayed({
            process()
        }, delay)
    }

    fun hideKeyboard() {
        mBaseActivity?.hideKeyboard()
    }

    fun showSnackBar(message: String) {
        mBaseActivity?.showSnackBar(message)
    }

    fun showSnackBar(message: Int, duration: Int) {
        mBaseActivity?.showSnackBar(message, duration)
    }
}