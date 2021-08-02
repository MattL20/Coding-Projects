import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;			//Import all the necessary classes

public class TSP {
//Set the window height and width variables
public static final int win_width = 800;		
public static final int win_Height = 540;
public static Window window;
	public static void main(String[] args) {
		//Create a new Window object passing in the height, width and the title for the window
		window = new Window(win_width,win_Height,"TSP"); 
	}

}
//create the window class
class Window extends JFrame {
//Window constructor method
	Window(int width, int height, String title){
		//Create a new JFrame object, this will be the window
		JFrame f= new JFrame(title);  
		f.setLayout(null);
		//Set the Size of the frame
		f.setSize(width, height);
		//Make the frame visible
		f.setVisible(true);
		//Set the code to close when the frame is closed
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//Makes the background cyan
		f.getContentPane().setBackground(Color.cyan);
		//Call the createTextArea method passing in the JFrame
		createTextArea(f);
	}
	
	
	 private void createTextArea(JFrame f) {
		 //Create a new JtextArea object
		 JTextArea ta = new JTextArea(200,50);
		 //Create a new JScrollPane Object Passing in the JTextArea
		 JScrollPane sp = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 //Set the bounds of the scrollable textArea
		 sp.setBounds(10,10,760,200);
		 //Create another Scrollable JTextArea
		 JTextArea ta2 = new JTextArea(200,50);
		 JScrollPane sp2 = new JScrollPane(ta2,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 sp2.setBounds(10,320,760,170);
		//Add both TextAreas to the JFrame
		 f.add(sp);
		 f.add(sp2);
		//Call the createButton method
		 createButton(f,ta,ta2);
		 //Call the createPanel method
		 createPanel(f);
		 //This adds text to the textArea to start with
		 ta.setText("Input Addresses here! Replace this Text"); 
		 
	 }
	 private void createPanel(JFrame f) {
		 	//Create a Panel object 
		 	//This panel is used to add more colour to the background of the window
			Panel p = new Panel();
			p.setLayout(null);
			//Set the bounds of the Panel
			p.setBounds(0,0,800,270);
			//Set the colour of the Panel
			p.setBackground(Color.red);
			//Add the panel to the JFrame
			f.add(p);
		
		}
	 private void createButton(JFrame f,JTextArea ta,JTextArea ta2) {
		 //Create 2 JButton objects
		 JButton b = new JButton("Get Route");
		 //Set the bounds of the first JButton object
		 b.setBounds(180,230,200,80);
		 JButton b2 = new JButton("Show Addresses");
		//Set the bounds of the second JButton object
		 b2.setBounds(400,230,200,80);
		 //Add an action listener to the first button to perform an action when clicked
		 b.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 //When the Button is clicked it takes the text that is in the first textArea
				String s = ta.getText();
				//Split the input using new lines
				String lines[] = s.split("\\r?\\n");
				//Compute the quickest route using the compute method and return it as a string
				 String ans = compute(lines);
				 //The compute method returns the route in numbers followed on a new line as the route in addresses
				 //So must be split up using the new line
				 String[] temp = ans.split("\\r?\\n");
				 //When the button is clicked the route in numbers is displayed in the second textArea
				ta2.setText(temp[0]);
			 }
		 });
		 //Add another actionlistener to the second button
		 b2.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 //This code is repeated to allow access to the answer to this button actionlistener
				String s = ta.getText();
				String lines[] = s.split("\\r?\\n");
				 String ans = compute(lines);
				 String[] temp = ans.split("\\r?\\n");
				 //The second part of the answer (the addresses) is split up using the commas between them
				String[] temp2 = temp[1].split(",",0);
				 String newAns = new String();
				 //The addresses are then added to a new string on individual lines
				 for(int i = 0;i<temp2.length;i++) {
					 newAns += temp2[i] + "\n";
				 }
				 //When the Second button is Clicked the addresses are displayed in the second textArea
					ta2.setText(newAns);
			 }
		 });
		 
		 //The Buttons are then added to the frame
		 f.add(b2);
		 f.add(b);
		
	 }
	 //The compute method, with the lines of the input passed into it
	 public static String compute(String [] lines) {
		 //A newLines Array is created and is one space bigger than the array of the lines inputted
		 String [] newLines = new String[lines.length + 1];
		 //The Address of Apache Maynooth is added into the first space of the newLines Array
		 //to act as a starting point
		 newLines[0] = "0, Apache pizza,0,53.38197,-6.59274";
		 //The rest of the addresses are added into the new array
		 for(int i = 0;i<lines.length;i++) {
			 newLines[i+1] = lines[i];
		 }
		 //An Array of coordinate objects is made to hold the Coordinates of each address
		 Coordinates [] coord = new Coordinates[newLines.length];
		 //An String array is made to hold the addresses
			String [] Addresses = new String [newLines.length];
			//This is a temporary array that is used to hold the contents of each lines split up
			String [] tempA = new String[5];
			for(int i =0;i<newLines.length;i++) {
				//Each Line is split up into its components
				tempA = newLines[i].split(",",0);
				//The coordinates of each address are added to a coordinate object in the coord array
				coord[i] = new Coordinates(Double.valueOf(tempA[3]),Double.valueOf(tempA[4]));
				//The Addresses are then added to the Addresses array
				Addresses[i] = tempA[1];			
			}
			//A 2D array is made to act as a distance matrix which holds the distance 
			//between each coordinates to each other coordinates
			double[][] distancematrix = new double [coord.length][coord.length];
			for(int i = 0;i<coord.length;i++) {
				for(int j = 0;j<coord.length;j++) {
					//The Distance matrix is filled using the GCD method (Great Circle Distance) 
						distancematrix[i][j]= GCD(coord[i].lat,coord[i].lon,coord[j].lat,coord[j].lon);
						//Each row acts as a list of distance from one location to all other locations
				}
			}
			int current = 0;
			int nearestplace = 0;
			//Two arraylists are made to hold the route of locations in numbers and in addresses
			ArrayList<Integer> a2 = new ArrayList<Integer> ();
			ArrayList<String> a3 = new ArrayList<String>();
			a2.add(0);
			a3.add(Addresses[0]);
			for(int i = 0;i<coord.length-1;i++) {
				double near = 300.0;
				//This for loop is used to find the closest neighbour that hasn't been visited yet
				for(int j = 0;j<coord.length;j++) {
					//The isIn method is used to check if the location has been visited already
					if(distancematrix[current][j]!=0.0 && near>distancematrix[current][j] && isIn(a2,j)) {
						near = distancematrix[current][j];	
						nearestplace = j;
					}
				}
				//The nearest neighbour is added to the arraylists and 
				//the process is repeated using the nearest neighbour as the starting point
				a2.add(nearestplace);
				a3.add(Addresses[nearestplace]);
				current = nearestplace;
			}
			
			String ans = new String();
			//These for loops are used to add the route to a single string
			//the numbers and addresses are separated by a new line
			for(int i =1;i<a2.size();i++) {
				if(i<a2.size()-1) {
				ans += a2.get(i) + ",";
				}else {
					ans += a2.get(i);
				}
			}
			ans += "\n";
			for(int i =1;i<a3.size();i++) {
				if(i<a3.size()-1) {
				ans += a3.get(i) + ",";
				}else {
					ans += a3.get(i);
				}
			}
			// the String is then returned
			return ans;
			
		}
	 //This method is used to find the distance between two pairs of coordinates
		public static double GCD(double lat1, double lon1, double lat2, double lon2) {
			//The Longitude and latitude are converted to radians
			double rlat1 = Math.toRadians(lat1);
			double rlon1 = Math.toRadians(lon1);
			double rlat2 = Math.toRadians(lat2);
			double rlon2 = Math.toRadians(lon2);
			//The Change in longitude and latitude is calculated
			double dlon = rlon1 - rlon2;
			double dlat = rlat1 - rlat2;
			//Using a formula the distance is calculate
			double num = Math.pow(Math.sin(dlat/2), 2)+ Math.cos(rlat1) * Math.cos(rlat2) * Math.pow(Math.sin(dlon/2), 2);
			double ans = 2*Math.asin(Math.sqrt(num));
			double r = 6371.0;
			double dis = ans*r;
			//The distance is then returned
			return dis;
		}
		//This method just checks if the location is already in the route to see if it has been visited already
		public static boolean isIn(ArrayList<Integer> a2, int j) {
			for(int i =0;i<a2.size();i++) {
				if(a2.get(i)==j) {
					return false;
				}
			}
			return true;
		}

}
//This coordinate class creates an object with two attributes that hold the longitude and latitude
class Coordinates {
	public double lon;
	public double lat;
	
	public Coordinates(double x,double y) {
		this.lon = x;
		this.lat = y;
	}

}
