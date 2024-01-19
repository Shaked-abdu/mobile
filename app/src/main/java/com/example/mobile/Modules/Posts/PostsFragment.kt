package com.example.mobile.Modules.Posts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.Model.Model
import com.example.mobile.Model.Post
import com.example.mobile.Modules.Posts.Adapter.PostsRecyclerAdapter
import com.example.mobile.R
import com.example.mobile.databinding.FragmentPostsBinding

class PostsFragment : Fragment() {
    var postsRecyclerView: RecyclerView? = null
    var posts: List<Post>? = null
    var adapter: PostsRecyclerAdapter? = null
    var progressBar: ProgressBar? = null

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        val view = binding.root
        progressBar = binding.progressBar
        progressBar?.visibility = View.VISIBLE
        adapter = PostsRecyclerAdapter(posts)

        Model.instance.getAllPosts { posts ->
            this.posts = posts
            adapter?.posts = posts
            adapter?.notifyDataSetChanged()
            progressBar?.visibility = View.GONE
        }
        postsRecyclerView = binding.rvPostsFragmentList
        postsRecyclerView?.setHasFixedSize(true)

        postsRecyclerView?.layoutManager = LinearLayoutManager(context)
//        postsRecyclerView?.adapter = PostsRecyclerAdapter()

        adapter?.listener = object : PostsRecyclerViewActivity.OnItemClickListener {
            override fun onItemClick(post: Post?, position: Int) {
                Log.i("TAG", "Item clicked: $position")
                val post = posts?.get(position)
                post?.let {
                    val action = PostsFragmentDirections.actionPostsFragmentToBlueFragment(it.title)
                    Navigation.findNavController(view).navigate(action)
                }
            }

            override fun onPostClicked(post: Post?) {
                Log.i("TAG", "Post clicked: $post")
            }
        }

        postsRecyclerView?.adapter = adapter
//        val addPostButton: ImageButton = view.findViewById(R.id.ibtnAddPost)
//        val action =
//            Navigation.createNavigateOnClickListener(PostsFragmentDirections.actionGlobalAddPostFragment())
//        addPostButton.setOnClickListener(action)
        return view
    }

    override fun onResume() {
        super.onResume()
        Model.instance.getAllPosts { posts ->
            this.posts = posts
            adapter?.posts = posts
            adapter?.notifyDataSetChanged()
            progressBar?.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}