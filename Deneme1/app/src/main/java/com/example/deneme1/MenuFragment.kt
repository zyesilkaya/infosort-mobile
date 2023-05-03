package com.example.deneme1

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_menu.*
import java.net.URISyntaxException


class MenuFragment : Fragment() {

    private val URL = "https://www.youtube.com/"
    private var isAlreadyCreated = false
    lateinit var webView : WebView
    //time passed between two back presses.
    private val TIME_INTERVAL = 200
    // variable to keep track of last back press
    private var mBackPressed: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        webView = view.findViewById<WebView>(R.id.webView2)//bindingle yap
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(false)
        webView.setWebViewClient(Callback())
        webView.loadUrl(URL)

        webView!!.requestFocus()
        webView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK
                && event.action == MotionEvent.ACTION_UP
            ) {
                if(webView.canGoBack()) {
                    //webview içinde önceki sayfaya gidecek
                    webView.goBack()
                    return@OnKeyListener true
                } else{
                    if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                        // order fragmentına geçicek
                        return@OnKeyListener false
                    }
                    else {
                        //webviewda geri gidecek sayfa yoksa 2 kez tıklaması için uyarı verecek
                        Toast.makeText(context, " Double Tap back button to exit the menu", Toast.LENGTH_SHORT).show();
                        mBackPressed = System.currentTimeMillis();
                        return@OnKeyListener true
                    }
                }
            }
            return@OnKeyListener false
        })
        return view
    }

    private class Callback : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

            // Open some links (it can be external links also) on Chrome
            // or any default web browser.
            // For example: following code will open contact.php
            // in Chrome or any default web browser of your mobile.
            /*if (url.equalsIgnoreCase("http://sandipBhattacharya.com/contact.php") || url.equalsIgnoreCase("https://sandipbhattacharya.com/contact.php")) {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else{
                return false;
            }
            */
            if (url.startsWith("intent")) {
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                    if (fallbackUrl != null) {
                        view.loadUrl(fallbackUrl)
                        return true
                    }
                } catch (e: URISyntaxException) {
                    // Syntax problem with uri
                }
            }
            /*if (url.startsWith("tel:")) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                startActivity(intent)
            } else if (url.startsWith("mailto:")) {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                startActivity(intent)
            }

             */
            view.loadUrl(url)
            return false
        }
    }
    override fun onResume() {
        super.onResume()
        if(isAlreadyCreated && !isNetworkAvailable()){
            isAlreadyCreated = false
            Toast.makeText(context, " Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private fun isNetworkAvailable(): Boolean{
        val connMgr = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return (networkInfo != null && networkInfo.isConnectedOrConnecting)

    }

/*private fun startLoaderAnimate(){
    val objectAnimator = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            val startHeight = 170
            val newHeight = (startHeight*(startHeight+40)*interpolatedTime).toInt()
            loaderImage.layoutParams.height = newHeight
            loaderImage.requestLayout()
        }

        override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
            super.initialize(width, height, parentWidth, parentHeight)
        }


        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    objectAnimator.repeatCount = -1
    objectAnimator.repeatMode= ValueAnimator.REVERSE
    objectAnimator.duration = 1000
    loaderImage.startAnimation(objectAnimator)
}

private fun endLoaderAnimate(){
    loaderImage.clearAnimation()
    loaderImage.visibility = View.GONE
}

 */

/*private fun showError(title:String, message: String, context: Context){
    val dialog = AlertDialog.Builder(context)
    dialog.setTitle(title)
    dialog.setMessage(message)
    dialog.setNegativeButton("Cancel", {_, _ ->
    })
    dialog.setNeutralButton("Settings", {_, _ ->
        startActivity(Intent(Settings.ACTION_SETTINGS))
    })
    dialog.setPositiveButton("Retry", {_, _ ->
        this@WebViewActivity.recreate()
    })

    dialog.create().show()

}

 */
}
