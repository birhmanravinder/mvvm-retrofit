package com.isolsgroup.rxmvvmdemo.ui.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.List;

public class DialogShare {
    private Context context;
    private Activity activity;
    public List<ResolveInfo> appsList;

    public DialogShare(Activity activity) { this.activity=activity; this.context=activity.getBaseContext(); }

    public void show(final String text){

        final Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        appsList=context.getPackageManager().queryIntentActivities(intent, 0);

        int fbAppPos=0,twitterAppPos=0;
        if(appsList!=null && appsList.size()>2){
            for (int i = 0; i <appsList.size(); i++) {
                ResolveInfo info=appsList.get(i);
                if(info.activityInfo.packageName.contains("com.facebook.katana")){ fbAppPos=i; }
                if(info.activityInfo.name.equals("com.twitter.android.composer.ComposerActivity")){ twitterAppPos=i; }
            }
            Collections.swap(appsList, fbAppPos, 0);
            Collections.swap(appsList, twitterAppPos, 1);
        }


        AppListAdapter adapter=new AppListAdapter(activity);
        new AlertDialog.Builder(activity).setTitle("Share").setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item ) {

                ResolveInfo info=appsList.get(item);
                String packageName=info.activityInfo.packageName;
                if(packageName.contains("com.facebook.katana")){

//                    ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse(text)).build();
//                    ShareDialog.show(activity, content);

                }else{
                    Intent in=new Intent(Intent.ACTION_SEND);
                    in.setComponent(new ComponentName(packageName, info.activityInfo.name));
                    in.setPackage(packageName);
                    in.putExtra(Intent.EXTRA_TEXT, text);
                    activity.startActivity(in);
                }
            }
        }).show();
    }

    class AppListAdapter extends BaseAdapter {

        Context context;
        PackageManager pm;
        AppListAdapter(Context context){this.context=context; pm=context.getPackageManager(); }

        public int getCount() { if (appsList != null) return appsList.size(); return 0; }

        public Object getItem(int paramInt) {
            return null;
        }

        public long getItemId(int paramInt) {
            return 0L;
        }

        public View getView(int position, View view, ViewGroup paramViewGroup) {

//            if (view == null) view = LayoutInflater.from(context).inflate(R.layout.listrow_user_tag, null);
//            ImageView imgUser=(ImageView)view.findViewById(R.id.imgUser);
//            VTextView textName=(VTextView)view.findViewById(R.id.textName);
//            ResolveInfo info= appsList.get(position);
//            try{
//                ApplicationInfo appInfo=pm.getApplicationInfo(info.activityInfo.packageName, 0);
//                Drawable icon=pm.getApplicationIcon(appInfo);
//                CharSequence appName=pm.getApplicationLabel(appInfo);
//                imgUser.setImageDrawable(icon);
//                textName.setText(Utils.notNullStr(appName.toString()));
//            }catch (Exception e){  }

            return view;
        }
    }

}
