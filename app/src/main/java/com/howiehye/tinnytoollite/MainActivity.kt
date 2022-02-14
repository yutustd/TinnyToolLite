package com.howiehye.tinnytoollite

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity(){
    private var cookieStr = ""
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myWebView: WebView = findViewById(R.id.webview)
        val urlJd = getString(R.string.url)
        val btn_copy: Button = findViewById(R.id.btn_copy)
        val btn_clear: Button = findViewById(R.id.btn_clear)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.loadWithOverviewMode = true

        myWebView.loadUrl(urlJd)
        myWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
        Toast.makeText(this@MainActivity, "请登录后再进行操作！！！", Toast.LENGTH_SHORT).show()
        btn_copy.setOnClickListener {
            val cookieManager: CookieManager = CookieManager.getInstance()
            this.cookieStr = cookieManager.getCookie("https://m.jd.com")
            val jdCookie = getJdCookie(this.cookieStr)[0]
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("simple text", jdCookie)
            clipboardManager.setPrimaryClip(clipData)
            println(jdCookie)
            // Toast 提示
            Toast.makeText(this@MainActivity,"已复制到剪贴板", Toast.LENGTH_SHORT).show()
        }
        btn_clear.setOnClickListener {
            val cookieManager: CookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookies(null)
            WebView(applicationContext).clearCache(true)
            WebView(applicationContext).clearHistory()
            myWebView.loadUrl(urlJd)
        }
    }
    private fun getJdCookie(cookieStr: String): Array<String> {
        val cookieTmp = cookieStr.split(";")
        var jdPin: String = ""
        var jdKey: String = ""
        for (ar1 in cookieTmp) {
            if (ar1.contains("pt_pin")) {
                jdPin = ar1.split("=")[1]
            }
            if (ar1.contains("pt_key")) {
                jdKey = ar1.split("=")[1]
            }
        }
        return arrayOf("pt_key=$jdKey;pt_pin=$jdPin;", jdKey, jdPin)
    }
}
