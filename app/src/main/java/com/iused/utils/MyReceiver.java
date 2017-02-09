package com.iused.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.iused.introduction.MobileVerifyActivity;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        Bundle bundle = intent.getExtras();
        MobileVerifyActivity verif2 = new MobileVerifyActivity();
        final Object[] pdusObj = (Object[]) bundle.get("pdus");
//        Log.e("message","msg");

        for (int i = 0; i < pdusObj.length; i++) {

            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
            String phoneNumber = currentMessage.getDisplayOriginatingAddress();

            String senderNum = phoneNumber;
//            Log.e("sender",senderNum.toString());
//            Log.e("message1","msg");

            if (senderNum.endsWith("PLVSMS")) {
                String message = currentMessage.getDisplayMessageBody();
//                Log.e("message2","msg");
//                Log.e("message",message);
                try {
                    verif2.getInstace().validateOTP(context, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            else if (senderNum.startsWith("VM-PLVSMS")) {
//                String message = currentMessage.getDisplayMessageBody();
////                Log.e("message3","msg");
////                Log.e("message",message);
//                try {
//                    verif2.getInstace().validateOTP(context, message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            else if (senderNum.startsWith("BZ-PLVSMS")) {
//                String message = currentMessage.getDisplayMessageBody();
////                Log.e("message3","msg");
////                Log.e("message",message);
//                try {
//                    verif2.getInstace().validateOTP(context, message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            else if (senderNum.startsWith("DM-PLVSMS")) {
//                String message = currentMessage.getDisplayMessageBody();
////                Log.e("message3","msg");
////                Log.e("message",message);
//                try {
//                    verif2.getInstace().validateOTP(context, message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

}
