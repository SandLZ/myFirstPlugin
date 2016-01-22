package com.handsmap.myFirstPlugin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class myFirstPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("showAlert")) {
            JSONObject jsonObject = args.getJSONObject(0);
            this.showAlert(jsonObject, callbackContext);
            return true;
        }
        return false;
    }

    private void showAlert(final JSONObject message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {

            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CharSequence sure = null,cancel = null;
                    AlertDialog.Builder builder=new AlertDialog.Builder(cordova.getActivity());  //先得到构造器
                    try {
                        String title = message.getString("title");
                        String msg   = message.getString("message");
                        sure         = message.getString("sureTitle");
                        cancel       = message.getString("cancelTitle");
                        builder.setTitle(title); //设置标题
                        builder.setMessage(msg); //设置内容

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    builder.setPositiveButton(sure, new DialogInterface.OnClickListener() { //设置确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //关闭dialog
                            Toast.makeText(cordova.getActivity(), "确认" + which, Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() { //设置取消按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(cordova.getActivity(), "取消" + which, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //参数都设置完成了，创建并显示出来
                    builder.create().show();
                }
            });
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
