package com.example.mmproject;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import core.Attendee;
import core.Item;
import core.ListItem;
import core.Meeting;
import core.Question;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	  
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void stuff(View v)
    {
    	//show("Teeeeeeeeeeeeeeeeest");
    	//text = (TextView) findViewById(R.id.textView1);
    	//text.setText("Teeeeeeeeeeest");


		//    	ListItem l = new ListItem("critères","Red");
//    	l.addItem("toto")"
//    	l.addItem("tata");
//    	l.addItem("titi");
//    	l.removeItem("tata");
//    	l.save();
//    	ArrayList<Question> questionsList = new ArrayList<Question>();
//    	questionsList.add(new Question("Q1"));
//    	questionsList.add(new Question("Q2"));
//    	questionsList.add(new Question("Q3"));
//    	ArrayList<Attendee> attendeesList = new ArrayList<Attendee>();
//    	attendeesList.add(new Attendee("A1"));
//    	Attendee a = new Attendee("A2");
//    	a.setHere(false);
//    	attendeesList.add(a);
//    	Attendee aa = new Attendee("A3");
//    	aa.setTheReporter(true);
//    	attendeesList.add(aa);
////    	Meeting m = new Meeting("Tiiiitle1", new Timestamp(System.currentTimeMillis()), "SCD", attendeesList, questionsList);
////    	Meeting m1 = new Meeting("Tiiiitle2", new Timestamp(System.currentTimeMillis()), "SCD", attendeesList, questionsList);
////    	Meeting m2 = new Meeting("Tiiiitle3", new Timestamp(System.currentTimeMillis()), "SCD", attendeesList, questionsList);
//    	Meeting test = new Meeting(Meeting.getMeetingsList().get(0).toString());
//    	show(Meeting.getMeetingsList().get(0).toString());
//    	show(test.toString());
    	//Meeting.read_sequences();
    }
    
	private void show(String data)
	{
	    Log.e("stuff", data);
	}
}