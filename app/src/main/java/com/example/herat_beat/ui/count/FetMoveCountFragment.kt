package com.example.herat_beat.ui.count

import com.example.herat_beat.ui.ViewPageAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.herat_beat.R
import com.example.herat_beat.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout


//FM:fetal movements
@SuppressLint("InflateParams")
class FetMoveCountFragment : Fragment() {
    companion object {
        private const val TAG = "FetMoveCountFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val mFMCountBinding get() = _binding!!

    private val fragmentList by lazy {
        //懒加载，初始化,放在这里避免重复加载。
        ArrayList<Fragment>().apply{
            add(FetalMove())
            add(FetalMoveList())
        }
    }

    val customView: View by lazy {
        LayoutInflater.from(requireContext()).inflate(R.layout.tab_layout_custom_view, null)
    }
    val custom2: View by lazy {
        LayoutInflater.from(requireContext()).inflate(R.layout.taidongno_view, null)
    }
    val custom23: View by lazy {
        LayoutInflater.from(requireContext()).inflate(R.layout.excel_yes, null)
    }    //懒加载，初始化
    val custom22: View by lazy {
        LayoutInflater.from(requireContext()).inflate(R.layout.excelno, null)}

    private lateinit var fragmentAdapter: ViewPageAdapter


    //初始化视图的地方，页面切换不会再次调用，只有绑定时才会调用
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentHomeBinding.inflate(inflater,container,false)
        return mFMCountBinding.root
    }

    //切换时会被调用，放一些交互，拓展UI加载的逻辑
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化viewPager并加载进tabLayout:
        fragmentAdapter = ViewPageAdapter(childFragmentManager,fragmentList)
        mFMCountBinding.viewPager.adapter=fragmentAdapter
        mFMCountBinding.tabLayout.setupWithViewPager(mFMCountBinding.viewPager)
        //视图badge设置：
        mFMCountBinding.tabLayout.getTabAt(0)?.customView=customView
        mFMCountBinding.tabLayout.getTabAt(1)?.customView=custom22
        mFMCountBinding.viewPager.offscreenPageLimit=2
        mFMCountBinding.viewPager.currentItem = 0
        mFMCountBinding.tabLayout.
        addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 选中状态
                when (tab.position) {
                    0 -> {
                        mFMCountBinding.tabLayout.getTabAt(0)?.customView=customView
                    }
                    1 -> {
                        mFMCountBinding.tabLayout.getTabAt(1)?.customView=custom23
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // 未选中状态
                when (tab.position) {
                    0 ->mFMCountBinding.tabLayout.getTabAt(0)?.customView=custom2
                    1 -> {
                        mFMCountBinding.tabLayout.getTabAt(1)?.customView=custom22
                    } // 设置未选中图标
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // 重新选中状态
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Fragment destroyed")
        _binding = null
    }
}