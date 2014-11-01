/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tavant.machinelearning.notifications;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.json.JSONObject;


public class ApnsCommunication {

	public static void main(String[] args) {
		ApnsCommunication  mApnsCommunication    =  new ApnsCommunication("e08455ece833084908a4b3948836322db6cfe93869e589c723be2449a1d54024", 1, "Your front door is unlocked, at unusual time, please check!",5);
		System.out.println("Done ########################################");
	}
    public ApnsCommunication(String deviceToken,int type ,String msgbody,int badge) {
       // this.sentNotification(deviceToken, requesterName, buddyName,msgbody,badge,otheruserId,eventId);
        
        this.sentNotification(deviceToken,type,msgbody,badge);
   }
    public void sentNotification(String deviceToken,int type ,String msgbody,int badge){
        try{
            int port                    =   2195; // default https port
            String host                 =   GlobalData.host;
            String    token             =   deviceToken;
            SSLContext sslContext       =   SSLContext.getInstance("SSL");
            char []passwKey             =   "Welcome123".toCharArray();
            KeyStore ts                 =   KeyStore.getInstance("PKCS12");
            FileInputStream mFileInputStream    =   new FileInputStream(GlobalData.Certificate);
            ts.load(mFileInputStream, passwKey);
            KeyManagerFactory tmf       = KeyManagerFactory.getInstance("SunX509");
            tmf.init(ts,passwKey);
            sslContext.init(tmf.getKeyManagers(), null, new SecureRandom());
            SSLSocketFactory factory    = (SSLSocketFactory) sslContext.getSocketFactory();
            SSLSocket socket            = (SSLSocket) factory.createSocket(host, port);
            socket.addHandshakeCompletedListener(mHandshakeCompletedListener);
            socket.startHandshake();
            
            
            OutputStream outputStream   =   socket.getOutputStream();
            APNSMessage         message =   new APNSMessage(token, getPayLoad(type,msgbody,badge).toString());
            ByteArrayOutputStream baos  =   new ByteArrayOutputStream();
            DataOutputStream dos        =   new DataOutputStream(baos);
            dos.writeByte(APNSMessage.COMMAND);
            dos.writeShort(APNSMessage.getDeviceTokenLenght());
            dos.write(APNSMessage.getDeviceToken());
            dos.writeShort(APNSMessage.getPayloadLength());
            dos.write(APNSMessage.getPayLoad());
            byte[] output               =   baos.toByteArray();
            outputStream.write(output);
            outputStream.flush();
            
            
             //Incoming
            BufferedReader in           =   new BufferedReader
             (new InputStreamReader(socket.getInputStream()));
            StringBuilder buffer = new StringBuilder();
    		
    			char[] tmp = new char[1024];
    			String userinput="";
    			while ((userinput = in.readLine()) != null) {
    				System.out.println("userinput"+userinput);
    			}
    	
        
            outputStream.close();
            in.close();
            socket.close();
            mFileInputStream.close();
            
            //System.out.print("response"+buffer);
      }catch(Exception e){
          //socket.close();
    	  e.printStackTrace();
          System.out.print(""+e.getMessage()+""+e.getStackTrace()+" "+e.getCause());
      }
    }
    static HandshakeCompletedListener mHandshakeCompletedListener = new HandshakeCompletedListener()
    {
    	@Override
        public void handshakeCompleted(HandshakeCompletedEvent arg0) {
            try{
            	System.out.print("Handshake  success");
            }catch(Exception e){
                 System.out.print("Handshake  Exception");
            }
        }
     };
    public static JSONObject getPayLoad(int type ,String msgbody,int badge){

      try{
                JSONObject mainobj  =   new JSONObject();
                JSONObject apsobj   =   new JSONObject();
                JSONObject alertobj =   new JSONObject();
                alertobj.put("body",msgbody);
                alertobj.put("action-loc-key","Open");
                apsobj.put("alert",alertobj);
                apsobj.put("screenID", 2);
                apsobj.put("badge", badge);
                apsobj.put("sound", "kS.caf");
                mainobj.put("acme1", "bar");
                mainobj.put("aps", apsobj);
                return mainobj;              
      }catch(Exception e){
          return null;
      }
  }
      
}
