package com.study.cube.yeti.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.study.cube.yeti.R

class NoticeItemFragment : Fragment() {
    var fragmentTitle : String? = null
    var className : String = NoticeItemFragment::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_notice_item, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        private const val KEY_TITLE = "NoticeItemFragment"

        fun newInstance(title: String) =
                NoticeItemFragment().apply {
                    fragmentTitle = title

                    val args = Bundle()
                    args.putString(KEY_TITLE, title)
                    arguments = args
                }
    }
}