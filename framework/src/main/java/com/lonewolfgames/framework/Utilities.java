package com.lonewolfgames.framework;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jhyde on 7/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class Utilities {

    public static SimpleDateFormat SERVER_DATE_FORMAT 	= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN_HARD =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String PASSWORD_PATTERN_EASY =
            "^.{6,}$";
    private static final String WEBSITE_PATTERN =
            "^[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
    private static final String PHONE_PATTERN =
            "^1?\\W*([2-9][0-8][0-9])\\W*([2-9][0-9]{2})\\W*([0-9]{4})(\\se?x?t?(\\d*))?";
    private static final String PHONE_PATTERN2 =
            "^1?\\s*\\W?\\s*([2-9][0-8][0-9])\\s*\\W?\\s*([2-9][0-9]{2})\\s*\\W?\\s*([0-9]{4})(\\e?x?t?(\\d*))?";

	public static boolean isNetworkAvailable(Context context) {
		if(context == null) {
			return false;
		}
		
		boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    
	    return haveConnectedWifi || haveConnectedMobile;
	}

    public final static void displayToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public final static void displayToastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public final static AlertDialog.Builder displayAlert(
            Context context,
            String title,
            String message,
            DialogInterface.OnClickListener positiveListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positiveListener)
                .setIcon(android.R.drawable.ic_dialog_alert);
    }

    public final static AlertDialog.Builder displayAlert(
            Context context,
            String title,
            String message,
            String positive,
            String negative,
            DialogInterface.OnClickListener positiveListener,
            DialogInterface.OnClickListener negativeListener) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener)
                .setIcon(android.R.drawable.ic_dialog_alert);
    }

    public static void hideSoftkeyboard(Activity activity, View view) {// Close the soft keyboard
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
	}

	public static String dateNow() {
		return SERVER_DATE_FORMAT.format(new Date());
	}

    public static Date parseDate(SimpleDateFormat format, String dateString) {
        Date date = new Date();

        try {
            date = format.parse(dateString);
        } catch(ParseException ex) {

        }

        return date;
    }

	public static float parseFloat(String floatString) {
		float number = 0;

		try {
			number = Float.parseFloat(floatString);
		} catch (NumberFormatException ex) {

		}

		return number;
	}

    public static int parseInt(String integerString) {
        int number = 0;

        try {
            number = Integer.parseInt(integerString);
        } catch (NumberFormatException ex) {

        }

        return number;
    }

    public static boolean parseBoolean(String booleanString) {
        boolean b = false;

        try {
            b = Boolean.parseBoolean(booleanString);
        } catch(Exception ex) {

        }

        return b;
    }

    public static boolean validateEmail(final String email) {
        if(email.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean validatePassword(final String password) {
        if(password.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN_EASY);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static boolean validateWebsite(final String website) {
        if(website.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile(WEBSITE_PATTERN);
        Matcher matcher = pattern.matcher(website);

        return matcher.matches();
    }

    public static boolean validatePhone(final String phone) {
        if(phone.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile(PHONE_PATTERN2);
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

    public static void setSelectableItemBackground(Context context, View view) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        view.setBackgroundResource(backgroundResource);
        typedArray.recycle();
    }
}
