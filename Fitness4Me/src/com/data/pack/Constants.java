package com.data.pack;

import com.data.fitness4me.R;

public class Constants {

	public static final String CONSUMER_KEY = "D3zXrrVXnsIVIXoT49MGLw";
	public static final String CONSUMER_SECRET= "dZyQ3xV7JlmBpUZIQ0m2Kswva2TlxwmbpXRQn8Phc6g";
	
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
	
	public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow-twitter";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
	 
}

