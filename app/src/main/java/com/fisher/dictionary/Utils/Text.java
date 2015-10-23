package com.fisher.dictionary.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fisher on 10/22/2015.
 */
public class Text {
	public static boolean fnContainsChinese(String sText){
		Pattern p = Pattern.compile( "[\\u4e00-\\u9fa5]" );
		Matcher m = p.matcher( sText );
		if(m.find())
			return true;
		return false;
	}
}
