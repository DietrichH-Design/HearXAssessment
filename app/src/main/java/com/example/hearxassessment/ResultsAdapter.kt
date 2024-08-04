package com.example.hearxassessment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.hearxassessment.api.models.TestScore

class ResultsAdapter(private val context: Context, private val dataSource: List<TestScore>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_item_results, parent, false)

        val scoreTextView = rowView.findViewById<TextView>(R.id.scoreTextView)
        val score = getItem(position) as TestScore

        scoreTextView.text = "Score: ${score.score}"

        return rowView
    }
}
