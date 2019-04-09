package com.study.cube.yeti.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.study.cube.yeti.R
import com.study.cube.yeti.eventBus.BusProvider
import com.study.cube.yeti.eventBus.EventKind
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.EventBus

class MainFragment : Fragment() {
    var className : String = MainFragment::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        // 신규프로젝트 생성
        button_project_new.setOnClickListener {
            EventBus.getDefault().post(BusProvider(ProjectNewStep1Fragment::class.java.name, EventKind.BIND_FRAGMENT, null))
        }

        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        private const val KEY_TITLE = "MainFragment"

        fun newInstance(title: String) =
                MainFragment().apply {
                    val args = Bundle()
                    args.putString(KEY_TITLE, title)
                    arguments = args
                }
    }
}