package mmreport.ui;

import java.sql.Timestamp;
import java.util.ArrayList;

import mmreport.ui.R;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import core.DummyContent;
import core.Item;
import core.ListItem;
import core.Meeting;
import core.Sequence;
import core.Tools;

/**
 * A fragment representing a single Meeting detail screen. This fragment is
 * either contained in a {@link MeetingListActivity} in two-pane mode (on
 * tablets) or a {@link MeetingDetailActivity} on handsets.
 */
public class MeetingDetailFragment extends Fragment  implements
View.OnClickListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MeetingDetailFragment() {
	
	}
	
	@Override
	  public void onResume() {

	    super.onResume();
	    MeetingDetailFragment.currentMeeting = MeetingDetailFragment.currentMeeting.reload();
	    printMeetting();
	    load_dynamic_component(MeetingDetailFragment.currentMeeting.getState(),rootView); 
	   
		

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_meeting_detail,
				container, false);

		// Show the dummy content as text in a TextView.
//		if (mItem != null) {
//			((TextView) rootView.findViewById(R.id.meeting_detail))
//					.setText(mItem.meeting.getTitle());
//		}
		init_list(rootView);
		MeetingDetailFragment.currentMeeting = mItem.meeting;
		MeetingDetailFragment.rootView = rootView;
		currentB = savedInstanceState;
		printMeetting();
		
		return rootView;
	}

	static View rootView;
	static Bundle currentB;
	
	ArrayList<String> ListeAttendee=new ArrayList<String>();
	ArrayAdapter<String> adapterListeAttendee;
	ArrayList<String> ListeQuestion=new ArrayList<String>();
	ArrayAdapter<String> adapterListeQuestion;    
	ArrayList<String> ListeMeetingDirectory=new ArrayList<String>();
	ArrayList<String> ListeMeetingName=new ArrayList<String>();
	ArrayAdapter<String> adapterListeMeeting;
	public void init_list(View rootView)
	{
		adapterListeAttendee = new ArrayAdapter<String>(this.getActivity(),
	            android.R.layout.simple_list_item_1,
	            ListeAttendee){
				       @Override
				        public View getView(int position, View convertView,
				                ViewGroup parent) {
				            View view =super.getView(position, convertView, parent);

				            TextView textView=(TextView) view.findViewById(android.R.id.text1);

				            /*YOUR CHOICE OF COLOR*/
				            textView.setTextColor(Color.WHITE);

				            return view;
				        }
				};;
		adapterListeQuestion = new ArrayAdapter<String>(this.getActivity(),
	            android.R.layout.simple_list_item_1,
	            ListeQuestion){
				       @Override
				        public View getView(int position, View convertView,
				                ViewGroup parent) {
				            View view =super.getView(position, convertView, parent);

				            TextView textView=(TextView) view.findViewById(android.R.id.text1);

				            /*YOUR CHOICE OF COLOR*/
				            textView.setTextColor(Color.WHITE);

				            return view;
				        }
				};;
		
		ListView lvListeAttendee = (ListView)rootView.findViewById(R.id.attendee_list);
	    lvListeAttendee.setAdapter(adapterListeAttendee);
	    
	    
	    ListView lvListeQuestion = (ListView)rootView.findViewById(R.id.question_list);
	    lvListeQuestion.setAdapter(adapterListeQuestion);
	}
	private void printMeetting() 
	{
		MeetingDetailFragment.currentMeeting = MeetingDetailFragment.currentMeeting.reload();
		TextView text = (TextView) rootView.findViewById(R.id.title);
		text.setText(MeetingDetailFragment.currentMeeting.getTitle());
		
		text = (TextView) rootView.findViewById(R.id.place);
		text.setText(MeetingDetailFragment.currentMeeting.getPlace());
		ArrayList tmp = core.Attendee.convertAttendees2Array(MeetingDetailFragment.currentMeeting.getAttendeesList());
		
		ListeAttendee.clear();
		ListeAttendee.addAll(tmp);

		tmp = core.Question.convertQuestions2Array(MeetingDetailFragment.currentMeeting.getQuestionsList());
		ListeQuestion.clear();		
		ListeQuestion.addAll(tmp);
		
		adapterListeQuestion.notifyDataSetChanged();
		adapterListeAttendee.notifyDataSetChanged();
		
		
		Timestamp t = MeetingDetailFragment.currentMeeting.getPlannifiedDate();
		int day = Integer.parseInt(t.toString().split(" ")[0].split("-")[2]);
		int month = Integer.parseInt(t.toString().split(" ")[0].split("-")[1]);
		int year = Integer.parseInt(t.toString().split(" ")[0].split("-")[0]);
		int hour = Integer.parseInt(t.toString().split(" ")[1].split(":")[0]); 
		int min = Integer.parseInt(t.toString().split(" ")[1].split(":")[1]);

		text = (TextView) rootView.findViewById(R.id.date);
		text.setText(day+"/"+ (month) +"/"+year+ "  " +hour+":"+min);
		
		load_dynamic_component(MeetingDetailFragment.currentMeeting.getState(),rootView);
	}
	
	void load_dynamic_component(int i,View rootView)
	{
		LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.dynamicBloc);
		linearLayout.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
		
		
		
		switch(i)
		{
		case 0: inflater.inflate(R.layout.layout_state_1, linearLayout);
				ImageView im4 = (ImageView)rootView.findViewById(R.id.goNext1);
				im4.setOnClickListener(this);
				break;
		case 1: 
				inflater.inflate(R.layout.layout_state_2, linearLayout);
				ImageView im3 = (ImageView)rootView.findViewById(R.id.goNext2);
				im3.setOnClickListener(this);
				break;
		default:
				inflater.inflate(R.layout.layout_state_3, linearLayout);
				ImageView im1 = (ImageView)rootView.findViewById(R.id.sendMail);
				im1.setOnClickListener(this);
/**
 * Too add after
				ImageView im5 = (ImageView)rootView.findViewById(R.id.sendWC);
				im5.setOnClickListener(this);
	**/			ImageView im2 = (ImageView)rootView.findViewById(R.id.viewRepport);
				im2.setOnClickListener(this);
		}
	}

	final boolean checked_state[]={false,false,false,false,false};
	final CharSequence[] colors_check={"Audio File","Repport(PDF)","Repport(HTML)","Sequence XML File","MeetingsInfo XML File"};
	public void sendMail()
	{
		AlertDialog.Builder builder1=new AlertDialog.Builder(this.getActivity())
			.setTitle("Choose Files")
			.setMultiChoiceItems(colors_check, null, new DialogInterface.OnMultiChoiceClickListener() {
			 
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			// TODO Auto-generated method stub
			 
			//storing the checked state of the items in an array
			checked_state[which]=isChecked;
			}
			})
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			 
			@Override
			public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			

				ArrayList<Uri> pdfFiles = new ArrayList(); 
			for(int i=0;i<5;i++)
			{
				
				if(checked_state[i]==true){
					switch(i)
					{
					case 0 : pdfFiles.add(Uri.parse("file://"+Tools.meetings_directory + "/" + MeetingDetailFragment.currentMeeting.getDirectoryName() + "/audio.amr"));break;
					case 2 : pdfFiles.add(Uri.parse("file://"+Tools.meetings_directory + "/" + MeetingDetailFragment.currentMeeting.getDirectoryName() + "/rapport.html"));break;
					case 1 : pdfFiles.add(Uri.parse("file://"+Tools.meetings_directory + "/" + MeetingDetailFragment.currentMeeting.getDirectoryName() + "/rapport.pdf"));break;
					case 3 : pdfFiles.add(Uri.parse("file://"+Tools.meetings_directory + "/" + MeetingDetailFragment.currentMeeting.getDirectoryName() + "/AudioSequences.xml"));break;
					case 4 : pdfFiles.add(Uri.parse("file://"+Tools.meetings_directory + "/" + MeetingDetailFragment.currentMeeting.getDirectoryName() + "/meetingDescription.xml"));break;
					}
				}
			}
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
	        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
	        CharSequence seq = Html.fromHtml("<html></html>");
	        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, seq);
	        shareIntent.setType("application/pdf");

	        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, pdfFiles);//pdfFiles
	        startActivity(Intent.createChooser(shareIntent, "Send Dialog"));		
			 
			 
			//clears the array used to store checked state
			for(int i=0;i<checked_state.length;i++){
			if(checked_state[i]==true){
			checked_state[i]=false;
			}
			}
			 
			//used to dismiss the dialog upon user selection.
			dialog.dismiss();
			}
			});
			AlertDialog alertdialog1=builder1.create();
			alertdialog1.show();
		}
	
	Intent intent;
	static Meeting currentMeeting;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch (v.getId()) {
	      case R.id.sendMail:
	    	  sendMail();
	    	  break;
/**
 * To add after
 * <ImageView 
            android:id="@+id/sendWC"
		    android:layout_width="130dp"
	        android:layout_height="130dp"
	        android:layout_gravity="center_vertical"
            android:gravity="center_vertical"
            android:layout_marginLeft="110dp"
	        android:src="@drawable/share"
	        />
	         * 
	         * 
	         * 
	      case R.id.sendWC:
	    	  sendWC();
	    	  break;

	      
**/	      
	      case R.id.viewRepport:
	    	  	currentMeeting.reportGeneration();
	    	  	Uri uri  = Uri.parse("file://"+Tools.meetings_directory + "/" +this.currentMeeting.getDirectoryName() + "/rapport.pdf");
				try
				{
				    Intent intentUrl = new Intent(Intent.ACTION_VIEW);
				    intentUrl.setDataAndType(uri, "application/pdf");
				    intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    v.getContext().startActivity(intentUrl);
				}
				catch (ActivityNotFoundException e)
				{
				    Toast.makeText(v.getContext(), "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
				}
	    	  break;
	      case R.id.goNext2:
		    	intent=new Intent(v.getContext(), TagActivity.class);
		    	intent.putExtra("meeting", MeetingDetailFragment.currentMeeting.getDirectoryName());
		    	startActivity(intent);
	    	  break;
	      case R.id.goNext1:
	    	    intent=new Intent(v.getContext(), RecorderActivity.class);
				intent.putExtra("meeting", MeetingDetailFragment.currentMeeting.getDirectoryName());
		    	startActivity(intent);
	    	  break;
	    	  
	      }
	}
	
	
	void sendWC()
	{
		MeetingDetailFragment.currentMeeting.read_sequences();
		ArrayList<Sequence> SL = MeetingDetailFragment.currentMeeting.sequencesList;
		ListItem l = new ListItem("Windchill-Local", true);
		ArrayList<String> result = new ArrayList();
		for(Sequence seq : SL)
		{
			for(Item item : seq.getItemsList())
			{
				for(Item item_tmp: l.getList())
				{
					if(item.toString().compareTo(item_tmp.toString())==0)
					{
						if(result.contains(item_tmp.toString())==false)
						{
							result.add(item_tmp.toString());
						}
					}
				}
			}
		}
		Toast.makeText(this.getActivity().getApplicationContext(), "Composants : "+ result.toString(), Toast.LENGTH_LONG).show();
	}
}
