package hui.ait.finalproject.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hui.ait.finalproject.ActionsActivity
import hui.ait.finalproject.R
import hui.ait.finalproject.data.AppDatabase
import hui.ait.finalproject.data.Post
import kotlinx.android.synthetic.main.post.view.*

class PostAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder> {

    var posts = mutableListOf<Post>()

    private val context : Context

    constructor(context: Context, listPosts: List<Post>) : super() {
        this.context = context
        posts.addAll(listPosts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var postView = LayoutInflater.from(context).inflate(
            R.layout.post, parent, false
        )

        return ViewHolder(postView)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPost = posts[position]
        viewHolder.tvTitle.text = currentPost.postTitle
        viewHolder.tvBody.text = currentPost.postBody
        viewHolder.tvLocation.text = currentPost.postLocation

        viewHolder.btnDelete.setOnClickListener {
            deletePost(viewHolder.adapterPosition)
        }

        viewHolder.btnEdit.setOnClickListener {
            (context as ActionsActivity).showEditPostDialog(currentPost, viewHolder.adapterPosition)
        }
    }

    fun updatePost(post: Post) {
        Thread {
            AppDatabase.getInstance(context).postDao().updatePost(post)
        }.start()
    }

    fun updatePost(post: Post, editIndex: Int) {
        posts.set(editIndex, post)
        notifyItemChanged(editIndex)
    }

    fun addPost(post: Post) {
        posts.add(0, post)
        notifyItemInserted(0)
    }

    fun deletePost(deletePosition: Int) {
        Thread{
            AppDatabase.getInstance(context).postDao().deletePost(posts.get(deletePosition))

            (context as ActionsActivity).runOnUiThread {
                posts.removeAt(deletePosition)
                notifyItemRemoved(deletePosition)
            }
        }.start()
    }

    inner class ViewHolder(postView: View) : RecyclerView.ViewHolder(postView) {
        var tvTitle = postView.tvTitle
        var tvBody = postView.tvBody
        var tvLocation = postView.tvLocation
        var btnDelete = postView.btnDelete
        var btnEdit = postView.btnEdit
    }
}