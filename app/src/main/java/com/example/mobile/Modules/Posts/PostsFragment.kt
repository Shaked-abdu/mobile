package com.example.mobile.Modules.Posts

import android.content.Context
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.Model.Model
import com.example.mobile.Model.Post
import com.example.mobile.Modules.Posts.Adapter.PostsRecyclerAdapter
import com.example.mobile.R
import com.example.mobile.base.MyApplication
import com.example.mobile.databinding.FragmentPostsBinding
import com.google.firebase.auth.FirebaseAuth
import org.checkerframework.checker.units.qual.Current
import kotlin.concurrent.thread

class PostsFragment : Fragment() {
    var postsRecyclerView: RecyclerView? = null
    var adapter: PostsRecyclerAdapter? = null
    var adapterMyPosts: PostsRecyclerAdapter? = null
    var progressBar: ProgressBar? = null

    val showMyPostsButton : Button? = null
    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PostsViewModel
    private val userId: String = signedInUserUid()
    private var showAll: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this)[PostsViewModel::class.java]
        progressBar = binding.progressBar
        progressBar?.visibility = View.VISIBLE

        adapter = PostsRecyclerAdapter(viewModel.posts?.value)
        viewModel.posts = Model.instance.getAllPosts()

        adapterMyPosts = PostsRecyclerAdapter(viewModel.myPosts?.value)
        viewModel.myPosts = Model.instance.getMyPosts(userId)

        postsRecyclerView = binding.rvPostsFragmentList
        postsRecyclerView?.setHasFixedSize(true)
        postsRecyclerView?.layoutManager = LinearLayoutManager(context)

        binding.btnPostsShowAll.setOnClickListener (::showAllClicked)


        adapter?.listener = object : PostsRecyclerViewActivity.OnItemClickListener {
            override fun onItemClick(post: Post?, position: Int) {
                Log.i("TAG", "Item clicked: $position")
                val post = viewModel.posts?.value?.get(position)
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

        viewModel.posts?.observe(viewLifecycleOwner) {
            adapter?.posts = it
            adapter?.notifyDataSetChanged()
            progressBar?.visibility = View.GONE
        }
        viewModel.myPosts?.observe(viewLifecycleOwner) {
            adapterMyPosts?.posts = it
            adapterMyPosts?.notifyDataSetChanged()
            progressBar?.visibility = View.GONE
        }

        binding.pullToRefresh.setOnRefreshListener {
            reloadData()

        }

        Model.instance.postsListLoadingState.observe(viewLifecycleOwner) { state ->
            binding.pullToRefresh.isRefreshing = state == Model.LoadingState.LOADING
        }




        return view
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }

    private fun reloadData() {
        progressBar?.visibility = View.VISIBLE
        Model.instance.refreshAllPosts()
        progressBar?.visibility = View.GONE
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun signedInUserUid(): String {
        return MyApplication.Globals.appContext
            ?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            ?.getString("userUid", null) ?: ""
    }

    private fun showAllClicked(view: View) {

            showAll = !showAll
            binding.btnPostsShowAll.text = if (showAll) "My Reports" else "All Reports"

        if (!showAll){

            viewModel.myPosts = Model.instance.getMyPosts(userId)
            adapterMyPosts = PostsRecyclerAdapter(viewModel.myPosts?.value)
            postsRecyclerView?.adapter = adapterMyPosts
            Thread.sleep(1000)

        }else {
            viewModel.posts = Model.instance.getAllPosts()
            adapter = PostsRecyclerAdapter(viewModel.posts?.value)
            postsRecyclerView?.adapter = adapter
            Thread.sleep(1000)
        }
            //  postsRecyclerView = binding.rvPostsFragmentList
            //  postsRecyclerView?.setHasFixedSize(true)
            //  postsRecyclerView?.layoutManager = LinearLayoutManager(context)

    }


    //progressBar?.visibility = View.VISIBLE
    //progressBar?.visibility = View.GONE

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.profileFragment -> {
//                Log.i("TAG", "onOptionsItemSelected userId: $userId")
//                val action = PostsFragmentDirections.actionPostsFragmentToProfileFragment(userId)
//                Navigation.findNavController(requireView()).navigate(action)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}