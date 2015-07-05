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
	/*�P�_�O�_�������*/
	private static final int MSG_SUCCESS = 1;
	private static final int MSG_ERROR = 0;
	/*�򥻤���ŧi*/
	EditText Account,PassWord;
    TextView Result;
    Button Login;
    HttpURLConnection connect;
    /*�ŧihandler ��������thread*/
    Handler Boss =null;
    HandlerThread Staff = null;
    /*�n�s������m*/
    String location = "";
    String test = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		connect = null;
		/*������xml�W��id*/
        Account = (EditText)findViewById(R.id.Account);
        PassWord = (EditText)findViewById(R.id.PassWord);
        Result = (TextView)findViewById(R.id.Result);
        Login = (Button)findViewById(R.id.Login);
    	/*�]�wthread*/
        Staff = new HandlerThread("kevin");
        Staff.start();
        /*����handler*/
        Boss = new Handler(Staff.getLooper());
        
        
        Login.setOnClickListener(new Button.OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				// TODO �۰ʲ��ͪ���k Stub
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
	/*�гy�lthread*/
	Runnable r1 = new Runnable(){
		@Override
		public void run() {
			// TODO �۰ʲ��ͪ���k Stub
			try {
				URL url = new URL(location);
				/*�s�u�]�w*/
				connect = (HttpURLConnection)url.openConnection();
				connect.setDoOutput(true);
				connect.setChunkedStreamingMode(0);
				connect.setDoInput(true);
				connect.setRequestMethod("POST");
				connect.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				/*�s��*/
				connect.connect();
				/*�o�e�n�D*/
				DataOutputStream out = new DataOutputStream(connect.getOutputStream());
				String content1 ="Account=" + URLEncoder.encode(Account.getText().toString(),"utf-8");
				/*�Y�n�ĤG�ӰѼ� �H&�j�}*/
				String content2 ="&PassWord=" + URLEncoder.encode(PassWord.getText().toString(),"utf-8");
				out.writeBytes(content1);
				out.writeBytes(content2);
				out.flush();
				out.close();
				
				/*Ū�^�ǭ�*/
				BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream(),"UTF-8"));
				String line;
	            StringBuffer sb = new StringBuffer();
	            while((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            reader.close();
	            test = sb.toString();
	            /*�_�}�s��*/
				connect.disconnect();
				UIHandler.obtainMessage(MSG_SUCCESS,test).sendToTarget();;
				
			} catch (MalformedURLException e) {
				// TODO �۰ʲ��ͪ� catch �϶�
				e.printStackTrace();
			} catch (IOException e) {
				// TODO �۰ʲ��ͪ� catch �϶�
				e.printStackTrace();
			}
			
		}
		
	};
	
	protected void onDestroy() {

        super.onDestroy();

        //�����u�H�W���u�@

        if (Boss != null) {

            Boss.removeCallbacks(r1);

        }

        //�Ѹu�u�H (����Thread)

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
