package com.example.genos.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.genos.R
import com.example.genos.model.Examination
import com.example.genos.model.Question
import genos.ui.BaseAdapter
import genos.ui.fragment.generic.Detail
import genos.ui.viewholder.Holder

class Exam : Detail<Examination>(R.layout.fragment_exam) {
    lateinit var adapter: BaseAdapter<Question, Holder>
    lateinit var viewPager: ViewPager2

    override fun onCreate(intent: Intent, id: String) {
//        call = API.userDetail(id)
        val questions = arrayListOf(
                Question(1, "多少度会中暑", "m", arrayListOf("38 度", "39 度"), arrayListOf("0", "1"), "习题解析"),
                Question(1, "肾炎主要感染途径是", "m", arrayListOf("直接感染", "呼吸系统感染"), arrayListOf("0", "1"), "习题解析")
        )
        data = Examination(1, "基础知识", questions)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.view_pager)
//        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
//            @SuppressLint("SetTextI18n")
//            override fun onPageSelected(position: Int) {
//                header.text = "${data?.title} ${position + 1} / ${adapter.itemCount}"
//            }
//        })
        adapter = object : BaseAdapter<Question, Holder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.pager_item_question, parent, false))

            override fun onBindViewHolder(holder: Holder, position: Int) {
                val item = getItem(position)
                with(holder) {
                    setText(R.id.header, "${data?.title} ${position + 1} / $itemCount")
                    title?.text = item.title
                    title?.setOnClickListener { viewPager.setCurrentItem(position + 1, true) }
                    setText(R.id.options, item.options.toString())
                    setText(R.id.answers, item.answers.toString())
                    setText(R.id.analysis, item.analysis)
                }
            }
        }
        viewPager.adapter = adapter
        adapter.submitList(data!!.questions)
    }

    override fun onDisplay(data: Examination) {
        adapter.submitList(data.questions)
    }
}
