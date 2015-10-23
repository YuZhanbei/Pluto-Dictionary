package com.fisher.dictionary.Utils;


import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Fisher on 10/16/2015.
 */
public class BasicLog {
	public static String charset = "utf-8";
	public static String sdSDCard= Environment.getExternalStorageDirectory().getAbsolutePath();
	public static String sdAPP=sdSDCard+"/Fisher/App";

	private OutputStreamWriter osw;

	public static BasicLog getLog( String path ) {
		return new BasicLog( path );
	}
	private BasicLog( String path ) {
		try {
			if ( new File( path ).getParentFile().exists() || new File( path ).getParentFile().mkdirs() )
				osw = new OutputStreamWriter( new FileOutputStream( path, true ), BasicLog.charset );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public boolean logLine( String msg ) {
		return log( msg + "\r\n" );
	}
	public boolean log( String msg ) {
		if ( osw == null )
			return false;
		try {
			osw.write( msg );
			osw.flush();
		} catch ( IOException e ) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean close( ) {
		try {
			if ( osw != null )
				osw.close();
		} catch ( IOException e ) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
