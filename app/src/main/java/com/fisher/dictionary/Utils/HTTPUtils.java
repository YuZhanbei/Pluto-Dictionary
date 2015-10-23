package com.fisher.dictionary.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import de.greenrobot.event.EventBus;

public class HTTPUtils {
	private static final int TIMEOUT_IN_MILLIONS = 25000;
	public static final String HTF_8 = "UTF-8";

	/**
	 * 异步的Get请求
	 *
	 * @param urlStr
	 */
	public static void doGetAsyn( final String urlStr ) {
		new Thread() {
			public void run( ) {
				try {
					String result = doGet( urlStr );
					EventBus.getDefault().post( new RequestCompleteEvent( result ) );
				} catch ( Exception e ) {
					e.printStackTrace();
				}

			}
			;
		}.start();
	}

	/**
	 * 异步的Post请求
	 *
	 * @param urlStr
	 * @param params
	 * @throws Exception
	 */
	public static void doPostAsyn( final String urlStr, final String params ) {
		new Thread() {
			public void run( ) {
				try {
					String result = doPost( urlStr, params );
					EventBus.getDefault().post( new RequestCompleteEvent( result ) );
				} catch ( Exception e ) {
					e.printStackTrace();
				}

			}
			;
		}.start();

	}

	/**
	 * Get请求，获得返回数据
	 *
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public static String doGet( String urlStr ) {
		URL url;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			url = new URL( urlStr );
			conn = ( HttpURLConnection ) url.openConnection();
			conn.setReadTimeout( TIMEOUT_IN_MILLIONS );
			conn.setConnectTimeout( TIMEOUT_IN_MILLIONS );
			conn.setRequestMethod( "GET" );
			conn.setRequestProperty( "accept", "*/*" );
			conn.setRequestProperty( "connection", "Keep-Alive" );
			conn.connect();
			if ( conn.getResponseCode() == 200 ) {
				is = conn.getInputStream();
				return getString( is, HTTPUtils.HTF_8 );
			} else {
				throw new RuntimeException( " responseCode is not 200 ... " );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			try {
				if ( is != null )
					is.close();
			} catch ( IOException e ) {
			}
			if(conn!=null)
				conn.disconnect();
		}
		return null;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url   发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws Exception
	 */
	public static String doPost( String url, String param ) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL( url );
			// 打开和URL之间的连接
			HttpURLConnection conn = ( HttpURLConnection ) realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty( "accept", "*/*" );
			conn.setRequestProperty( "connection", "Keep-Alive" );
			conn.setRequestMethod( "POST" );
			conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
			conn.setRequestProperty( "charset", "utf-8" );
			conn.setUseCaches( false );
			// 发送POST请求必须设置如下两行
			conn.setDoOutput( true );
			conn.setDoInput( true );
			conn.setReadTimeout( TIMEOUT_IN_MILLIONS );
			conn.setConnectTimeout( TIMEOUT_IN_MILLIONS );

			if ( param != null && !param.trim().equals( "" ) ) {
				// 获取URLConnection对象对应的输出流
				out = new PrintWriter( conn.getOutputStream() );
				// 发送请求参数
				out.print( param );
				// flush输出流的缓冲
				out.flush();
			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
			String line;
			while ( ( line = in.readLine() ) != null ) {
				result += line;
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if ( out != null ) {
					out.close();
				}
				if ( in != null ) {
					in.close();
				}
			} catch ( IOException ex ) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static class RequestCompleteEvent {
		private String result = "";
		public String getResult( ) {
			return result;
		}
		public RequestCompleteEvent( String msg ) {
			this.result = msg;
		}
	}


	public static String getString( InputStream in, String charset ) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader( new InputStreamReader( in, charset ) );
		String line;
		while ( ( line = br.readLine() ) != null ) {
			sb.append( line );
		}
		return sb.toString();
	}

}
