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
	/**
	 * @param args
	 */
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

        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, null);//pdfFiles
//        startActivity(Intent.createChooser(shareIntent, "getText(R.string.share_chooser_title)"));
        //pdfFiles is of type ArrayList<Uri>.
        
        
        
        /*OR
         * 
Intent intent = new Intent(Intent.ACTION_SEND);
intent.setType("text/plain");
intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
intent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
intent.putExtra(Intent.EXTRA_TEXT, "body text");
File root = Environment.getExternalStorageDirectory();
File file = new File(root, xmlFilename);
if (!file.exists() || !file.canRead()) {
    Toast.makeText(this, "Attachment Error", Toast.LENGTH_SHORT).show();
    finish();
    return;
}

Uri uri = Uri.fromFile(file);
intent.putExtra(Intent.EXTRA_STREAM, uri);
startActivity(Intent.createChooser(intent, "Send email..."));
         * 
         */
        
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
