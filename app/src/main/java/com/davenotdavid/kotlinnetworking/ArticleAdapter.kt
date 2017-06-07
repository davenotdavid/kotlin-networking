package com.davenotdavid.kotlinnetworking

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ArticleAdapter(private val mListItemClickListener: ListItemClickListener,
                     private val mArticleData: List<Article>) :
        RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    interface ListItemClickListener {
        fun onListItemClick(article: Article)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArticleViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.mNewsTitleTV.text = mArticleData[position].mTitle
    }

    override fun getItemCount(): Int {
        return mArticleData.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
        val mNewsTitleTV: TextView = itemView.findViewById(R.id.title) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mListItemClickListener.onListItemClick(mArticleData[adapterPosition])
        }
    }
}
