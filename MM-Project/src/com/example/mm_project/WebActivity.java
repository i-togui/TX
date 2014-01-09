package com.example.mm_project;

import java.net.URL;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebActivity extends Activity {

	public void closeAct(View v)
	{
		this.finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_web);
		Intent myIntent= getIntent(); // gets the previously created intent
		String title = myIntent.getStringExtra("title");
		String url = myIntent.getStringExtra("url");
		TextView t = (TextView)findViewById(R.id.title);
		t.setText(title);
		WebView mWebView = (WebView) findViewById(R.id.customWebView);

		
		WebViewClient yourWebClient = new WebViewClient()
	       {
	           // Override page so it's load on my view only
	           @Override
	           public boolean shouldOverrideUrlLoading(WebView  view, String  url)
	           {
	               return false;
	           }
	       };
	       
	       
	       // Get Web view
	       mWebView = (WebView) findViewById( R.id.customWebView ); //This is the id you gave 
	       mWebView.getSettings().setJavaScriptEnabled(false);   
	       mWebView.getSettings().setSupportZoom(true);       //Zoom Control on web (You don't need this 
	       mWebView.getSettings().setBuiltInZoomControls(true); //Enable Multitouch if supported by ROM
	       mWebView.setWebViewClient(yourWebClient);		
	       mWebView.loadUrl(url);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

}
