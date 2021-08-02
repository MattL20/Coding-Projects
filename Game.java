package cs210;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;


public class Game {

	public static void main(String[] args) {
		//Set up the JFrame window
		JFrame frame = new JFrame();
		//Initialize the Gameplay object
		Gameplay Game = new Gameplay();
		//Set up the size, title and conditions of the JFrame window
		frame.setBounds(400,100,600,600);
		frame.setTitle("Game");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Add in the gameplay object as a source for the JFrame window
		frame.add(Game);
		frame.setVisible(true);

	}

}

 class Gameplay extends JPanel implements KeyListener, ActionListener {
	//Play variable set up to determine when the game has started
	private boolean play = false;
	//Score variable to keep track of your score
	private int score = 0; 
	//Timer and delay control the speed of the game
	private Timer timer;
	private int delay = 250;
	//Set up the snake object
	private Snake snake = new Snake();
	//Width and height variables set up to help with locating food
	private int w = 580/20;
	private int h = 580/20;
	//Set up the food variable with a random location
	private Vector food = foodlocation(w,h);

	public Gameplay() {
		//Add in the KeyListener, set its conditions and start the timer
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay,this);
		timer.start();
	}
	public void paint(Graphics g) {
		// Draw the Background
		g.setColor(Color.black);
		g.fillRect(0, 0, 600, 600);
		
		//Draw each segment of the Snake no matter the size
		Link current = snake.tail.first;
		while(current!=null) {
			g.setColor(Color.green);
			g.fillRect(current.data.x, current.data.y, 20, 20);
			current = current.next;
		}
		
		//Checks if the snake has eaten the food and if it has makes it grow 
		//and sets up a new food Location
		if(eat(food.x,food.y)) {
			food = foodlocation(w,h);
			Link current2 = snake.tail.first;
			while(current2!=null) {
				if(food.x ==current2.data.x && food.y == current2.data.y) {
					food = foodlocation(w,h);
				}
				current2 = current2.next;
			}
		}
		//Draws the food
		g.setColor(Color.red);
		g.fillRect(food.x, food.y, 20, 20);
		
		//Checks if the game is over using the endgame method
		//and draw the game over screen if it is over
		if(snake.endGame()) {
			play = false;
			g.setColor(Color.red);
			g.fillRect(0, 0, 600, 600);
			
			g.setColor(Color.black);
			g.setFont(new Font("serif", Font.BOLD, 50));
			g.drawString("GAME OVER", 150, 250);
			
			g.setColor(Color.black);
			g.setFont(new Font("serif", Font.BOLD, 25));
			g.drawString("SCORE:" + this.score, 250, 350);
			
			g.setColor(Color.black);
			g.setFont(new Font("serif", Font.BOLD, 15));
			g.drawString("PRESS 'ENTER' TO RESTART", 200, 400);
			
		}
		
		g.dispose();
		
	}
	//This method returns a random vector within the dimensions of the window,
	// and as a multiple of 20 to keep everything on the same grid
	public Vector foodlocation(int w,int h) {
		int tempX = (int)(Math.floor(Math.random()*w));
		tempX = tempX*20;
		int tempY = (int)(Math.floor(Math.random()*h));
		tempY = tempY*20;
		Vector temp = new Vector(tempX,tempY);
		return temp;
	}
	//Checks if the snake's head is in the exact same place as the food and returns true if it is,
	//false otherwise
	public boolean eat(int x, int y) {
		if(snake.tail.first.data.x==x&&snake.tail.first.data.y==y) {
			grow();
			this.score += 100;
			return true;
		}else {
		return false;
		}
	}
	//Adds in a new link to the linked list, meaning the snake grows in length
	public void grow() {
		Vector temp = new Vector(snake.tail.first.data.x-snake.getXspeed(),snake.tail.first.data.y-snake.getYspeed());
		snake.tail.insertLast(temp);
	}
	//this method is called every time there is a new frame, each frame is determined by the timer
	@Override
	public void actionPerformed(ActionEvent e) {
		if(play) {
			snake.Update();
			this.score+=1;
			if(snake.getX()==600) {
				snake.setX(0);
			}
			if(snake.getX()+20==0) {
				snake.setX(600);
			}
			if(snake.getY()==600) {
				snake.setY(0);
			}
			if(snake.getY()+20==0) {
				snake.setY(600);
			}
		}
		repaint();
	}
	//This method controls what happens when a key is pressed
	@Override
	public void keyPressed(KeyEvent e) {
		//if the game is still going
		if(!snake.endGame()) {
			//Press space to start the game
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				play = true;
			}
			//press the right arrow to move right
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
		
			if(snake.getXspeed()!=-20) {
				snake.moveRight();
			}
		}
		//press the left arrow to move left
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
		
			if(snake.getXspeed()!=20) {
			snake.moveLeft();
			}
		}
		//press the up arrow to move up
		if(e.getKeyCode() == KeyEvent.VK_UP) {
		
			if(snake.getYspeed()!=20) {
			snake.moveUp();	
			}
		}
		//press the down arrow to move down
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			
			if(snake.getYspeed()!=-20) {
			snake.moveDown();
			}
		}
		}
		//if the game is over press enter to restart
		if(snake.endGame()) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.score = 0;
				snake = new Snake();
			}
			
		}
		
		
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
}
 //This snake class controls most of the snakes functions
 class Snake {
	 //Set up attributes for each snake object
	 //posX and posY are used to store the coordinates of the head of the snake later on
		private int posX;
		private int posY;
		//Xspeed and Yspeed control the speed and direction of movement
		private int Xspeed;
		private int Yspeed;
		//Set up the Linked list which asks as the snakes body
		public LinkedList tail = new LinkedList();
		//constructor function to set up the basic variables needed for the snake at the beginning
		public Snake() {
			Vector temp = new Vector(300,300);
			this.tail.insertHead(temp);
			this.Xspeed = 20;
			this.Yspeed = 0;
		}
		//This update method is called for each frame and it is what allows the snake to move
		//and for each segment to have memory of where the head has been
		public void Update() {
			//Make a vector copy of the head of the snake
			Vector temp = new Vector (this.tail.first.data.x,this.tail.first.data.y);
			//Delete the link that holds the space of the end of the snake
			this.tail.deleteLast();
			//Update the position of the copy
			temp.x += this.Xspeed;
			temp.y += this.Yspeed;
			//Insert the copy at the head of the snake effectively moving the entire snake forward one unit
			this.tail.insertHead(temp);
			//Storing the current position of the head of the snake
			this.posX = this.tail.first.data.x;
			this.posY = this.tail.first.data.y;
		}
		//This endgame method checks if the head of the snake has crashed into any other part of its body
		public boolean endGame() {
			//create a copy of the head of the snake
			Vector temp = new Vector (this.tail.first.data.x,this.tail.first.data.y);
			Link current = this.tail.first.next;
			//loop through the whole snake to see if the head is in the same position as any part of its body
			//return true if it is and false otherwise
			while(current!=null) {
				if(temp.x ==current.data.x && temp.y ==current.data.y) {
					return true;
				}
				current = current.next;
			}
			return false;
		}
		//Getter and setter methods for the posX, posY, Xspeed and Yspeed
		public int getX() {
			return this.posX;
		}
		public int getY() {
			return this.posY;
		}
		//The next few methods are just called and used to set the Xspeed and the Yspeed
		//to the appropriate amount to move the snake in the desired direction
		//moveRight() sets the speeds to move the snake right
		public void moveRight() {
			this.Xspeed = 20;
			this.Yspeed = 0;
		}
		//moveLeft() sets the speeds to move the snake left
		public void moveLeft() {
			this.Xspeed = -20;
			this.Yspeed = 0;
		}
		//moveUp() sets the speeds to move the snake up
		public void moveUp() {
			this.Xspeed = 0;
			this.Yspeed = -20;
		}
		//moveDown() sets the speeds to move the snake down
		public void moveDown() {
			this.Xspeed = 0;
			this.Yspeed = 20;
		}
		public int getXspeed() {
			return this.Xspeed;
		}
		public int getYspeed() {
			return this.Yspeed;
		}
		public void setX(int x) {
			this.tail.first.data.x = x;
		}
		public void setY(int y) {
			this.tail.first.data.y = y;
		}


	}
 //This class sets up a double ended doubly linked list to act as the snakes body
 class LinkedList {
	 //Set up the first and Last Link variable to act as pointers
	 public Link first;
	 public Link last;
	 //Constructor method to initialize the list
	 public LinkedList() {
	 	first = null;
	 	last = null;
	 }
	 //Checks if the list is empty
	 public boolean isEmpty() {
	 	return (first==null);
	 }
	 //Inserts the provided variable at the start of the list
	 public void insertHead(Vector number) {
	 	Link newLink = new Link(number);
	 	newLink.next = first;
	 	newLink.previous = null;
	 	if(first != null) {
	 		first.previous = newLink;
	 	}else {
	 		last = newLink;
	 	}
	 	first = newLink;
	 }
	//Inserts the provided variable at the end of the list
	 public void insertLast(Vector number) {
	 	Link newLink = new Link(number);
	 	last = first;
	 	newLink.next = null;
	 	if(isEmpty()) {
	 		newLink.previous = null;
	 		first = newLink;
	 	}else {
	 		while(last.next!=null) {
	 			last = last.next;
	 		}
	 		last.next = newLink;
	 	newLink.previous = last;
	 	last = newLink;
	 	}
	 }
	 //Deletes the Last link and makes the link before it act as the new last link
	 public void deleteLast() {
	 	if(first!=last) {
	 	last = last.previous;
	 	this.last.next=null;
	 	}	
	 }
	 }
 //A Link class to set up what type of data each link will hold
 // and to store pointers to the next and previous links
 class Link {
	 public Vector data;
	 public Link next;
	 public Link previous;
	 public Link (Vector datain) {
	 	data = datain;
	 }

	 }
 // a vector class to act as coordinates for each part of the snake and the food
 class Vector {
		public int x;
		public int y;
		public Vector(int x,int y) {
			this.x = x;
			this.y = y;
		}

	}




