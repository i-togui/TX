package com.example.mm_project;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import core.Meeting;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
		final Calendar cal = Calendar.getInstance();
		phour = cal.get(Calendar.HOUR_OF_DAY);
		pminute = cal.get(Calendar.MINUTE);
		pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        pPickDate = (Button) findViewById(R.id.buttonDate);
        pPickTime = (Button) findViewById(R.id.buttonTime);
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

	boolean timeIsSet=false;
	boolean dateIsSet=false;	
	Button pPickDate;
	Button pPickTime;
	private int phour = -1;
	private int pminute = -1;
	private int pYear = -1;
    private int pMonth = -1;
    private int pDay = -1;
    
	static final int TIME_DIALOG_ID = 999;
	static final int DATE_DIALOG_ID = 998;
	public void chooseDate(View v)
	{
		showDialog(DATE_DIALOG_ID);	
	}
	public void chooseTime(View v)
	{
		showDialog(TIME_DIALOG_ID);
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, phour, pminute,false);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, pDateSetListener, pYear, pMonth, pDay);
 
		}
		return null;
	}
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			phour = selectedHour;
			pminute = selectedMinute;
			pPickTime.setText(phour+" : "+pminute);
			timeIsSet = true;
					}
	};
	private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
 
                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    pPickDate.setText(pDay+" / "+(pMonth+1)+" / " +pYear);
                    dateIsSet = true;
                }
            };
	
	
	public void createMeeting(View v)
	{

		if(timeIsSet == false)
		{
			Toast.makeText(getApplicationContext(), "Please choose a time first!", Toast.LENGTH_LONG).show();
			return;
		}

		if(dateIsSet ==false)
		{
			Toast.makeText(getApplicationContext(), "Please choose a date first!", Toast.LENGTH_LONG).show();
			return;
		}
		TextView text = (TextView) findViewById(R.id.title);
		String title = text.getText().toString();
		if(title.trim().length()==0)
		{
			Toast.makeText(getApplicationContext(), "Title is missing", Toast.LENGTH_LONG).show();
			return;
		}
		text = (TextView) findViewById(R.id.place);
		String place = text.getText().toString();
		if(place.trim().length()==0)
		{
			Toast.makeText(getApplicationContext(), "Place is missing..", Toast.LENGTH_LONG).show();
			return;
		}
		if(this.ListeAttendee.size()==0)
		{
			Toast.makeText(getApplicationContext(), "Attendees list is empty..", Toast.LENGTH_LONG).show();
			return;
		}
		if(this.ListeQuestion.size()==0)
		{
			Toast.makeText(getApplicationContext(), "Questions list is empty..", Toast.LENGTH_LONG).show();
			return;
		}
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(this.pYear, this.pMonth, this.pDay, this.phour, this.pminute);
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
