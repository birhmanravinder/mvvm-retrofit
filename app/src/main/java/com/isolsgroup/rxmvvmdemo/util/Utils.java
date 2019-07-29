package com.isolsgroup.rxmvvmdemo.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.isolsgroup.rxmvvmdemo.R;
import com.isolsgroup.rxmvvmdemo.ui.custom.ClickSpan;
import com.isolsgroup.rxmvvmdemo.ui.custom.RoundedBackgroundSpan;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.inject.Singleton;

import static android.content.Context.CLIPBOARD_SERVICE;

@Singleton
public class Utils {
    public void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(view, 0);
    }

    public void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private double degreeToRadians(double deg) {
        return deg * (Math.PI / 180);
    }

    public boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void showAlertDialog(Context context, @StringRes int titleResId, @StringRes int bodyResId) {
        showAlertDialog(context, context.getString(titleResId), context.getString(bodyResId), null);
    }

    public void showAlertDialog(Context context, String title, String body) {
        showAlertDialog(context, title, body, null);
    }

    public void showAlertDialog(Context context, String title, String body, DialogInterface.OnClickListener okListener) {
        if(okListener == null) okListener = (dialog, which) -> dialog.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(body)
                .setPositiveButton("OK", okListener);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.show();
    }

    public void showProgressDialog(Context context, String title, String body, boolean isCancellable) {
        showProgressDialog(context, title, body, null, isCancellable);
    }

    static ProgressDialog mProgressDialog;

    public void showProgressDialog(Context context, String title, String body, Drawable icon, boolean isCancellable) {

        if (context instanceof Activity) {
            if (!((Activity)context).isFinishing()) {
                mProgressDialog = ProgressDialog.show(context, title, body, true);
                mProgressDialog.setIcon(icon);
                mProgressDialog.setCancelable(isCancellable);
            }
        }
    }

    public static boolean isProgressDialogVisible() {
        return (mProgressDialog != null);
    }

    public static void dismissProgressDialog() {
        if (mProgressDialog != null)mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    public int getDip(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

    public int getPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public void showConfirmDialog(Context context, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        showConfirmDialog(context, message, yesListener, noListener, "Yes", "No");
    }

    public void showConfirmDialog(Context context, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener, String yesLabel, String noLabel) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (yesListener == null) {
            yesListener = (dialog, which) -> dialog.dismiss();
        }
        if (noListener == null) {
            noListener = (dialog, which) -> dialog.dismiss();
        }
        builder.setMessage(message).setPositiveButton(yesLabel, yesListener).setNegativeButton(noLabel, noListener).show();
    }




//    public static void playSoundLike(Context context) {
//        try {
//            SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
//            soundPool.load(context, R.raw.like_main, 1);
//            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//                @Override
//                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                    soundPool.play(sampleId, 1, 1, 0, 0, 1);
//                }
//            });
//
//        } catch (Exception e) {
//            Log.e("Error", e.toString());
//        }
//    }


    public String getFbAccessToken(Context context) {
        return context.getResources().getString(R.string.facebook_app_id) + "|" + Constants.FB_APP_SECRET;
    }

    public String format(long value) {

        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value);

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "K");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static boolean isValidURL(String url) {
        if (isEmptyOrNull(url)) {
            return false;
        }
        return Patterns.WEB_URL.matcher(url.toLowerCase()).matches();
    }

    public boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public int getAppVersionCode(Context context) {
        int v = 0;
        try {
            v = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return v;
    }

    public static String getAppName(Context context){
        return context.getResources().getString(R.string.app_name);
    }

    public static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("[2-9][0-9][0-9]{8}");
            return pattern.matcher(target).matches();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isEmptyOrNull(String text) {
        if (text == null || TextUtils.isEmpty(text) || text.length() < 1) return true;
        else return false;
    }

    public static String notNullStr(String text) {
        if (text == null) return "";
        return text;
    }

    public static int geOffsetFromUtc() {
        return TimeZone.getDefault().getOffset(new Date().getTime()) / 1000;
    }

    public static Integer tryParseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            return 0;
        }
    }

    public static Long tryParseLong(String text) {
        try {
            return Long.parseLong(text);

        } catch (Exception e) {
            return 0L;
        }
    }

    public static Float tryParseFloat(String text) {
        try {
            return Float.parseFloat(text);

        } catch (Exception e) {
            return 0F;
        }
    }

    public static Double tryParseDouble(String text) {
        try {
            return Double.parseDouble(text);

        } catch (Exception e) {
            return 0D;
        }
    }

    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static void navigateToAppSettings(Activity activity) {
        try {
            final String appPackageName = activity.getPackageName();
            Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + appPackageName));
            activity.startActivity(i);

        } catch (ActivityNotFoundException ex) {
            Intent i = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            activity.startActivity(i);
        }
    }

    public static void navigateToPlayStore(Activity activity) {
        final String appPackageName = activity.getPackageName();
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void launchGoogleMap(Activity activity, double latitude, double longitude) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude + ""));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude + "")));
        }
    }

    public static void sendEmail(Activity activity, String emailAddress){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);

        try {
            activity.startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(activity, "There is no email client available.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void call(Activity activity, String numbers){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + Utils.notNullStr(numbers)));
        activity.startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)return Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        else return Html.fromHtml(html);
    }

    public static void generateFacebookLoginKeyHash(Activity activity) {
        try {
            String appPackageName = activity.getPackageName();
            PackageInfo info = activity.getPackageManager().getPackageInfo(appPackageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public static void copyTextToClipboard(Context context, String text){
        ClipboardManager clipboard = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Share URL", Utils.notNullStr(text));
        clipboard.setPrimaryClip(clip);
    }

    public static void shareOnTwitter(Activity activity, String link) {
        try {
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?url="+link));
            activity.startActivity(intent);

        }catch(Exception e){
            Toast.makeText(activity, "Failed !", Toast.LENGTH_SHORT).show();
        }
    }

    public static void openSoftKeyboard(final Context context, final EditText etText){
        etText.post(new Runnable() {
            @Override
            public void run() {
                etText.requestFocus();
                InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public static void appendBoldOnly(SpannableStringBuilder span, String text){
        span.append(text);
        span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), span.length()-text.length(), span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void appendBold(SpannableStringBuilder span, String text){
        span.append(text);
        span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), span.length()-text.length(), span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.DKGRAY), span.length()-text.length(), span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void appendRoundedBg(Context context, SpannableStringBuilder span, String text, int textColor, int backgroundColor, int radius){
        span.append(text);
        span.setSpan(new RoundedBackgroundSpan(context, textColor, backgroundColor, radius), span.length()-text.length(), span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void appendBoldClickable(SpannableStringBuilder span, String text, int color, ClickListener clickListener){
        span.append(text);
        span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), span.length()-text.length(), span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ClickSpan(color, clickListener), span.length()-text.length(), span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void appendClickable(SpannableStringBuilder span, String text, int color, ClickListener clickListener){
        span.append(text);
        span.setSpan(new ClickSpan(color, clickListener), span.length()-text.length(), span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}

