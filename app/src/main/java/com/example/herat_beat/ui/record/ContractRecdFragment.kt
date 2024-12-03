package com.example.herat_beat.ui.record

import com.example.herat_beat.ui.ViewPageAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.herat_beat.R
import com.google.android.material.tabs.TabLayout

//contraction record fragment
class ContractRecdFragment : Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var fragmentAdapter: ViewPageAdapter
    private lateinit var tabLayout: TabLayout

    private val fragmentList by lazy {
        ArrayList<Fragment>().apply {
            add(Contraction())
            add(ContractionList())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view=inflater.inflate(R.layout.fragment_dashboard,container,false)
        viewPager=view.findViewById(R.id.viewPager2)
        tabLayout=view.findViewById(R.id.tab_layout2)
        return view
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAdapter= ViewPageAdapter(childFragmentManager,fragmentList)
        viewPager.adapter=fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.offscreenPageLimit=2
        viewPager.currentItem = 0
        val customView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_gongsuo_yes, null)
        val custom2=    LayoutInflater.from(requireContext()).inflate(R.layout.tab_gongsuo_no, null)
        val custom22=    LayoutInflater.from(requireContext()).inflate(R.layout.excelno, null)
        val custom23=    LayoutInflater.from(requireContext()).inflate(R.layout.excel_red_yes, null)
        tabLayout.getTabAt(0)?.customView=customView
        tabLayout.getTabAt(1)?.customView=custom22
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // 选中状态
                when (tab.position) {
                    0 -> {
                        tabLayout.getTabAt(0)?.customView=customView

                    }
                    1 -> {
                        tabLayout.getTabAt(1)?.customView=custom23
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // 未选中状态
                when (tab.position) {
                    0 ->tabLayout.getTabAt(0)?.customView=custom2
                    1 -> {
                        tabLayout.getTabAt(1)?.customView=custom22
                    } // 设置未选中图标
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // 重新选中状态
            }
        })
    }
}