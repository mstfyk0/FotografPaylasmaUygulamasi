package com.mustafayigitkarakoca.fotografpaylasmafirebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler.view.*

class RecyclerAdapter(val postList : ArrayList<Post>): RecyclerView.Adapter<RecyclerAdapter.PostHolder>() {
    //
    class PostHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    }
    ////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler,parent,false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemView.textViewRecyclerViewEmail.text = postList[position].kullaniciEmail
        holder.itemView.textViewRecyclerViewYorum.text=postList[position].kullaniciYorum
        Picasso.get().load(postList[position].gorselUrl).into(holder.itemView.imageViewRecyclerView)


    }

    override fun getItemCount(): Int {
        return postList.size

    }

}