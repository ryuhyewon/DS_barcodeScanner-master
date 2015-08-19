package com.example.bera.ds_barcodescanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.zxing.Result;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
/**
 * Created by bera on 2015-05-31.
 * modified by hyewon on 2015-06-25
 */
public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private String nameresult;
    private String HALLOWEEN_ORANGE = "#FF7F27";
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scan);
        FrameLayout scanwrapper = (FrameLayout) findViewById(R.id.scan_view_wrapper);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        scanwrapper.addView(mScannerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    public void handleResult(Result rawResult) {
        int resultValue;
        String alert = getResources().getString(R.string.btn_scan_confirm);
        String fail=getResources().getString(R.string.error_message);
        String person=getResources().getString(R.string.person_message);
        String product=getResources().getString(R.string.product_message);
        resultValue = checkResult(rawResult);
        //rawResult=setTextColor(getResources().getColor(R.color.my_color));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Typeface face2=Typeface.createFromAsset(getAssets(), "NanumBarunGothicOTF.otf");
      //  NiftyDialogBuilder dialogBuilder= NiftyDialogBuilder.getInstance(this);


        if (resultValue == 1) {
            //등록 제품
            final Intent intent_scan = new Intent(this, ScannerActivity.class);
            final String raw = rawResult.toString();

            builder .setMessage(product+ "\n\n" + nameresult + "\n\n" +   "[" +rawResult.getText()+  "]" )
                    .setCancelable(false)
                    .setPositiveButton(R.string.btn_scan_yes, new DialogInterface.OnClickListener() {
                        // 확인 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mScannerView.startCamera();
                            startActivity(intent_scan);
                        }
                    });
            final AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        mScannerView.startCamera();
                    }
                    return true;
                }
            });
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            textView.setTextSize(20);
            textView.setTypeface(face2);
            textView.setGravity(Gravity.CENTER);
        }
        else if(resultValue == 2){
            //다른 판촉사원이 찍은 경우
            final Intent intent_scan = new Intent(this, ScannerActivity.class);
            final String raw = rawResult.toString();

            builder .setMessage(person + "\n\n" + nameresult + "\n\n" + "[" + rawResult.getText() + "]")
                    .setCancelable(false)
                    .setPositiveButton(R.string.btn_scan_yes, new DialogInterface.OnClickListener() {
                        // 확인 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mScannerView.startCamera();
                            startActivity(intent_scan);
                        }
                    });
            final AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        mScannerView.startCamera();
                    }
                    return true;
                }
            });
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            textView.setTextSize(20);
            textView.setTypeface(face2);
            textView.setGravity(Gravity.CENTER);
        }
        else{
            //미등록 제품
            final int requestCode = 1000;
            final Intent intent_scan = new Intent(this, ScannerActivity.class);
            final Intent intent_store = new Intent(this, StoreActivity.class);
            final String raw = rawResult.toString();

            builder.setMessage(fail + "\n\n" +  "[" + rawResult.getText() + "]" + "\n\n" + alert)
                    //.setMessage(R.string.btn_scan_confirm)
                    .setCancelable(false)
                    .setNegativeButton(R.string.btn_scan_confirm_no, new DialogInterface.OnClickListener() {
                        // 취소 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivity(intent_scan);
                        }
                    })
                    .setPositiveButton(R.string.btn_scan_confirm_yes, new DialogInterface.OnClickListener() {
                        // 확인 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton) {
                            intent_store.putExtra("key1", raw);
                            startActivityForResult(intent_store, requestCode);
                        }
            });

            final AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        mScannerView.startCamera();
                    }
                    return true;
                }
            });
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            textView.setTextSize(20);
            textView.setTypeface(face2);
            textView.setGravity(Gravity.CENTER);
        }
    }


    public int  checkResult(Result barCode) {
        String result = SendByHttp(barCode.toString());
        Log.d("data", result.toString());
        boolean startsWith = result.startsWith("010");
        if (result != ""&&  startsWith==false &&barCode != null){
            nameresult=result;
                    return 1;
                }
        else if(result != ""&& startsWith==true && barCode != null){
            nameresult=result;
            return 2;
        }
        else return 3;
    }

    /**
     * 서버에 데이터 보내는 메소드
     */
    private String SendByHttp(String bcd){
        if (bcd == null)
            bcd = "";
        String URL ="http://api.eatsight.com/FoodInfo/client/check_bcd.do";

        DefaultHttpClient client = new DefaultHttpClient();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 3000);
            HttpConnectionParams.setSoTimeout(params, 3000);
            HttpPost post = new HttpPost(URL + "?bcd=" + bcd);
            int timeout = 5000;
      /*데이터 받아오기*/
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
            HttpConnectionParams.setSoTimeout(httpParams, timeout);

            post.setParams(httpParams);
            HttpResponse response = client.execute(post);
            BufferedReader bufreader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), "utf-8")
            );
            String line = null;
            String result = "";

            while ((line = bufreader.readLine()) != null) {

                result += line;
            }
            // Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            String match = "[result{}:\"]";
            result =result.replaceAll(match, "");
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            client.getConnectionManager().shutdown();
            return "";
        }
    }


        @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.finish)
                        .setMessage(R.string.finish_message)
                        .setNegativeButton(R.string.finish_no, null)
                        .setPositiveButton(R.string.finish_yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 프로세스 종료.
                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());

                            }
                        })
                        .show();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}