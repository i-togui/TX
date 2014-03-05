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
	public ArrayList<Sequence> sequencesList;
	int state;
	Mp3Recorder recorder;
	XmlSerializer serializer = Xml.newSerializer();
	StringWriter writer = new StringWriter();

	
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
					attendee.setTheReporter(bl);
					
					bl = (listAttributes.getNamedItem("here").getNodeValue().compareTo("True") == 0)?true:false;
					attendee.setHere(bl);
					
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
	public Meeting reload()
	{
		return new Meeting(this.getDirectoryName());
	}

	
	int getIndex(ArrayList l,String s)
	{
		
		for(int i=0;i<l.size();i++)
		{
			if(l.get(i).toString().compareTo(s) == 0) return i;
		}
		return -1;
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
	public void removeQuestion(String question)
	{
		this.questionsList.remove(getIndex(this.questionsList,question));
		try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new File(Tools.meetings_directory + "/" + this.directoryName + "/meetingDescription.xml"));
				
				Node questionsNode = doc.getElementsByTagName("Questions").item(0);
				NodeList questionNode = doc.getElementsByTagName("Question");
				for(int i=0;i<questionNode.getLength();i++)
				{
					if(questionNode.item(i).getTextContent().toString().compareTo(question)==0)
					{
						questionsNode.removeChild(questionNode.item(i));
					}
				}
				
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
	public void addAttendee(Attendee attendee)
	{
		this.attendeesList.add(attendee);
		try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new File(Tools.meetings_directory + "/" + this.directoryName + "/meetingDescription.xml"));
		 
				Node attendeeNode = doc.getElementsByTagName("Attendees").item(0);
				Element newAttendee = doc.createElement("Attendee");
				newAttendee.appendChild(doc.createTextNode(attendee.getName()));
				newAttendee.setAttribute("reporter", attendee.isTheReporter()?"True":"False");
				newAttendee.setAttribute("here", attendee.isHere()?"True":"False");
				attendeeNode.appendChild(newAttendee);
				
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
	public boolean isTheReporter(String string)
	{
		for(int i=0;i<this.getAttendeesList().size();i++)
		{
			if(this.getAttendeesList().get(i).name.compareTo(string)==0)
				return this.getAttendeesList().get(i).isTheReporter;
		}
		return false;
	}
	public void removeAttendee(String string) {
		this.attendeesList.remove(getIndex(this.attendeesList,string));
		try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new File(Tools.meetings_directory + "/" + this.directoryName + "/meetingDescription.xml"));
				
				Node questionsNode = doc.getElementsByTagName("Attendees").item(0);
				NodeList questionNode = doc.getElementsByTagName("Attendee");
				for(int i=0;i<questionNode.getLength();i++)
				{
					if(questionNode.item(i).getTextContent().toString().compareTo(string)==0)
					{
						questionsNode.removeChild(questionNode.item(i));
					}
				}
				
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
	public void setRepporter(String attendee)
	{
		this.attendeesList.remove(getIndex(this.attendeesList,attendee));
		Attendee a = new Attendee(attendee);
		a.setTheReporter(true);
		this.attendeesList.add(a);
		this.removeAttendee(attendee);
		this.addAttendee(a);
	}
	public void alterState()
	{
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(Tools.meetings_directory + "/" + this.directoryName + "/meetingDescription.xml"));
	 
				
			Node rootNode = doc.getElementsByTagName("MeetingDescription").item(0);
			Node stateNode = doc.getElementsByTagName("State").item(0);
			rootNode.removeChild(stateNode);
			
			Element newState = doc.createElement("State");
			newState.appendChild(doc.createTextNode(""+this.getState()));
			rootNode.appendChild(newState);
				
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
	
	public void save_sequence(String currentOffset, ArrayList<String> listeItems) 
	{
		try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new File(Tools.meetings_directory + "/" + this.directoryName + "/AudioSequences.xml"));
				
				NodeList sequences = doc.getElementsByTagName("Sequence");
				NamedNodeMap listAtt;
				for(int i=0;i<sequences.getLength();i++)
				{
					listAtt = sequences.item(i).getAttributes();
					if(listAtt.getNamedItem("finish").getNodeValue().compareTo(currentOffset)==0)
					{
						NodeList childNodes = sequences.item(i).getChildNodes();
					    int length = childNodes.getLength();
					    for (int j = 0; j < length; j++) {
					        	Node childNode = childNodes.item(j);
					        	sequences.item(i).removeChild(childNode);  
					        }
					    for(int k = 0;k<listeItems.size();k++)
					    {
					    	Element newAttendee = doc.createElement("Item");
							newAttendee.appendChild(doc.createTextNode(listeItems.get(k).toString()));
							sequences.item(i).appendChild(newAttendee);
								
					    }
					    break;
					}
				}
				
				
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(Tools.meetings_directory + "/" + this.directoryName + "/AudioSequences.xml"));
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
	
	
	
	public void addItemToSequence(float finishOffset, Item critere)
	{
		for(int i=0;i<this.sequencesList.size();i++)
		{
			if(this.sequencesList.get(i).getfinishOffset() == finishOffset)
			{
				this.sequencesList.get(i).addCriteria(critere);
			}
		}
	}
	public void removeItemFromSequence(float startOffset, Item critere)
	{
		for(int i=0;i<this.sequencesList.size();i++)
		{
			if(this.sequencesList.get(i).getfinishOffset() == startOffset)
			{
				this.sequencesList.get(i).removeCriteria(critere);
			}
		}
	}
	

	
	public Sequence read_sequence(String offset)
	{
		for(int i=0;i<this.sequencesList.size();i++)
		{
			if(String.valueOf(this.sequencesList.get(i).getfinishOffset()).compareTo(offset)==0)
				return this.sequencesList.get(i);
		}
		return null;
	}
	public String getThe0ffset(String offset)
	{
		for(int i=0;i<this.sequencesList.size();i++)
		{
			if( Float.parseFloat(offset) <= this.sequencesList.get(i).getfinishOffset() )
			{
				return String.valueOf(this.sequencesList.get(i).getfinishOffset());
			}
		}
		return null;
	}
	public String getStartOffset(String offset)
	{
		String bg = "0.0";
		for(int i=0;i<this.sequencesList.size();i++)
		{
			if( Float.parseFloat(offset) > this.sequencesList.get(i).getfinishOffset() )
			{
				bg = String.valueOf(this.sequencesList.get(i).getfinishOffset());
			}
			else
			{
				break;
			}
		}
		return bg;	
	}
	/**
	 *  7  > 10
	 *  15 > 10
	 *  3
	 *  5 10 20 30
	 */
	
	

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
					sequence.setfinishOffset(Float.parseFloat(listAttributes.getNamedItem("finish").getNodeValue()));
					sequence.setWhoSpeacking(new Attendee(listAttributes.getNamedItem("attendee").getNodeValue()));
					sequence.setQuestion(new Question(listAttributes.getNamedItem("question").getNodeValue()));
					if(listAttributes.getNamedItem("Comment") != null)
						sequence.setComment(listAttributes.getNamedItem("Comment").getNodeValue());
					else
						sequence.setComment("");
					
					
					
					NodeList criteriaList = nodeList.item(i).getChildNodes();
					sequence.itemsList = new ArrayList<Item>();
					for (int j = 0; j < criteriaList.getLength(); j++) {
						if(criteriaList.item(j).getNodeType() == Node.ELEMENT_NODE)
						{
							Item critere = new Item(criteriaList.item(j).getTextContent());
							sequence.itemsList.add(critere);
						}
					}
					this.sequencesList.add(sequence);
				}
				
//				nodeList = doc.getElementsByTagName("Question");
//				this.questionsList = new ArrayList<Question>();
//				for (int i = 0; i < nodeList.getLength(); i++) {
//					 this.questionsList.add(new Question(nodeList.item(i).getTextContent()));
				//}
				
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
						serializer.attribute("", "finish", "" + this.sequencesList.get(i).getfinishOffset());
						serializer.attribute("", "attendee", this.sequencesList.get(i).getWhoSpeacking().getName());
						serializer.attribute("", "question", this.sequencesList.get(i).getQuestion().getQuestion());
						serializer.text(this.sequencesList.get(i).getComment());
						for(int j=0; j<this.sequencesList.get(i).itemsList.size();j++)
						{
							serializer.startTag("", "Criteria");
							serializer.text(sequencesList.get(i).itemsList.get(j).data);
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
	

	
	
	
	
	
	
	
	
	
	
	
	public void startRecordSequences()
	{
		this.realDate = new Timestamp(System.currentTimeMillis());
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
				serializer.attribute("", "finish","" + sequence.getfinishOffset());
				serializer.attribute("", "attendee", sequence.getWhoSpeacking().getName());
				serializer.attribute("", "question", sequence.getQuestion().toString());
				if (sequence.getComment().length()>0)
				{
					serializer.attribute("", "Comment", sequence.getComment());
				}
		    serializer.endTag("", "Sequence");
		}
		catch (Exception e) 
		    {
		        throw new RuntimeException(e);
		    } 
}
	public void stopRecordSequences()
	{
		try 
		{
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
	public void finishrecordingStep() 
	{
		this.setState(1);
		alterState();
	}
	public void finishTagingStep() 
	{
		reportGeneration();
		this.setState(2);
		alterState();
		
	}	
	
	

	


	
	 	
	public void reportGeneration()
	{
		try {

			FirstPdf f = new FirstPdf(this);
			ClassHtml c = new ClassHtml(this);
            } 
		catch (TransformerFactoryConfigurationError e) 
		{
            e.printStackTrace();
        }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void simpleTransform(String sourcePath, String xsltPath,
	        String resultDir) {
	        TransformerFactory tFactory = TransformerFactory.newInstance();
	        try {
	            Transformer transformer =
	                    tFactory.newTransformer(new StreamSource(new File(xsltPath)));

	            transformer.transform(new StreamSource(new File(sourcePath)),
	    new StreamResult(new File(resultDir)));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getDirectoryName() {
		return directoryName;
	}
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public Timestamp getPlannifiedDate() {
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
	public ArrayList<Attendee> getAttendeesList() {
		return attendeesList;
	}
	public void setAttendeesList(ArrayList<Attendee> attendeesList) {
		this.attendeesList = attendeesList;
	}
	public ArrayList<Question> getQuestionsList() {
		return questionsList;
	}
	public void setQuestionsList(ArrayList<Question> questionsList) {
		this.questionsList = questionsList;
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
	public static ArrayList getMeetingsListDirectory()
	{
		ArrayList <String>meetingsId = new ArrayList<String>();
		File meeting_folder = new File(Tools.meetings_directory);
		if(meeting_folder.canRead())
		{
			for (File inFile : meeting_folder.listFiles()) 
			{
			    if (inFile.isDirectory()) {
			    	meetingsId.add(inFile.getName());
			    }
			}
			return meetingsId;
		}
		return null;
	}
	public static ArrayList getMeetingsListName(ArrayList list)
	{
		ArrayList <String>meetingsName = new ArrayList<String>();
		for(int i=0;i<list.size();i++)
		{
			Meeting m = new Meeting(list.get(i).toString());
			meetingsName.add(m.getTitle());
		}
		return meetingsName;
	}
	public static boolean removeMeeting(String name)
	{
		String dir = Tools.meetings_directory + "/" + name;
		File path = new File(dir);
		if (path.exists())
			{
			DeleteRecursive(path);
			return true;
			}
		else return false;
	}

	static void DeleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            DeleteRecursive(child);

	    fileOrDirectory.delete();
	}
	
	
	
}
