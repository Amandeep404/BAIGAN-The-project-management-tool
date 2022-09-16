package com.example.baigan_theprojectmanagertool.activities.dsvvsv.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.baigan_theprojectmanagertool.R
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.activities.TaskListActivity
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.TaskModel
import kotlinx.android.synthetic.main.task_item.view.*

open class TaskListItemAdapter(private val context: Context, private var list:ArrayList<TaskModel>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        val layoutParameters = LinearLayout.LayoutParams((parent.width*0.87 ).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT) // LinearLayout.LayoutParams( width, height)
        layoutParameters.setMargins((15.toDp().toPx()), 0, (25.toDp()).toPx(), 0) // 15dp margin towards left and 40 to right

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

            holder.itemView.tvTaskListTitle.text = model.title
            holder.itemView.btnAddTaskList.setOnClickListener{
                holder.itemView.btnAddTaskList.visibility = View.GONE
                holder.itemView.cvAddTaskListName.visibility = View.VISIBLE
            }
            holder.itemView.ibDeleteListName.setOnClickListener{
                holder.itemView.btnAddTaskList.visibility = View.VISIBLE
                holder.itemView.cvAddTaskListName.visibility = View.GONE
            }
            holder.itemView.ibDoneListName.setOnClickListener{
                val listName = holder.itemView.etTaskListName.text.toString()
                if (listName.isNotEmpty()){
                    if (context is TaskListActivity){
                        context.createTaskList(listName)
                    }
                }else{
                    Toast.makeText(context, "Please Enter a List Name", Toast.LENGTH_SHORT).show()
                }
            }
            holder.itemView.ibTitleEdit.setOnClickListener {
                holder.itemView.tvTaskListTitle.text = model.title
                holder.itemView.llTitleView.visibility = View.GONE
                holder.itemView.cvEditTaskListName.visibility = View.VISIBLE
            }
            holder.itemView.ibCloseEditableView.setOnClickListener {
                holder.itemView.llTitleView.visibility = View.VISIBLE
                holder.itemView.cvEditTaskListName.visibility = View.GONE
            }
            holder.itemView.ibDoneEditableView.setOnClickListener{
                val listName = holder.itemView.etEditableView.text.toString()

                if (listName.isNotEmpty()){
                    if (context is TaskListActivity){
                        context.updateTaskList(position, listName, model)
                    }
                }else{
                    Toast.makeText(context, "Please Enter a List Name", Toast.LENGTH_SHORT).show()
                }
            }
            holder.itemView.ibDeleteList.setOnClickListener{
                alertDialogForDeleteList(position, model.title )
            }
        }

    }

    fun alertDialogForDeleteList(position: Int,title:String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("ALERT")
        builder.setMessage("Are you sure you want to delete the list $title")
            .setIcon(R.drawable.ic_baseline_error_outline_24)
            .setPositiveButton("Yes") { dialoginterface, which ->
                dialoginterface.dismiss()

                if (context is TaskListActivity){
                    context.deleteTaskList(position)
                }
            }
            .setNegativeButton("No"){dialogInterface , which->
                dialogInterface.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun getItemCount(): Int = list.size
    private fun Int.toDp() :Int = (this/Resources.getSystem().displayMetrics.density).toInt()
    private fun Int.toPx() :Int = (this*Resources.getSystem().displayMetrics.density).toInt()

}