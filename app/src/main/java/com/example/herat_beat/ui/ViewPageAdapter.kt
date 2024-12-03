package com.example.herat_beat.ui
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewPageAdapter(
    fragmentManager: FragmentManager?,
    fragments: ArrayList<Fragment>,

) :
    FragmentPagerAdapter(fragmentManager!!) {
    //各导航的Fragment
    private val mFragmentList: List<Fragment>

    //导航的标题

    init {
        mFragmentList = fragments
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }


}