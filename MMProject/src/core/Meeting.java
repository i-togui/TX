package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;
import core.Tools;
import android.R;
import android.R.integer;
import android.media.MediaRecorder;
import android.os.Environment;
//import android.sax.Element;
import android.util.Log;
import android.util.Xml;

public class Meeting {
	
	String Title;
	Timestamp plannifiedDate;
	Timestamp realDate;
	String place;
	String directoryName;
	ArrayList<Attendee> attendeesList;
	ArrayList<Question> questionsList;
	ArrayList<Sequence> sequencesList;
	int state;
	Mp3Recorder recorder;
	
	
	
	XmlSerializer serializer = Xml.newSerializer();
	StringWriter writer = new StringWriter();
	/**
	 * 0 : just create!
	 */
	
	public Meeting(String title, Timestamp plannifiedDate, String place,
			ArrayList<Attendee> attendeesList, ArrayList<Question> questionsList) 
	{
		Title = title;
		this.plannifiedDate = plannifiedDate;
		this.place = place;
		this.attendeesList = attendeesList;
		this.questionsList = questionsList;
		this.state = 0;
		this.directoryName = "" + System.currentTimeMillis();
		File item_folder = new File(Tools.meetings_directory + "/" + this.directoryName);
		item_folder.mkdirs();
		
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
				serializer.setOutput(writer);
			    serializer.startDocument("UTF-8", true);
			    serializer.startTag("", "MeetingDescription");
					
			    	serializer.startTag("", "Title");
					serializer.text(this.Title);
					serializer.endTag("", "Title");
					
					serializer.startTag("", "Place");
					serializer.text(this.place);
					serializer.endTag("", "Place");
				    
					serializer.startTag("", "PlannifiedDate");
					serializer.text(this.plannifiedDate.toString());
					serializer.endTag("", "PlannifiedDate");
					
					serializer.startTag("", "State");
					serializer.text("" + this.getState());
					serializer.endTag("", "State");
					
					serializer.startTag("", "Attendees");
					for (Attendee item: attendeesList){
					            serializer.startTag("", "Attendee");
					            
					            String tmp = (item.isTheReporter())?"True":"False";
					            serializer.attribute("", "reporter", tmp);
					            
					            tmp = (item.isHere())?"True":"False";
					            serializer.attribute("", "here", tmp);
					            serializer.text(item.getName());
					            serializer.endTag("", "Attendee");
					}
				    serializer.endTag("", "Attendees");
				 
				    serializer.startTag("", "Questions");
					for (Question item: questionsList){
					            serializer.startTag("", "Question");
					            serializer.text(item.getQuestion());
					            serializer.endTag("", "Question");
					}
				    serializer.endTag("", "Questions");
				 
				 serializer.endTag("", "MeetingDescription");    
			     serializer.endDocument();
			     FileWriter fw = new FileWriter(item_folder + "/meetingDescription.xml");
			     fw.write(writer.toString());
			     fw.close();
		} 
		catch (Exception e) 
	    {
	        throw new RuntimeException(e);
	    } 
	}
	
	

	public Meeting(String directory)
	{
		//load data
		File item = new File(Tools.meetings_directory + "/" + directory + "/meetingDescription.xml");
		if (item.canRead())
		{
			this.directoryName = directory;
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new FileInputStream(item));
				doc.getDocumentElement().normalize();
				
				NodeList nodeList = doc.getElementsByTagName("Title");
				this.setTitle(nodeList.item(0).getTextContent());
				
				nodeList = doc.getElementsByTagName("Place");
				this.setPlace(nodeList.item(0).getTextContent());
				
				nodeList = doc.getElementsByTagName("State");
				this.setState(Integer.parseInt(nodeList.item(0).getTextContent()));
				
				nodeList = doc.getElementsByTagName("PlannifiedDate");
				String dat_tmp = nodeList.item(0).getTextContent();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
				Date parsedDate = dateFormat.parse(dat_tmp);
				
				Timestamp timestamp = new Timestamp(parsedDate.getTime());
				this.setPlannifiedDate(timestamp);
				
						
				
				nodeList = doc.getElementsByTagName("Attendee");
				this.attendeesList = new ArrayList<Attendee>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Attendee attendee = new Attendee(nodeList.item(i).getTextContent());
					NamedNodeMap listAttributes = nodeList.item(i).getAttributes();
					boolean bl = (listAttributes.getNamedItem("reporter").getNodeValue().compareTo("True") == 0)?true:false;
					attendee.setHere(bl);
					bl = (listAttributes.getNamedItem("here").getNodeValue().compareTo("True") == 0)?true:false;
					attendee.setTheReporter(bl);
					this.attendeesList.add(attendee);
				}
				
				nodeList = doc.getElementsByTagName("Question");
				this.questionsList = new ArrayList<Question>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					 this.questionsList.add(new Question(nodeList.item(i).getTextContent()));
				}
				
			} 
			catch (ParserConfigurationException e) 
			{
				e.printStackTrace();
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (SAXException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean switchState()
	{
		//RULES to verify before switching state!
		switch(this.state)
		{
			case 0 : return switch_to_state_one();
			case 1 : return switch_to_state_two();
			case 2 : return switch_to_state_three();
			default : return false;
		}
	}
	public boolean switch_to_state_one()
	{
		if(state != 0) return false;
		if(questionsList == null) return false;
		else
		{
			if(questionsList.size() == 0) return false;
		}
		if(attendeesList == null) return false;
		else
		{
			if(attendeesList.size() == 0) return false;
		}
		if(place == null) return false;
		if(Title == null) return false;
		if(plannifiedDate == null) return false;
		File item = new File(Tools.meetings_directory + "/" + this.directoryName + "/meetingDescription.xml");
		if (! item.canRead()) return false;
		state = 1;
		return true;
	}
	public boolean switch_to_state_two()
	{
		if(state != 1) return false;
		File item = new File(Tools.meetings_directory + "/" + this.directoryName + "/audio.mp3");
		if (! item.canRead()) return false;
		state = 2;
		return true;
	}
		
	public boolean switch_to_state_three()
	{
//		if (true) //all sequences are ok
			return true;
	}
	
	
	public void read_sequences()
	{
		File item = new File(Tools.meetings_directory + "/" + this.directoryName + "/AudioSequences.xml");
		if (item.canRead())
		{
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new FileInputStream(item));
				doc.getDocumentElement().normalize();
				
				
				NodeList nodeList = doc.getElementsByTagName("Sequence");
				this.sequencesList = new ArrayList<Sequence>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Sequence sequence = new Sequence();
					NamedNodeMap listAttributes = nodeList.item(i).getAttributes();
					sequence.setStartOffset(Float.parseFloat(listAttributes.getNamedItem("start").getNodeValue()));
					sequence.setWhoSpeacking(new Attendee(listAttributes.getNamedItem("attendee").getNodeValue()));
					sequence.setQuestion(new Question(listAttributes.getNamedItem("question").getNodeValue()));
					sequence.setType(new Item(listAttributes.getNamedItem("type").getNodeValue()));
					sequence.setComment(listAttributes.getNamedItem("comment").getNodeValue());
					NodeList criteriaList = nodeList.item(i).getChildNodes();
					sequence.criteriaList = new ArrayList<Item>();
					for (int j = 0; j < criteriaList.getLength(); i++) {
						Item critere = new Item(criteriaList.item(j).getNodeValue());
						sequence.criteriaList.add(critere);
					}
				}
				
				nodeList = doc.getElementsByTagName("Question");
				this.questionsList = new ArrayList<Question>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					 this.questionsList.add(new Question(nodeList.item(i).getTextContent()));
				}
				
			} 
			catch (ParserConfigurationException e) 
			{
				e.printStackTrace();
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (SAXException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void save_sequences()
	{
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
				serializer.setOutput(writer);
			    serializer.startDocument("UTF-8", true);
			    serializer.startTag("", "AudioSequences");
				for(int i=0; i<this.sequencesList.size();i++)
				{
					serializer.startTag("", "Sequence");
						serializer.attribute("", "start", "" + this.sequencesList.get(i).startOffset);
						serializer.attribute("", "attendee", this.sequencesList.get(i).getWhoSpeacking().getName());
						serializer.attribute("", "question", this.sequencesList.get(i).getQuestion().getQuestion());
						serializer.attribute("", "type", this.sequencesList.get(i).getType().data);
						serializer.text(this.sequencesList.get(i).getComment());
						for(int j=0; j<this.sequencesList.get(i).criteriaList.size();j++)
						{
							serializer.startTag("", "Criteria");
							serializer.text(sequencesList.get(i).criteriaList.get(j).data);
							serializer.endTag("", "Criteria");
						}
					serializer.endTag("", "Sequence");
				}
			    	serializer.startTag("", "Title");
					serializer.text(this.Title);
					serializer.endTag("", "Title");
					
					serializer.startTag("", "Place");
					serializer.text(this.place);
					serializer.endTag("", "Place");
				    
					serializer.startTag("", "PlannifiedDate");
					serializer.text(this.plannifiedDate.toString());
					serializer.endTag("", "PlannifiedDate");
					
					serializer.startTag("", "State");
					serializer.text("" + this.getState());
					serializer.endTag("", "State");
					
					serializer.startTag("", "Attendees");
					for (Attendee item: attendeesList){
					            serializer.startTag("", "Attendee");
					            
					            String tmp = (item.isTheReporter())?"True":"False";
					            serializer.attribute("", "reporter", tmp);
					            
					            tmp = (item.isHere())?"True":"False";
					            serializer.attribute("", "here", tmp);
					            serializer.text(item.getName());
					            serializer.endTag("", "Attendee");
					}
				    serializer.endTag("", "Attendees");
				 
				    serializer.startTag("", "Questions");
					for (Question item: questionsList){
					            serializer.startTag("", "Question");
					            serializer.text(item.getQuestion());
					            serializer.endTag("", "Question");
					}
				    serializer.endTag("", "Questions");
				 
				 serializer.endTag("", "AudioSequences");    
			     serializer.endDocument();
			     FileWriter fw = new FileWriter(Tools.meetings_directory + "/" + this.directoryName + "/AudioSequences.xml");
			     fw.write(writer.toString());
			     fw.close();
		} 
		catch (Exception e) 
	    {
	        throw new RuntimeException(e);
	    } 

	}
	
	
	
	
	public void reportGeneration()
	{
		try {

            Source xmlSource = new StreamSource("..");
            Source xsltSource = new StreamSource("..");

            TransformerFactory transFact = TransformerFactory.newInstance();
            Transformer trans = transFact.newTransformer(xsltSource);
            File f = new File(Tools.meetings_directory + "/" + this.directoryName + "/report.html");
            StreamResult result = new StreamResult(f);
            trans.transform(xmlSource, result);
        } 
		catch (TransformerConfigurationException e) 
		{
			e.printStackTrace();
        } 
		catch (TransformerFactoryConfigurationError e) 
		{
            e.printStackTrace();
        } catch (TransformerException e) 
        {
            e.printStackTrace();
        }
	}


	
	public void addQuestion(Question question)
	{
		this.questionsList.add(question);
		try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new File(Tools.meetings_directory + "/" + this.directoryName + "/meetingDescription.xml"));
		 
				Node questionsNode = doc.getElementsByTagName("Questions").item(0);
				Element newQuestion = doc.createElement("Question");
				newQuestion.appendChild(doc.createTextNode(question.getQuestion()));
				questionsNode.appendChild(newQuestion);
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(Tools.meetings_directory + "/" + this.directoryName + "/meetingDescription.xml"));
				transformer.transform(source, result);
		 
		} 
		catch (ParserConfigurationException pce) 
		{
				pce.printStackTrace();
		} 
		catch (IOException ioe) 
		{
				ioe.printStackTrace();
		} 
		catch (SAXException sae) 
		{
				sae.printStackTrace();
		} 
		catch (TransformerException e) 
		{
			e.printStackTrace();
		}
	}
	public void addCriteriaToSequence(float startOffset, Item critere)
	{
		for(int i=0;i<this.sequencesList.size();i++)
		{
			if(this.sequencesList.get(i).getStartOffset() == startOffset)
			{
				this.sequencesList.get(i).addCriteria(critere);
			}
		}
	}
	public void removeCriteria(float startOffset, Item critere)
	{
		for(int i=0;i<this.sequencesList.size();i++)
		{
			if(this.sequencesList.get(i).getStartOffset() == startOffset)
			{
				this.sequencesList.get(i).removeCriteria(critere);
			}
		}
	}
	
	
	
	public void startRecord()
	{
		this.realDate = new Timestamp(System.currentTimeMillis());
		recorder = new Mp3Recorder(Tools.meetings_directory + "/" + this.directoryName + "/audio.mp3");
		recorder.start();
		//creer XML avec
		try {
				serializer = Xml.newSerializer();
				writer = new StringWriter();
				serializer.setOutput(writer);
			    serializer.startDocument("UTF-8", true);
			    serializer.startTag("", "AudioSequences");			   
		} 
		catch (Exception e) 
	    {
	        throw new RuntimeException(e);
	    } 
	}
	public void appendRecord(Sequence sequence)
	{
		try 
		{
			serializer.startTag("", "Sequence");
				serializer.attribute("", "start","" + recorder.getOffset());
				serializer.attribute("", "attendee", sequence.getWhoSpeacking().getName());
				serializer.attribute("", "question", sequence.getQuestion().toString());
				serializer.attribute("", "type", sequence.getType().toString());
				if (sequence.getComment().length()>0)
				{
					serializer.startTag("", "Comment");
					serializer.text(sequence.getComment());
					serializer.endTag("", "Comment");
				}
		    serializer.endTag("", "Sequence");
		}
		catch (Exception e) 
		    {
		        throw new RuntimeException(e);
		    } 
}
	public void stopRecord()
	{
		try 
		{
			recorder.stop();
			serializer.endTag("", "AudioSequences");    
		    serializer.endDocument();
			FileWriter fw = new FileWriter(Tools.meetings_directory + "/" + this.directoryName + "/AudioSequences.xml");
			fw.write(writer.toString());
			fw.close();
		} 
		catch (Exception e) 
	    {
	        throw new RuntimeException(e);
	    } 
	}
	
	
	
	
	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public Date getPlannifiedDate() {
		return plannifiedDate;
	}

	public void setPlannifiedDate(Timestamp plannifiedDate) {
		this.plannifiedDate = plannifiedDate;
	}

	public Date getRealDate() {
		return realDate;
	}

	public void setRealDate(Timestamp realDate) {
		this.realDate = realDate;
	}

	@Override
	public String toString() {
		return "Meeting [Title=" + Title + ", plannifiedDate=" + plannifiedDate
				+ ", realDate=" + realDate + ", place=" + place
				+ ", attendeesList=" + attendeesList + ", questionsList="
				+ questionsList + ", sequencesList=" + sequencesList
				+ ", state=" + state + "]";
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return this.state;
	}
	public String getStateName()
	{
		switch (state)
		{
			case 0:return "NEW";
			default:return "UNDEFINED";
		}	
	}

	public static ArrayList getMeetingsList()
	{
		ArrayList <String>meetingsId = new ArrayList<String>();
		File meeting_folder = new File(Tools.meetings_directory);
		
		for (File inFile : meeting_folder.listFiles()) 
		{
		    if (inFile.isDirectory()) {
		    	meetingsId.add(inFile.getName());
		    }
		}
		return meetingsId;
	}
}
