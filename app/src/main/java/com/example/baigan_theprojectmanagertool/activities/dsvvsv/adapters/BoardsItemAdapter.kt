package com.example.baigan_theprojectmanagertool.activities.dsvvsv.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baigan_theprojectmanagertool.R
import com.example.baigan_theprojectmanagertool.activities.dsvvsv.models.Board
import kotlinx.android.synthetic.main.board_item_list.view.*

open class BoardsItemAdapter(private val context: Context, private var list: ArrayList<Board>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener : OnClickListener? =null //onclick

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.board_item_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is ViewHolder){
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_person_grey_24)
                .into(holder.itemView.ivBoardImage)

            holder.itemView.tvBoardName.text= model.name
            holder.itemView.tvBoardCreatorName.text = "Created By: ${model.createdBy}"

            //onclick
            holder.itemView.setOnClickListener{
                if (onClickListener!=null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }

    }
//onclick
    interface OnClickListener{
        fun onClick(position: Int, model:Board){

        }
    }
    //onclick
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun getItemCount() = list.size
}