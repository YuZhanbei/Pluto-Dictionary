package com.fisher.dictionary.Youdao;

/**
 * Created by Fisher on 10/22/2015.
 */
public class Public {
	public static final String url="http://fanyi.youdao.com";
	public static final String path="/openapi.do?keyfrom=pluto-dictionary&key=2097312305&type=data&doctype=json&version=1.1";
	public static String  getQueryURL(String sWord){
		return Public.url+Public.path+"&q="+sWord;
	}

}
