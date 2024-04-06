package com.example.khabrii;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class ReceiveSms extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String msgFrom;
        if(bundle!=null){
            try{
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                String format = bundle.getString("format");
                for(int i=0; i<msgs.length; i++){
                    msgs[i]= SmsMessage.createFromPdu((byte[]) pdus[i],format);
                    msgFrom = msgs[i].getOriginatingAddress();
                    String msgBody =msgs[i].getMessageBody();

                    Toast.makeText(context, "FROM: "+ msgFrom + " BODY: " + msgBody, Toast.LENGTH_SHORT).show();

                    if(msgFrom.equals(MainActivity.savedFromPhone)||msgBody.contains(MainActivity.savedMsgContains)){
                        sendSms(MainActivity.savedFwdPhone,msgBody);
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    }

    private void sendSms(String desAddress,String msgBody ){
        SmsManager smsManagerSend = SmsManager.getDefault();
        smsManagerSend.sendTextMessage(desAddress,null,msgBody,null,null);
    }

}
