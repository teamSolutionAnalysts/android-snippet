package com.login.demo.appview.country_picker.view

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.login.base.base.BaseDialogFragment
import com.login.base.base.IFragmentState
import com.login.base.utils.CommonUtils
import com.login.demo.R
import com.login.demo.appview.country_picker.adapter.CountryListAdapter
import com.login.demo.appview.country_picker.model.CountryItem
import com.login.demo.appview.authentication.home.view.HomeActivity
import com.login.demo.databinding.CountryPickerFragmentBinding
import com.login.demo.utils.ViewUtils
import kotlinx.android.synthetic.main.country_picker_fragment.*
import java.lang.reflect.Type


class CountryPickerFragment : BaseDialogFragment(), CountryListAdapter.ItemTouchListener {

    private lateinit var mFragmentBinding: CountryPickerFragmentBinding
    private var mAdapter: CountryListAdapter = CountryListAdapter(this)
    private var mList = arrayListOf<CountryItem>()
    private lateinit var mCountrySelectedListener: ViewUtils.CountrySelectedListener

    override fun getLayoutId(): Int {
        return R.layout.country_picker_fragment
    }

    override fun preDataBinding(arguments: Bundle?) {

    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        mFragmentBinding = binding as CountryPickerFragmentBinding
        mFragmentBinding.lifecycleOwner = this
        return mFragmentBinding
    }

    override fun initializeComponent(view: View?) {
        val jsonStr = CommonUtils.loadJSONFromAsset(
            (requireActivity() as HomeActivity),
            resources.getString(R.string.str_country_code)
        )
        val listType: Type = object : TypeToken<List<CountryItem?>?>() {}.type
        mList = Gson().fromJson(jsonStr, listType)
        mList = ArrayList(mList.sortedWith(compareBy({ it.name })))
        recCountry.adapter = mAdapter
        recCountry.isNestedScrollingEnabled = false
        mAdapter.setList(mList)
    }

    override fun <T> setUpFragmentConfig(currentState: IFragmentState, keys: T?) {

    }

    override fun pageVisible() {

    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = resources.getDimension(R.dimen._270sdp)
            val height = resources.getDimension(R.dimen._450sdp)
            it.window?.setLayout(width.toInt(), height.toInt())
        }
    }

    override fun onItemClick(position: Int, item: CountryItem) {
        mCountrySelectedListener.onCountrySelect(item.dial_code)
    }

    fun setCountrySelectedListener(countrySelectedListener: ViewUtils.CountrySelectedListener) {
        mCountrySelectedListener = countrySelectedListener
    }

}
