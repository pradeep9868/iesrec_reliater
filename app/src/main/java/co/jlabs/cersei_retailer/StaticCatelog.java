package co.jlabs.cersei_retailer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Pradeep on 1/9/2016.
 */
public class StaticCatelog {
    public static String load_json(Context context,int resource)
    {
        InputStream is = context.getResources().openRawResource(resource);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        }
        catch (Exception e)
        {

        }
        finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }

        return writer.toString();
    }
    public static String getStringProperty(Context context,String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        String res = null;
        if (sharedPreferences != null) {
            res = sharedPreferences.getString(key, null);
        }
        return res;
    }

    public static void setStringProperty(Context context,String key, String value) {
        SharedPreferences  sharedPreferences = context.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }
    public static String geturl()
    {
        //return "http://192.168.0.13:8000/";
        return "http://lannister-api.elasticbeanstalk.com/";
    }
    public static CharSequence SpanIt(String text1,String text2)
    {

        SpannableString span1 = new SpannableString(text1);
        span1.setSpan(new AbsoluteSizeSpan(30), 0, text1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span2 = new SpannableString(text2);
        span2.setSpan(new AbsoluteSizeSpan(20), 0, text2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return TextUtils.concat(span1,"\n\n", span2);
    }
    public static CharSequence SpanIt2(String text1,String text2,String text3)
    {

        String enter ="\n\n";

        SpannableString span1 = new SpannableString(text1);
        span1.setSpan(new AbsoluteSizeSpan(30), 0, text1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span2 = new SpannableString(text2);
        span2.setSpan(new AbsoluteSizeSpan(23), 0, text2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString span3 = new SpannableString(text3);
        span3.setSpan(new AbsoluteSizeSpan(23), 0, text3.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString enter1 = new SpannableString(enter);
        enter1.setSpan(new AbsoluteSizeSpan(15), 0, enter.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString enter2 = new SpannableString(enter);
        enter2.setSpan(new AbsoluteSizeSpan(3), 0, enter.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        return TextUtils.concat(span1,enter1, span2,enter2,span3);
    }
    public static String printHashKey(Context ctx) {
        // Add code to print out the key hash
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "SHA-1 generation: the key count not be generated: NameNotFoundException thrown";
        } catch (NoSuchAlgorithmException e) {
            return "SHA-1 generation: the key count not be generated: NoSuchAlgorithmException thrown";
        }

        return "SHA-1 generation: epic failed";
    }
}
