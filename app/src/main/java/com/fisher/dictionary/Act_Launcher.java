package com.fisher.dictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fisher.dictionary.Ser.Dictionary;
import com.fisher.dictionary.Utils.ContextHelper;

public class Act_Launcher extends AppCompatActivity {



	private TextView tServiceStatus;
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.act_launcher );

		init();
	}
	private void init( ) {
		tServiceStatus = ( TextView ) findViewById( R.id.tServiceStatus );
		btnCheckService( null );
	}

	public void btnCheckService( View view ) {
		boolean status = ContextHelper.fnCheckService( this, Dictionary.class );
		if ( status ) {
			tServiceStatus.setText( "Running. .." );
		} else {
			tServiceStatus.setText( "Pause. .." );
		}

	}



	public void btnStartService( View view ) {
		startService( new Intent( this, Dictionary.class ) );
		btnCheckService( null );
	}
	public void btnStopService( View view ) {
		stopService( new Intent( this, Dictionary.class ) );
		btnCheckService( null );
	}


	private String log( String msg ) {
		btnCheckService( null );
		Log.v( this.getClass().getName() + " -->> ", msg );
		return msg;
	}
	private String alert( String msg ) {
		btnCheckService( null );
		Toast.makeText( this, msg, Toast.LENGTH_LONG ).show();
		return msg;
	}
	private String info( String msg ) {
		btnCheckService( null );
		return msg;
	}


}
