package com.davenotdavid.kotlinnetworking

import android.app.LoaderManager
import android.content.AsyncTaskLoader
import android.content.Context
import android.content.Intent
import android.content.Loader
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_article.*

class ArticleActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<List<Article>>,
        ArticleAdapter.ListItemClickListener {

    private val mArticleLoaderId = 1
    private val mNewsEndpointUrl = "https://newsapi.org/v1/articles"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        supportActionBar?.title = getString(R.string.news_source_title)

        rv_articles.layoutManager = LinearLayoutManager(this)
        rv_articles.setHasFixedSize(true)

        runLoaders()
    }

    private fun runLoaders() {
        progress_bar.visibility = View.VISIBLE

        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            loaderManager.initLoader(mArticleLoaderId, null, this)
        } else {
            progress_bar.visibility = View.INVISIBLE

            Toast.makeText(this, R.string.toast_no_int_connection, Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Article>> {
        val baseUri: Uri? = Uri.parse(mNewsEndpointUrl)
        val uriBuilder: Uri.Builder? = baseUri?.buildUpon()

        uriBuilder?.appendQueryParameter(
                getString(R.string.query_param_source),
                getString(R.string.news_source))
        uriBuilder?.appendQueryParameter(
                getString(R.string.query_param_apikey),
                getString(R.string.news_api_key))

        return object : AsyncTaskLoader<List<Article>>(this) {

            override fun onStartLoading() {
                forceLoad()
            }

            override fun loadInBackground(): List<Article>? {
                return QueryUtils.fetchArticleData(uriBuilder.toString())
            }
        }
    }

    override fun onLoadFinished(loader: Loader<List<Article>>?, articleList: List<Article>) {
        progress_bar.visibility = View.INVISIBLE

        if (!articleList.isEmpty() && articleList != null) {
            rv_articles.adapter = ArticleAdapter(this, articleList)
        }
    }

    override fun onLoaderReset(loader: Loader<List<Article>>?) {
        // TODO: Implement code here such as clearing the adapter's data
    }

    override fun onListItemClick(article: Article) {
        val articleUrl = Uri.parse(article.mUrl)
        val browserIntent = Intent(Intent.ACTION_VIEW, articleUrl)
        startActivity(browserIntent)
    }
}