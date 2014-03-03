package core;

import java.util.Calendar;

import com.example.mm_project.R;

import android.content.Intent;
import android.text.Html;
import android.util.Log;

public class Tools {
	public final static String meetings_directory = "/storage/extSdCard/MM/MEETINGS";
	public final static String items_directory = "/storage/extSdCard/MM/ITEMS";
	public final static String extra_directory = "/storage/extSdCard/MM/EXTRA";
	public static void show(String data)
	{
	    Log.e("stuff", data);
	}
	public void addCal()
	{
		Calendar cal = Calendar.getInstance();              
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", cal.getTimeInMillis());
		intent.putExtra("allDay", true);
		intent.putExtra("rrule", "FREQ=YEARLY");
		intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
		intent.putExtra("title", "A Test Event from android app");
		//startActivity(intent);
	}
	public void sendMail()
	{
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        CharSequence seq = Html.fromHtml("<html></html>");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, seq);
        shareIntent.setType("application/pdf");

        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, null);
        
	}

}
