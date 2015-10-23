package com.fisher.dictionary.Youdao;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by Fisher on 10/22/2015.
 */
public class Youdao {
	private int errorCode = 0;
	private String query = null;
	private String[] translation = null;
	private Word basic = null;

	@Override
	public String toString( ) {
		String sText = "\n";
		sText += "Error Code: " + this.getErrorCode() + "\n";
		sText += "Query: " + this.getQuery() + "\n";
		sText += "Translation: " + Arrays.toString( this.getTranslation() ) + "\n";
		sText += "Basic: " + this.getBasic() + "\n";
		sText += "Web: " + Arrays.toString( this.getWeb() ) + "\n";
		return sText;
	}
	public String toText( ) {
		String sText = "";
		sText += this.getQuery();

		if(this.getBasic() == null)
			return null;

		if ( this.getBasic().getUs_phonetic() != null )
			sText += "\n[" + this.getBasic().getUs_phonetic() + "] ";

		String[] translation = this.getBasic().explains;
		for ( int i = 0; i < translation.length; i++ ) {
			sText += "\n" + translation[i];
		}
		return sText;
	}
	public String getNoticeContent( ) {
		String sText = "";
		if(this.getBasic() == null)
			return null;
		String[] translation = this.getBasic().explains;
		for ( int i = 0; i < translation.length; i++ ) {
			sText +=  translation[i]+";  ";
		}
		return sText;
	}

	public int getErrorCode( ) {
		return errorCode;
	}
	public void setErrorCode( int errorCode ) {
		this.errorCode = errorCode;
	}
	public String getQuery( ) {
		return query;
	}
	public void setQuery( String query ) {
		this.query = query;
	}
	public String[] getTranslation( ) {
		return translation;
	}
	public void setTranslation( String[] translation ) {
		this.translation = translation;
	}
	public Word getBasic( ) {
		return basic;
	}
	public void setBasic( Word basic ) {
		this.basic = basic;
	}
	public Web[] getWeb( ) {
		return web;
	}
	public void setWeb( Web[] web ) {
		this.web = web;
	}
	private Web[] web = null;

	public class Word {
		private String phonetic = null;
		@SerializedName( "uk-phonetic" )
		private String uk_phonetic = null;
		@SerializedName( "us-phonetic" )
		private String us_phonetic = null;
		private String[] explains = null;

		@Override
		public String toString( ) {
			String sText = "";
			sText += "Phonetic: " + this.getPhonetic() + "\n";
			sText += "Uk_phonetic: " + this.getUk_phonetic() + "\n";
			sText += "Us_phonetic: " + this.getUs_phonetic() + "\n";
			sText += "Explains: " + Arrays.toString( this.getExplains() ) + "\n";
			return sText;
		}

		public String getPhonetic( ) {
			return phonetic;
		}
		public void setPhonetic( String phonetic ) {
			this.phonetic = phonetic;
		}
		public String getUk_phonetic( ) {
			return uk_phonetic;
		}
		public void setUk_phonetic( String uk_phonetic ) {
			this.uk_phonetic = uk_phonetic;
		}
		public String getUs_phonetic( ) {
			return us_phonetic;
		}
		public void setUs_phonetic( String us_phonetic ) {
			this.us_phonetic = us_phonetic;
		}
		public String[] getExplains( ) {
			return explains;
		}
		public void setExplains( String[] explains ) {
			this.explains = explains;
		}
	}

	private class Web {
		private String key = null;
		private String[] value = null;

		@Override
		public String toString( ) {
			String sText = "";
			sText += "Key: " + this.getKey() + "\n";
			sText += "Value: " + Arrays.toString( this.getValue() ) + "\n";
			return sText;
		}


		public String[] getValue( ) {
			return value;
		}
		public void setValue( String[] value ) {
			this.value = value;
		}
		public String getKey( ) {
			return key;
		}
		public void setKey( String key ) {
			this.key = key;
		}
	}
}
