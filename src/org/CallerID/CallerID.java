package org.CallerID;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.graphics.PorterDuff.Mode;


import android.net.LocalServerSocket;
import android.net.ParseException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;
import java.lang.*;
import java.net.*;
import java.text.MessageFormat;
import java.util.Enumeration;

public class CallerID<Main> extends Activity {
int ii=0;
	int sersoc=22333;
	int pass_k=12345;
	String cli_ip="192.168.1.2";
	String loc_ip;
	String pinger;
	
	
	PScanner ps_ob = new PScanner();
	EditText et_ip;
	EditText et_pass,mon;
	TextView tvstate;
	String macAddr;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
         Button scan = (Button)findViewById(R.id.scan);
         et_ip = (EditText)findViewById(R.id.et_ip);
         et_pass = (EditText)findViewById(R.id.et_pass);
         mon = (EditText)findViewById(R.id.mon);
         tvstate = (TextView)findViewById(R.id.tvstate);
         scan.setOnClickListener(lis_clk);
        
     	
         WifiManager wifiMan = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
     	WifiInfo wifiInf = wifiMan.getConnectionInfo();
     	 macAddr = wifiInf.getMacAddress();  
     	tvstate.setText("Not Connected..");
        
    }
   
    
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
           
        }
        return null;
    }
    
    View.OnClickListener lis_clk = new OnClickListener()
    {
		@Override
		public void onClick(View v)
		{
			tvstate.setText("Connecting..");
			mon.setText("System Started...\n");
			pass_k= Integer.parseInt(et_pass.getText().toString());
			cli_ip=et_ip.getText().toString();
			
			loc_ip=getLocalIpAddress().toString();
			
			
			
			PScanner pps =new PScanner();
			pps.doit();
		//	pps.destroy();
			
			tvstate.setText("Scanning..");
			PScanner for_thr =new PScanner();
			for_thr.start();
			
			
		}
	};
	
	
	
	public class PScanner extends Thread
	{
		public void doit()
		{
			
			try {
				//et_ip.et
				String pinger = "A-"+macAddr+"-"+loc_ip+"-"+String.valueOf(sersoc)+"-"+String.valueOf(pass_k)+"";
				
				
				
				// sender
				Socket clsoc = new Socket(InetAddress.getByName(cli_ip),32323);
				
				BufferedWriter brw_to_cli = new BufferedWriter(new OutputStreamWriter(clsoc.getOutputStream()));
				brw_to_cli.write(pinger);
			    brw_to_cli.flush();
			    clsoc.close();			    
			    mon.append("Android IP ");
			    mon.append(getLocalIpAddress());
			    mon.append("\nServer IP  ");
			    mon.append(cli_ip);
			    mon.append("\n");
			    mon.append(macAddr.toString());
			    mon.append("\n\n\n");
				// listner
			    ServerSocket serversoc = new ServerSocket(22333);			   
				Socket svock=serversoc.accept();				
				   BufferedReader brw_to_svr = new BufferedReader (new InputStreamReader( svock.getInputStream()));
				
				String line = brw_to_svr.readLine();
				if(line.equalsIgnoreCase("S"))
					//	mon.setText(String.valueOf(ii));
				et_pass.getBackground().setColorFilter(getResources().getColor(R.color.but_col),Mode.MULTIPLY);
				svock.close();
				serversoc.close();

				
			} catch (IOException e) {
			//	tvstate.setText("Scanning Ended..");
			}
			
		}
		
		public void run()
		{
			// listner
//	/*	    
			try {
			
				while(true)
				{
					ServerSocket serversoc = new ServerSocket(11333);					   
					Socket svock=serversoc.accept();				
					   BufferedReader brw_to_svr = new BufferedReader (new InputStreamReader( svock.getInputStream()));
					   
						Socket clsoc = new Socket(InetAddress.getByName(cli_ip),31313);
						
						BufferedWriter brw_to_cli = new BufferedWriter(new OutputStreamWriter(clsoc.getOutputStream()));
						brw_to_cli.write("S");
					    brw_to_cli.flush();
					    clsoc.close();
					   
					String line = brw_to_svr.readLine();
					if(line.equalsIgnoreCase("S"))
					{
						
					}
					else
					{
						Message mmsg =mRedrawHandler.obtainMessage(0,line.toString());
						mRedrawHandler.sendMessage(mmsg);
					}
					//et_pass.getBackground().setColorFilter(getResources().getColor(R.color.but_col),Mode.MULTIPLY);
					svock.close();
					serversoc.close();
				
			
				}
			
			
			} catch (IOException e) {
			//	e.printStackTrace();
				}
//	*/	
			}
				
	}
	
	private RefreshHandler mRedrawHandler = new RefreshHandler();  
	  
	  class RefreshHandler extends Handler {  
	    @Override  
	    public void handleMessage(Message msg) {  
	    	mon.append(msg.obj.toString()); 
	    	mon.append("\n");
	    }

		public void dispatchMessage(String msg) {
			mon.setText(msg.toString());
			mon.append("\n");
			
		} 
	   }  
	
	
	
}

