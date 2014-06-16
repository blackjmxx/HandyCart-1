package com.example.handycart;


import android.os.Binder;

//Binder impl�mente IBinder
public class BackgroundServiceBinder extends Binder {
	
	private IMyServiceMethod mService; 
	  
	//on recoit l'instance du service
    public BackgroundServiceBinder(IMyServiceMethod service) { 
        super(); 
        mService = service; 
    } 
 
    /** @return l'instance du service */
    public IMyServiceMethod getService(){ 
        return mService; 
    } 
    
    /** les m�thodes de cette interface seront accessibles par l'activit� */
    public interface IMyServiceMethod { 
    	public String getDataFromService();	
    	public void setDataToService(String data);
    }
}
