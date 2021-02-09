/*
 * Copyright (C) 2016 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flx.features.start.view

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.rd.PageIndicatorView
import com.flx.app.databinding.StartActivityBinding

class StartActivity : AppCompatActivity() {

    lateinit var negativeButton: Button
    lateinit var positiveButton: Button

    private lateinit var pagerAdapter: StartFragmentsPagerAdapter
    private lateinit var binding: StartActivityBinding
    private lateinit var pageIndicator: PageIndicatorView
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = StartActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        negativeButton = binding.buttonNegative
        positiveButton = binding.buttonPositive
        viewPager = binding.viewPagerFragments

        pagerAdapter = StartFragmentsPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        pageIndicator = binding.pageIndicatorView
        pageIndicator.count = pagerAdapter.count

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                pageIndicator.selection = position
            }
        })
    }

    fun gotoNext() {
        viewPager.currentItem = viewPager.currentItem + 1
    }
}
