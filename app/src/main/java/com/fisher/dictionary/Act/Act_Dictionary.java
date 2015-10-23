package com.fisher.dictionary.Act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.fisher.dictionary.R;
import com.fisher.dictionary.Utils.HTTPUtils;
import com.fisher.dictionary.Youdao.Public;
import com.fisher.dictionary.Youdao.Youdao;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

/**
 * Created by Fisher on 10/23/2015.
 */
public class Act_Dictionary extends AppCompatActivity {
	private EditText eText;
	private TextView tText;
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		super.onCreate( savedInstanceState );
		setContentView( R.layout.act_dictionary );

		init();
	}
	private void init( ) {
		eText=(EditText)findViewById( R.id.eText );
		tText=( TextView)findViewById( R.id.tText );
		Intent intent=getIntent();
		String sWord=intent.getStringExtra( "msg" );
		if(sWord!=null){
			eText.setText( sWord );
			new Thread(  ){
				@Override
				public void run( ) {
					btnQuery( null );
				}
			}.start();
		}
	}

	public void btnQuery(View view){
		String sText=eText.getText().toString().trim();
		HTTPUtils.doGetAsyn( Public.getQueryURL( sText ) );
	}

	public void onEventMainThread(HTTPUtils.RequestCompleteEvent event){
		String sResult=event.getResult();
		Youdao youdao=new Gson().fromJson( sResult, Youdao.class);
		tText.setText( youdao.toString() );
	}

	@Override
	protected void onStart( ) {
		super.onStart();
		EventBus.getDefault().register( this );
	}
	@Override
	protected void onStop( ) {
		EventBus.getDefault().unregister( this );
		super.onStop();
	}
}
