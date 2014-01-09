package core;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.MediaRecorder;

public class Mp3Recorder 
{
	MediaRecorder recorder = new MediaRecorder();
	long startTime;
	Mp3Recorder(String path)
	{
		try 
		{
			recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			File output = new File(path);
			FileOutputStream writer = new FileOutputStream(output);
			FileDescriptor fd = writer.getFD();
			recorder.setOutputFile(fd);		   
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		catch (IllegalStateException e) 
		{
		    e.printStackTrace();
		} 
		catch (IOException e) 
		{
		    e.printStackTrace();
		}
	}
	
	
	public void start()
	{
	    try 
	    {
			recorder.prepare();
			recorder.start();
		    startTime = System.nanoTime();
	    } 
	    catch (IllegalStateException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	public long stop()
	{
	    try 
	    {
	    	if (recorder != null) 
	    	{
	    		recorder.stop();
	    		recorder.release();
	    		recorder = null;
	    		return this.getOffset();
	    	}
	    	return 0;
	    } 
	    catch (IllegalStateException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} 
	}
	public long getOffset()
	{
		return System.nanoTime() - startTime;
	}
}
