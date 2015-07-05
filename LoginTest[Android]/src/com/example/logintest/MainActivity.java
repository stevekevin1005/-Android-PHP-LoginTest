package com.example.logintest;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	/*判斷是否有收到值*/
	private static final int MSG_SUCCESS = 1;
	private static final int MSG_ERROR = 0;
	/*基本元件宣告*/
	EditText Account,PassWord;
    TextView Result;
    Button Login;
    HttpURLConnection connect;
    /*宣告handler 跟其對應的thread*/
    Handler Boss =null;
    HandlerThread Staff = null;
    /*要連結的位置*/
    String location = "";
    String test = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		connect = null;
		/*對應到xml上的id*/
        Account = (EditText)findViewById(R.id.Account);
        PassWord = (EditText)findViewById(R.id.PassWord);
        Result = (TextView)findViewById(R.id.Result);
        Login = (Button)findViewById(R.id.Login);
    	/*設定thread*/
        Staff = new HandlerThread("kevin");
        Staff.start();
        /*指派handler*/
        Boss = new Handler(Staff.getLooper());
        
        
        Login.setOnClickListener(new Button.OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				// TODO 自動產生的方法 Stub
				Boss.post(r1);
			};
			
        });
	}
	/*UI thread*/
	private Handler UIHandler = new Handler() {
		public void handleMessage (Message msg){
		switch(msg.what){	
			case MSG_SUCCESS:
			Result.setText(test);
			break;	
			}
		}
	};
	/*創造子thread*/
	Runnable r1 = new Runnable(){
		@Override
		public void run() {
			// TODO 自動產生的方法 Stub
			try {
				URL url = new URL(location);
				/*連線設定*/
				connect = (HttpURLConnection)url.openConnection();
				connect.setDoOutput(true);
				connect.setChunkedStreamingMode(0);
				connect.setDoInput(true);
				connect.setRequestMethod("POST");
				connect.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				/*連結*/
				connect.connect();
				/*發送要求*/
				DataOutputStream out = new DataOutputStream(connect.getOutputStream());
				String content1 ="Account=" + URLEncoder.encode(Account.getText().toString(),"utf-8");
				/*若要第二個參數 以&隔開*/
				String content2 ="&PassWord=" + URLEncoder.encode(PassWord.getText().toString(),"utf-8");
				out.writeBytes(content1);
				out.writeBytes(content2);
				out.flush();
				out.close();
				
				/*讀回傳值*/
				BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream(),"UTF-8"));
				String line;
	            StringBuffer sb = new StringBuffer();
	            while((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            reader.close();
	            test = sb.toString();
	            /*斷開連結*/
				connect.disconnect();
				UIHandler.obtainMessage(MSG_SUCCESS,test).sendToTarget();;
				
			} catch (MalformedURLException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace();
			}
			
		}
		
	};
	
	protected void onDestroy() {

        super.onDestroy();

        //移除工人上的工作

        if (Boss != null) {

            Boss.removeCallbacks(r1);

        }

        //解聘工人 (關閉Thread)

        if (Staff != null) {

            Staff.quit();

        }

    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
