package com.fisher.dictionary.Youdao;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Fisher on 10/22/2015.
 */
public interface YoudaoServer {
	@GET( Public.path )
	void query( @Query( "q" ) String word, Callback<Youdao> callback );
}
