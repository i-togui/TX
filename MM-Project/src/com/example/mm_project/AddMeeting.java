package com.example.mm_project;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import core.Meeting;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
/*
 * L'activité qui permet d'ajouter un nouveau meeting
 */
public class AddMeeting extends Activity 
{
	ArrayList<String> ListeAttendee=new ArrayList<String>();
	ArrayAdapter<String> adapterListeAttendee;
	
	ArrayList<String> ListeQuestion=new ArrayList<String>();
	ArrayAdapter<String> adapterListeQuestion;    
	
	ArrayList<String> ListeMeetingDirectory=new ArrayList<String>();
	ArrayList<String> ListeMeetingName=new ArrayList<String>();
	
	ArrayAdapter<String> adapterListeMeeting;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_meeting);
		init_list();
		this.setFinishOnTouchOutside(false);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_meeting, menu);
		return true;
	}
	/*
	 * Relie les listViews avec les adapteurs
	 */
	public void init_list()
	{
		adapterListeAttendee=new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1,
	            ListeAttendee);
		adapterListeQuestion=new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1,
	            ListeQuestion);
		
		ListView lvListeAttendee = (ListView)findViewById(R.id.attendee_list);
	    lvListeAttendee.setAdapter(adapterListeAttendee);
	    
	    ListView lvListeQuestion = (ListView)findViewById(R.id.question_list);
	    lvListeQuestion.setAdapter(adapterListeQuestion);
	}
	/*
	 * Ajoute une question
	 */
	public void add_question(View v)
	{
		TextView text = (TextView) findViewById(R.id.text_question);
		if(text.getText().toString().length()>0)
		{
			ListeQuestion.add(text.getText().toString());
			adapterListeQuestion.notifyDataSetChanged();
			text.setText("");
		}
		else 
			Toast.makeText(getApplicationContext(), "Forbiden action:empty field", Toast.LENGTH_LONG).show();
	}
	/*
	 * Ajoute un attendee
	 */
	public void add_attendee(View v)
	{
		TextView text = (TextView) findViewById(R.id.text_attendee);
		if(text.getText().toString().length()>0)
		{
			ListeAttendee.add(text.getText().toString());
			adapterListeAttendee.notifyDataSetChanged();
			text.setText("");
		}
		else 
			Toast.makeText(getApplicationContext(), "Forbiden action:empty field", Toast.LENGTH_LONG).show();
	}
	/*
	 * La fonction qui crée un meeting et ferme l'activity par la suite
	 */
	public void createMeeting(View v)
	{
		TextView text = (TextView) findViewById(R.id.title);
		String title = text.getText().toString();
		
		text = (TextView) findViewById(R.id.place);
		String place = text.getText().toString();
		
		DatePicker dp = (DatePicker) findViewById(R.id.dpResult);
		TimePicker tm = (TimePicker) findViewById(R.id.timePicker1);
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tm.getCurrentHour(), tm.getCurrentMinute());
		long time = rightNow.getTimeInMillis();
		core.Meeting m = new Meeting(title, new Timestamp(time), place, core.Attendee.convertArray2Attendees(this.ListeAttendee), core.Question.convertArray2Questions(this.ListeQuestion)); 
		
		MeetingListActivity.refresh();
		addToCalendar(m, rightNow);
		Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
		this.finish();
	}
	/*
	 * La fonction qui ajoute la date du Meeting comme évenement dans le calendrier de l'utilisateurs
	 */
	public void addToCalendar(Meeting m, Calendar beginTime)
	{
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(beginTime.getTime());
		endTime.add(Calendar.HOUR, 2);
		
		Intent intent = new Intent(Intent.ACTION_INSERT)
		    .setData(Events.CONTENT_URI)
		    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
		    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
		    .putExtra(Events.TITLE, "Meeting - "+m.getTitle())
		    .putExtra(Events.EVENT_LOCATION, m.getPlace())
		    .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
		startActivity(intent);
	}

}
