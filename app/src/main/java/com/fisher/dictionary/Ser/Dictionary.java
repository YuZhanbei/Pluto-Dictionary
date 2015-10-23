package com.fisher.dictionary.Ser;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fisher.dictionary.Act.Act_Dictionary;
import com.fisher.dictionary.R;
import com.fisher.dictionary.Utils.BasicLog;
import com.fisher.dictionary.Utils.HTTPUtils;
import com.fisher.dictionary.Utils.Text;
import com.fisher.dictionary.Utils.Time;
import com.fisher.dictionary.Youdao.Public;
import com.fisher.dictionary.Youdao.Youdao;
import com.fisher.dictionary.Youdao.YoudaoServer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Dictionary extends Service {
	private YoudaoServer query;
	private NotificationManager notificationManager;
	public static final int iNotificationFlag = 102300;
	private int iCount = 0;
	private BasicLog mDictionaryLog;
	private BasicLog mPasteBoardLog;
	private BasicLog mWordsLog;
	private String sPasteBoard = "";
	private long lLastQueryTime = 0;



	@Override
	public void onCreate( ) {
		super.onCreate();
		initLog();
		log( Thread.currentThread().getStackTrace()[2].getMethodName() );
		record( mDictionaryLog, Thread.currentThread().getStackTrace()[2].getMethodName() );
		init();
	}
	private void initLog( ) {
		mPasteBoardLog = BasicLog.getLog( BasicLog.sdAPP + "/Dictionary/Log/PasteBoard.log" );
		mDictionaryLog = BasicLog.getLog( BasicLog.sdAPP + "/Dictionary/Log/Dictionary.log" );
		mWordsLog = BasicLog.getLog( BasicLog.sdAPP + "/Dictionary/Log/Words.log" );
	}
	@Override
	public void onDestroy( ) {
		log( Thread.currentThread().getStackTrace()[2].getMethodName() );
		record( mDictionaryLog, Thread.currentThread().getStackTrace()[2].getMethodName() );
		release();
		super.onDestroy();
	}

	@Override
	public int onStartCommand( Intent intent, int flags, int startId ) {
		log( Thread.currentThread().getStackTrace()[2].getMethodName() );
		record( mDictionaryLog, Thread.currentThread().getStackTrace()[2].getMethodName() );
		return super.onStartCommand( intent, START_STICKY, startId );
	}
	private void init( ) {
		notificationManager = ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );
		initRetrofit();
		initClipBoard();
		EventBus.getDefault().register( this );
		record( mDictionaryLog, "Registered EventBus from dictionary service!" );
	}


	private void initRetrofit( ) {
		RestAdapter mAdapter = new RestAdapter.Builder().setEndpoint( Public.url ).build();
		query = mAdapter.create( YoudaoServer.class );
	}
	private void initClipBoard( ) {
		final ClipboardManager mManager = ( ClipboardManager ) getSystemService( Context.CLIPBOARD_SERVICE );
		ClipboardManager.OnPrimaryClipChangedListener mListener = new ClipboardManager.OnPrimaryClipChangedListener() {

			@Override
			public void onPrimaryClipChanged( ) {
				//				ClipData mData = mManager.getPrimaryClip();
				//				ClipData.Item item = mData.getItemAt( 0 );
				//				String sData = item.getText().toString();
				String sNewPasteBoard = mManager.getText().toString();
				log( "Now the " + ( ++iCount ) + " pasteboard changed to: " + sNewPasteBoard );
				if ( sNewPasteBoard.equals( "" ) || ( sNewPasteBoard.equals( sPasteBoard ) && ( ( System.currentTimeMillis() - lLastQueryTime ) < 200 ) ) ) {
					return;
				}
				sPasteBoard = sNewPasteBoard;
				lLastQueryTime = System.currentTimeMillis();
				mPasteBoardLog.log( getRecord( sPasteBoard ) );

				String sWord = fnProcessText( sPasteBoard );
				if ( sWord != null ) {
					fnQueryText( sWord );
				}
			}
		};
		mManager.addPrimaryClipChangedListener( mListener );
	}



	private String fnProcessText( String sText ) {
		sText = sText.trim();
		if ( sText.equals( "" ) )
			return null;
		if ( sText.length() > 20 ) {
			log( "The text is too long" );
			return null;
		}
		if ( sText.indexOf( ' ' ) >= 0 ) {
			log( "The text contains blank" );
			return null;
		}

		if ( Text.fnContainsChinese( sText ) ) {
			log( "The text contains Chinese" );
			return null;
		}
		log( "The text in the paste board is ok -> " + sText );
		return sText;
	}
	private void fnQueryText( String sWord ) {
		fnQueryTextHTTP( sWord );
	}
	private void fnQueryTextHTTP( String sWord ) {
		log( Public.url + Public.path + "&q=" + sWord );
		HTTPUtils.doGetAsyn( Public.url + Public.path + "&q=" + sWord );
	}
	public void onEventMainThread( HTTPUtils.RequestCompleteEvent event ) {
		log( event.getResult() );
		try {
			Youdao youdao = new Gson().fromJson( event.getResult(), Youdao.class );
			log( youdao.toString() );
			fnNotice( youdao );
		} catch ( JsonSyntaxException e ) {
			e.printStackTrace();
			fnNotice();
		}
	}

	private void fnQueryTextRetrofit( String sData ) {
		query.query( sData, new Callback< Youdao >() {
			@Override
			public void success( Youdao youdao, Response response ) {
				log( youdao.toString() );
				log( new Gson().toJson( youdao ) );
				fnNotice( youdao );
			}
			@Override
			public void failure( RetrofitError error ) {
				log( "Error happened!\r\n" + error.toString() + "\r\n" + error.getUrl() + "\r\n" + error.getMessage() + "\r\n" + error.getBody() );
				record( mDictionaryLog, "Error happened!\r\n" + error.toString() );
				record( mWordsLog, "Failed to query word:\r\n" + error.getUrl() );
				fnNotice();
			}
		} );
	}

	@TargetApi( Build.VERSION_CODES.JELLY_BEAN )
	private void fnNotice( Youdao youdao ) {
		notificationManager.cancel( iNotificationFlag );

		String title = youdao.getQuery();
		if ( youdao.getBasic() != null && youdao.getBasic().getUs_phonetic() != null )
			title += " [" + youdao.getBasic().getUs_phonetic() + "]";
		String content = youdao.getNoticeContent();
		String ticker = "I am Ticker";
		Notification mNotification = new Notification.Builder( this ).setSmallIcon( R.drawable.icon_groot )
				//
				.setContentIntent( PendingIntent.getActivity( this, 0, new Intent( this, Act_Dictionary.class ).putExtra( "msg", youdao.getQuery() ), PendingIntent.FLAG_UPDATE_CURRENT ) )
				//
				.setTicker( ticker ).setContentTitle( title ).setContentText( content ).setAutoCancel( true ).build();
		notificationManager.notify( iNotificationFlag, mNotification );

		record( mWordsLog, title + "\n" + content );
	}

	@TargetApi( Build.VERSION_CODES.JELLY_BEAN )
	private void fnNotice( ) {
		notificationManager.cancel( iNotificationFlag );

		String title = "Query word(s) failed!";
		String content = "Please make sure your network connection is fine!";
		String ticker = "I am Ticker";
		Notification mNotification = new Notification.Builder( this ).setSmallIcon( R.drawable.icon_groot ).setTicker( ticker ).setContentTitle( title ).setContentText( content ).setAutoCancel( true ).build();
		notificationManager.notify( iNotificationFlag, mNotification );
	}



	@Nullable
	@Override
	public IBinder onBind( Intent intent ) {
		log( Thread.currentThread().getStackTrace()[2].getMethodName() );
		record( mDictionaryLog, Thread.currentThread().getStackTrace()[2].getMethodName() );
		return null;
	}
	private void release( ) {
		EventBus.getDefault().unregister( this );
		record( mDictionaryLog, "Unregistered EventBus from dictionary service!" );
		if ( mDictionaryLog != null )
			mDictionaryLog.close();
		if ( mPasteBoardLog != null )
			mPasteBoardLog.close();
		if ( mWordsLog != null )
			mWordsLog.close();
	}


	private String log( String msg ) {
		Log.v( this.getClass().getName() + " -->> ", msg + "" );
		return msg;
	}
	private String getRecord( String msg ) {
		return "------------------->>\r\n" + new Time().fGetFullTime() + "\r\n" + msg + "\r\n\r\n";
	}
	private String record( BasicLog log, String msg ) {
		if ( log != null )
			log.log( getRecord( msg ) );
		return msg;
	}
}
