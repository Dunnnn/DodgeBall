import java.applet.AudioClip;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;


public class GamePanel extends JPanel
{
	enum state {Start,Exit}
	enum state2 {Restart,Exit}

	final int  WIDTH = 600, HEIGHT = 800;
	final CardLayout cards = new CardLayout();
	
	private StartPanel PanelA;
	private GameBodyPanel PanelB;
    private ResultPanel PanelC;
    
    private AudioClip ingamemusic,changesound,startsound,endsound,reboundsound;
    
    Integer score = 0;
	
	
	public GamePanel()
	{	
		PanelA = new StartPanel();
		PanelB = new GameBodyPanel();
		PanelC = new ResultPanel();
	
		setLayout(cards);
		add(PanelA,"StartPanel");
		add(PanelB,"Gamebody");
		add(PanelC,"ResultPanel");
		
		cards.show(this, "StartPanel");
     	PanelA.setFocusable(true);
     	
     	musicsystem();
     	
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
	}//End of Constructor of the GamePanel
	

//Paint components of the start Panel
	private class StartPanel extends JPanel
	{
		private JPanel BackgroundPanel,BackgroundPanel2,BackgroundPanel3;
		private JLabel startimg,start, exit;
		
		private state current;
		
		
		public StartPanel()
		{			
			
			addKeyListener(new GameListener());

			current = state.Start;
			
			BackgroundPanel = new JPanel();
			BackgroundPanel.setPreferredSize(new Dimension(600,600));
			BackgroundPanel.setBackground(Color.white);
	
			
			BackgroundPanel2 = new JPanel();
			BackgroundPanel2.setPreferredSize(new Dimension(200,100));
			BackgroundPanel2.setBackground(Color.white);
			
			BackgroundPanel3 = new JPanel();
			BackgroundPanel3.setPreferredSize(new Dimension(200,100));
			BackgroundPanel3.setBackground(Color.white);
			
			ImageIcon icon = new ImageIcon("StartImage.jpg");
			startimg = new JLabel(icon,SwingConstants.CENTER);
			BackgroundPanel.add(startimg);
			BackgroundPanel.setLayout(null);
			startimg.setBounds(100,100,400,400);
			
			start = new JLabel("Start");
			start.setFont(new Font("Courier",Font.BOLD,32));
			BackgroundPanel2.add(start);
	
			exit = new JLabel ("Exit");
			exit.setFont(new Font("Courier",Font.BOLD,32));
			BackgroundPanel3.add(exit);
	
			
			add(BackgroundPanel);
			add(BackgroundPanel2);
			add(BackgroundPanel3);
			
			setBackground(Color.white);
			setPreferredSize(new Dimension(WIDTH,HEIGHT));

		}//End of the Constructor of StartPanel
		
	//Paint the choose line
		public void paint(Graphics page)
		{
			super.paint(page);
			if (current == state.Start)
			{
				start.setForeground(Color.red);
				exit.setForeground(Color.black);
			}
							
			else
			{
				start.setForeground(Color.black);
				exit.setForeground(Color.red);
			}
		}//End of paint method of startpanel
			
		
	//Implementation of the keylistener of the startpanel
		private class GameListener implements KeyListener
		{
			public void keyTyped (KeyEvent event) {}
	        public void keyReleased (KeyEvent event) {}
			public void keyPressed(KeyEvent event) {
					switch (event.getKeyCode())
					{
					case KeyEvent.VK_RIGHT:
						changesound.play();
						if(current == state.Start)
							current = state.Exit;
						else
							current = state.Start;
						break;
					case KeyEvent.VK_LEFT:
						changesound.play();
						if(current == state.Exit)
							current = state.Start;
						else
							current = state.Exit;
						break;
					case KeyEvent.VK_ENTER:
						if(current == state.Start)
							ChangePanel1();
						else
							System.exit(0);
						break;
					}//End of Switch
					repaint();
			}//End of KeyPressed Method of GameListener
		}//End of implementation of GameListener

	}//End of StartPanel Class
	
	private class GameBodyPanel extends JPanel
	{
		//ScorePanel Part

		private JPanel ScorePanel = new JPanel();
		private DecimalFormat fmt = new DecimalFormat("00000000");
		private JLabel ScoreLabel = new JLabel("Score:"+fmt.format(score));
		
		private JPanel BodyPanel = new JPanel();
		
		//Set the HighestTimer
		private Timer  BodyTimer;
		final int DELAY = 50;
		private Random generator = new Random();
		
		
		private ImageIcon icon = new ImageIcon("NormalBall.png");
		private ImageIcon playericon = new ImageIcon("Player.png");
		
		ControlListener controller = new ControlListener();
		        
        private ArrayList<Ball> Balls = new ArrayList<Ball>(300);
        private int Ballnum = 1;
        
        private GamePlayer player;
        private boolean callforaccN = false;
        int[] level ={30000/DELAY,60000/DELAY,100000/DELAY,160000/DELAY,280000/DELAY,460000/DELAY,660000/DELAY,900000/DELAY,1080000/DELAY};

		
		public GameBodyPanel()
		{
		setLayout(null);
		
		addKeyListener(controller);
		
		BodyTimer = new Timer(DELAY,new BodyPerformer());

		//Initiate the ScorePanel
		ScoreLabel.setFont(new Font("Courier",1,18));
		ScoreLabel.setForeground(Color.white);
		ScoreLabel.setBounds(10,10,200,20);
		ScorePanel.add(ScoreLabel);
		
		ScorePanel.setBackground(Color.RED);
		ScorePanel.setLayout(null);
		ScorePanel.setPreferredSize(new Dimension(100,100));
		ScorePanel.setBounds(0,0,600,40);
		
		BodyPanel.setBounds(0,41,600,760);
		BodyPanel.setPreferredSize(new Dimension(600,760));
		BodyPanel.setLayout(null);
		BodyPanel.setBackground(Color.white);
		
        this.add(ScorePanel);
        this.add(BodyPanel);
        
   
		
		setBackground(Color.white);
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
				
		
		}//End of Constructor of the GameBodyPanel.
		
		
		public void GameStart()
		{
			score = 0;
			
			Ballnum = 1;
			Balls.clear();
			
			startsound.play();
			ingamemusic.loop();
			
			BodyTimer.start();
			
			player = new GamePlayer();
			BodyPanel.add(player);

			Balls.add(new Ball());
			Balls.add(new Ball());
			
			BodyPanel.add(Balls.get(0));
			BodyPanel.add(Balls.get(1));
			
			controller.reset();
			
			
			
		}
		
		public void End()
		{
			ingamemusic.stop();
			

			
			BodyPanel.removeAll();
			
	
			BodyTimer.stop();
		}
		
		public void paintComponent (Graphics page)
		   {
		      super.paintComponent (page);
		   }//End of PaintComponent method
		
		public class Ball extends JLabel
		{
			final int diameter = 36;
			private int x,y,speedx,speedy;
			public boolean boundx,boundy;
			
			
			public Ball(){
				super(icon);
				boundx = false;
				boundy = false;
				
				x = (int)(generator.nextDouble()*400) + 100;
				y = (int)(generator.nextDouble()*400) + 100;
				
				speedx = generator.nextBoolean()?(generator.nextInt(3)+3):(-(generator.nextInt(3)+3));
				speedy = generator.nextBoolean()?(generator.nextInt(3)+3):(-(generator.nextInt(3)+3));


				setBounds(x-diameter/2,y-diameter/2,diameter,diameter);
			}
			
			public Ball(Ball orig){
				
				super(icon);				
				this.x = orig.x;
				this.y = orig.y;
				boundx = false;
				boundy = false;
				
				double multiplex, multipley;
				multiplex = generator.nextDouble()*0.5 +0.75;
				multipley = generator.nextDouble()*0.5 +0.75;
				
				do{
				multiplex = generator.nextDouble()*0.5 +0.75;
				multipley = generator.nextDouble()*0.5 +0.75;
				speedx = (int)(orig.speedx * multiplex);
				speedy = (int)(orig.speedy * multipley);
				}while((Math.abs(speedx - orig.speedx) < 1 )|| (Math.abs(speedy - orig.speedy) < 1));
				
				
				if(speedx < 2 && speedx > 0)
					speedx += 2;
				
				if(speedx > -2 && speedx < 0)
					speedx -= 2;
				
				if(speedy < 2 && speedy > 0)
					speedy += 2;
				
				if(speedy > -2 && speedy < 0)
					speedy -= 2;
				
				
				

				
				setBounds(x-diameter/2,y-diameter/2,diameter,diameter);
				
								
			}
			
			public void testboundx(){
				if (x<= diameter/2)
				{
					x = diameter/2;
					boundx = true;
				}
				else 
					if(x>=(600-diameter/2))
					{
						x = 600-diameter/2;
						boundx = true;
					}
			}
			
			public void testboundy(){
				if (y<=diameter/2)
				{
					y = diameter/2;
					boundy = true;
				}
				else 
					if(y>=(760-diameter/2))
					{
						y = 760-diameter/2;
						boundy = true;
					}
			}
			
			public void move()
			{
				
			    x = x + speedx;
			    y = y + speedy;
			
				setBounds(x-diameter/2,y-diameter/2,diameter,diameter);
			}
			
			public void Accelerate()
			{
				for(int i=0;i<level.length;i++)
				{
					if(score == level[i])
						callforaccN = true;
				}
				
				if(callforaccN)
				{
					speedx = (int) Math.ceil(speedx * 1.25);
					speedy = (int) Math.ceil(speedy * 1.25);
					callforaccN = false;
				}
			}

			public void boundfromx()
			{
				speedx = -speedx;
			}
			
			public void boundfromy()
			{
				speedy = -speedy;
			}
			
			
			public void boundoff()
			{
				boundx = false;
				boundy = false;
			}
			
			public int getx()
			{
				return x;
			}
			
			public int gety()
			{
				return y;
			}
			
			
		}
		
		public class GamePlayer extends JLabel
		{
			final int diameter = 10;
			private int x,y,movespeed,speedx,speedy;
			
			public GamePlayer()
			{
				super(playericon);
				x = 300;
				y = 630;
				setBounds(x-diameter/2,y-diameter/2,diameter,diameter);
				movespeed = 8;
				speedx = 0;
				speedy = 0;
			}
			
			public void move()
			{
				
				if(x > diameter/2 && x<600-diameter/2)
					x += speedx;
				else
				{	
					if ((x <= diameter && speedx < 0))
					{
						speedx = 0;
						x = diameter;
					}
					else
					{
						if(x >= 600 - diameter && speedx > 0)
						{
							speedx = 0;
							x = 600 - diameter;
						}
						else
						x +=speedx;
					}
					
					if(Math.abs(speedx) == 3)
						speedx = 0;
					

					
				}
				
				if(y > diameter && y<760-diameter/2)
					y += speedy;
				else
				{
					if ((y <= diameter && speedy < 0))
					{
						speedy = 0;
						y = diameter;
					}
					else
					{
						if(y >= 760 - diameter && speedy > 0)
						{
							speedy = 0;
							y = 760 - diameter;
						}
						else
						y +=speedy;
					}
					
					
				}
				
				switch (speedx)
				{

				case 1:
					speedx = 0;
					break;

				case -1:
					speedx = 0;
					break;					
				}
				
				switch (speedy)
				{
				case 1:
					speedy = 0;
					break;
				case -1:
					speedy = 0;
					break;						
				}
				
					
					
				

				
				setBounds(x-diameter/2,y-diameter/2,diameter,diameter);
			}
			
		
			
			
			public void accUp(){
				switch(speedy)
				{
					case 0:
					case 1:
					case 7:
					case 6:
					case 8:
					case 10:
					case 11:
					case 12:
						speedy = -7;
						break;
					case -7:
						speedy = -8;
						break;
					case -8:
						speedy = -10;
						break;
					case -10:
						speedy = -11;
						break;
					case -11:
						speedy = -12;
						break;


				}
			};
			public void accDown(){
				switch(speedy)
				{
					case 0:
					case -1:
					case -5:
					case -7:
					case -8:
					case -10:
					case -11:
					case -12:
						speedy = 7;
						break;
					case 7:
						speedy = 8;
					case 8:
						speedy = 10;
						break;
					case 10:
						speedy = 11;
						break;
					case 11:
						speedy = 12;
						break;

						


				}
			};
			public void accRight(){
				switch(speedx)
				{
					case 0:
					case -1:
					case -7:
					case -6:
					case -8:
					case -10:
					case -11:
					case -12:
						speedx = 7;
						break;
					case 7:
						speedx = 8;
					case 8:
						speedx = 10;
						break;
					case 10:
						speedx = 11;
						break;			
					case 11:
						speedx = 12;
						break;

				}
			};
			public void accLeft(){
				switch(speedx)
				{
					case 0:
					case 1:
					case 6:
					case 7:
					case 8:
					case 10:
					case 11:
					case 12:
						speedx = -7;
						break;
					case -7:
						speedx = -8;
					case -8:
						speedx = -10;
						break;
					case -10:
						speedx = -11;
						break;			
					case -12:
						speedx = -12;
						break;


				}
			};
			
			public void decUp(){
				speedy = -1;
			};
			public void decDown(){
				speedy = 1;
			};
			public void decRight(){
				speedx = 1;
			};
			public void decLeft(){
				speedx = -1;
			};
			
			
			
			public int getx()
			{
				return x;
			}
			
			public int gety()
			{
				return y;
			}
			
			public int getmovespeed()
			{
				return movespeed;
			}
			
		}
		
		
		public void StartSplit(Ball target)
		{
			int rd = generator.nextInt(10000);//ratio denominator
			
			if(Ballnum<20)
			{
				if(rd<7777)
				Split(target);
			}
			else
			{
				if(Ballnum<40)
				{
					if(rd<2000)
						Split(target);	
				}
				else
				{
					if(Ballnum<80)
					{
						if(rd<500)
						Split(target);
					}
					else
					{
						if(Ballnum < 160 )
							if(rd < 100)
							Split(target);
					}
				}
			}
		}//End of StartSplit Method of GameBodyPanel Class
		
		private void Split(Ball target)
		{
			if(Ballnum < 300)
			{
				Balls.add(new Ball(target));
				reboundsound.play();
				Ballnum++;
				BodyPanel.add(Balls.get(Ballnum));
			}
		}
		
	   private void TestLose(GamePlayer player, Ball ball)
	   {
		   int xdiff = ball.getx() - player.getx();
		   int ydiff = ball.gety() - player.gety();
		   
		   int xdiffsquare = (int) Math.pow(xdiff, 2);
		   int ydiffsquare = (int) Math.pow(ydiff, 2);
				   	
		   
		   int distance = (int) Math.sqrt(xdiffsquare+ydiffsquare);
		   
		   if ((distance*2) <= player.diameter + ball.diameter-14)
				   LoseGame();
		   
	   }
	   
	   private void LoseGame()
	   {
		   ChangePanel3();
	   }
	   
	   private void scoreincrement()
	   {
		   score ++;
	   }
		
		
		
		
		
		
		private class BodyPerformer implements ActionListener
		{
			
			public void actionPerformed(ActionEvent event) {

				player.move();

				
				
				for(int i = 0; i < Balls.size();i++)
				{
					if(Balls.get(i) != null)
					{
						
						Ball target = Balls.get(i);
						TestLose(player,target);
						
						target.Accelerate();
						target.move();
						
						
						target.testboundx();
						target.testboundy();
						
						if(target.boundx||target.boundy)
						{
							
								if (target.boundx)
								{
									target.boundfromx();
								}
								else
								{
									target.boundfromy();
								}
								target.boundoff();
								
								StartSplit(target);
																														
						}//End of the if for Split Processing 

					}//End of move Processing
				
				
				}//End of Check for each ball
				
				scoreincrement();
				ScoreLabel.setText("Score:"+fmt.format(score));
				
				ScorePanel.repaint();
				BodyPanel.repaint();
			}//End of ActionPerformed of BodyPerformer
		
		}//End of BodyPerformer Implementation of GameBodyPanel Class	
		
		private class ControlListener implements KeyListener
		{
			private boolean up = false,down = false,left = false,right = false;
			
			public void reset()
			{
				up = false;
				down = false;
				left = false;
				right = false;
			}
			
			public void keyPressed(KeyEvent event) 
			{

				switch(event.getKeyCode())
				{
					case KeyEvent.VK_ESCAPE:
						ChangePanel2();
						break;
					case KeyEvent.VK_UP:
						up = true;
						//player.accUp();
						break;
					case KeyEvent.VK_DOWN:
						//player.accDown();
						down = true;
						break;
					case KeyEvent.VK_LEFT:
						//player.accLeft();
						left = true;
						break;
					case KeyEvent.VK_RIGHT:
						//player.accRight();
						right = true;
						break;
				}
				MoveWay();
			}
			
			public void keyReleased(KeyEvent event)
			{
				switch(event.getKeyCode())
				{
					case KeyEvent.VK_UP:
						player.decUp();
						up = false;
						break;
					case KeyEvent.VK_DOWN:
						player.decDown();
						down = false;
						break;
					case KeyEvent.VK_LEFT:
						player.decLeft();
						left = false;
						break;
					case KeyEvent.VK_RIGHT:
						player.decRight();
						right = false;
						break;
				}
				MoveWay();
			}
			
			public void MoveWay()
			{
				if(up&&down)
				{
					player.decDown();
					player.decUp();
				}
				
				if(left&&right)
				{
					player.decLeft();
					player.decRight();
				}
				
				if(up&&!down&&!left&&!right)
					player.accUp();
				else 
				{
					if (!up&&down&&!left&&!right)
						player.accDown();
					else 
					{
						if (!up&&!down&&left&&!right)
							player.accLeft();
						else 
						{	
							if (!up&&!down&&!left&&right)
								player.accRight();
							else 
							{
								if(up&&!down&&left&&!right)					
								{
									player.accUp();
									player.accLeft();
								}
								else
								{
									if(up&&!down&&!left&&right)	
									{
										player.accUp();
										player.accRight();
									}
									else
									{
										if(!up&&down&&!left&&right)	
										{
											player.accDown();
											player.accRight();
										}
										else
										{	
											if(!up&&down&&left&&!right)	
											{
												player.accDown();
												player.accLeft();
											}
										}
									}
								}
							}
						}
					}
				}
				
				
			}

			public void keyTyped(KeyEvent e) {};
				
			
		}//End of ControlListener Implementation of GameBodyPanel Class
		
		
	}//End of GameBody Panel
	
	
	
	
	
	private class ResultPanel extends JPanel
	{
		JPanel ScorePanel, OptionPanel;
		JLabel ScoreLabel1, ScoreLabel2,RestartLabel,ExitLabel;
		private state2 current;
		
		
		public ResultPanel()
		{
			setLayout(null);
			current = state2.Restart;

			addKeyListener(new ResultListener());
			
			ScorePanel = new JPanel();
			ScorePanel.setLayout(null);
			ScorePanel.setBounds(0, 0, 600, 600);
			ScorePanel.setPreferredSize(new Dimension(WIDTH,HEIGHT - 200));
			ScorePanel.setBackground(Color.white);
			
			
			ScoreLabel1 = new JLabel("Your Score: ");
			ScoreLabel1.setFont(new Font("Courier",1,36));
			ScoreLabel1.setBounds(178, 80, 300, 50);
			ScorePanel.add(ScoreLabel1);
			
			ScoreLabel2 = new JLabel();
			ScoreLabel2.setFont(new Font("Courier",1,64));
			ScoreLabel2.setForeground(Color.ORANGE);
			ScoreLabel2.setBounds(180, 310, 300, 50);
			
			

			ScorePanel.add(ScoreLabel2);
			
			
			
			
			OptionPanel = new JPanel();
			OptionPanel.setBounds(0, 601, 600, 200);
			OptionPanel.setPreferredSize(new Dimension(200,200));
			OptionPanel.setBackground(Color.white);
			OptionPanel.setLayout(null);
			
			RestartLabel = new JLabel("Restart");
			RestartLabel.setFont(new Font("Courier",1,24));
			RestartLabel.setBounds(125,0,100,50);
			
			ExitLabel = new JLabel("Exit");
			ExitLabel.setFont(new Font("Courier",1,24));
			ExitLabel.setBounds(375,0,100,50);
			
			OptionPanel.add(RestartLabel);
			OptionPanel.add(ExitLabel);
			
			add(ScorePanel);
			add(OptionPanel);
			
			setBackground(Color.white);
			setPreferredSize(new Dimension(WIDTH,HEIGHT));
			
		}
		

		private void setScore()
		{
			DecimalFormat fmt = new DecimalFormat("000000");
			ScoreLabel2.setText(fmt.format(score));
		}
		
		public void paint(Graphics page)
		{
			super.paint(page);
			if (current == state2.Restart)
			{
				RestartLabel.setForeground(Color.red);
				ExitLabel.setForeground(Color.black);
			}
							
			else
			{
				RestartLabel.setForeground(Color.black);
				ExitLabel.setForeground(Color.red);
			}
		}
		
		private class ResultListener implements KeyListener
		{
			public void keyTyped (KeyEvent event) {}
	        public void keyReleased (KeyEvent event) {}
			public void keyPressed(KeyEvent event) {
					switch (event.getKeyCode())
					{
					case KeyEvent.VK_RIGHT:
						changesound.play();
						if(current == state2.Restart)
						{	
							current = state2.Exit;
						}
						else
						{
							current = state2.Restart;
						}
						break;
					
					case KeyEvent.VK_LEFT:
						changesound.play();
						if(current == state2.Exit)
						{
							current = state2.Restart;
						}
						else
						{		
							current = state2.Exit;
						}
						break;
					
					case KeyEvent.VK_ENTER:
						if(current == state2.Restart)
							ChangePanel1();
						else
							System.exit(0);
						break;
					}//End of Switch
					repaint();
			}//End of KeyPressed Method of ResultListener
		}//End of implementation of ResultListener
	}
	
	private void ChangePanel1()
	{
		endsound.stop();
		
		cards.show(this, "Gamebody");
		PanelB.GameStart();
		PanelB.requestFocus(true);
	}//End of ChanegPanel1 Method of GamePanel
	
	private void ChangePanel2()
	{
		endsound.stop();
		
		cards.show(this, "StartPanel");
		PanelB.End();
		PanelA.requestFocus(true);
	}//End of ChanegPanel2 Method of GamePanel
	
	private void ChangePanel3()
	{
		cards.show(this, "ResultPanel");
		PanelB.End();
		
		endsound.play();
		PanelC.setScore();
		PanelC.requestFocus(true);
	}
	
	private void musicsystem()
	{
		URL ingameurl,changeurl,starturl,endurl,reboundurl;
		ingameurl = changeurl = starturl =endurl = reboundurl = null;
		try{
     	ingameurl = new URL("file","localhost","Dueling Ninjas.wav");
     	changeurl = new URL("file","localhost","change.wav");
     	starturl = new URL("file","localhost","start.wav");
     	endurl = new URL("file","localhost","end.wav");
     	reboundurl = new URL("file","localhost","rebound.wav");
		}
		catch(Exception exception){}

     	
		
     	ingamemusic = JApplet.newAudioClip(ingameurl);
     	changesound = JApplet.newAudioClip(changeurl);
     	startsound =JApplet.newAudioClip(starturl);
     	endsound = JApplet.newAudioClip(endurl);
     	reboundsound = JApplet.newAudioClip(reboundurl);
	}
	
	
	

}//End of GamePanel Class