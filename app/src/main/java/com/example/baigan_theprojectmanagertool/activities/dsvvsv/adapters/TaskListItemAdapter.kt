package com.example.baigan_theprojectmanagertool.activities.dsvvsv.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.baigan_theprojectmanagertool.R
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.TaskModel
import kotlinx.android.synthetic.main.task_item.view.*

open class TaskListItemAdapter(private val context: Context, private var list:ArrayList<TaskModel>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        val layoutParameters = LinearLayout.LayoutParams((parent.width*0.9 ).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT) // LinearLayout.LayoutParams( width, height)
        layoutParameters.setMargins((15.toDp().toPx()), 0, (40.toDp()).toPx(), 0) // 15dp margin towards left and 40 to right

        view.layoutParams = layoutParameters

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            if (position == list.size -1){
                holder.itemView.btnAddTaskList.visibility = View.VISIBLE
                holder.itemView.llTaskItem.visibility = View.GONE

            }else{
                holder.itemView.btnAddTaskList.visibility = View.GONE
                holder.itemView.llTaskItem.visibility = View.VISIBLE
            }
        }

    }

    override fun getItemCount(): Int = list.size
    private fun Int.toDp() :Int = (this/Resources.getSystem().displayMetrics.density).toInt()
    private fun Int.toPx() :Int = (this*Resources.getSystem().displayMetrics.density).toInt()

}