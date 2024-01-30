package edu.pacific.comp55.starter;

import acm.program.*;
import acm.graphics.*;
import java.util.Random;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Timer;


public class TopRoom extends GraphicsPane {
	private MainApplication program;
	public static final int PROGRAM_WIDTH = 800;
	public static final int PROGRAM_HEIGHT = 600;
	public static final String TOP_WALL = "TopWall.gif";
	public static final String LEFT_WALL = "LeftWall.gif";
	public static final String BOTTOM_WALL = "BottomWall.gif";
	public static final String RIGHT_WALL = "RightWall.gif";
	public static final String TOP_LEFT = "TopLeftCorner.png";
	public static final String TOP_RIGHT = "TopRightCorner.png";
	public static final String BOTTOM_LEFT = "BottomLeftCorner.png";
	public static final String BOTTOM_RIGHT = "BottomRightCorner.png";
	public static final String FLOORING = "Flooring.png";
	public static final String TOP_OPEN_DOOR = "TopOpenDoor.png";
	public static final String RIGHT_OPEN_DOOR = "RightOpenDoor.png";
	public static final String BOTTOM_OPEN_DOOR = "BottomOpenDoor.png";
	public static final String LEFT_OPEN_DOOR = "LeftOpenDoor.png";
	public static final String TOP_CLOSED_DOOR = "TopClosedDoor.gif";
	public static final String RIGHT_CLOSED_DOOR = "RightClosedDoor.gif";
	public static final String BOTTOM_CLOSED_DOOR = "BottomClosedDoor.gif";
	public static final String LEFT_CLOSED_DOOR = "LeftClosedDoor.gif";

	public static final String RIGHTROCK = "RightRock.png";
	public static final String LEFTROCK = "LeftRock.png";
	public static final String TOPROCK = "TopRock.png";
	public static final String BOTTOMROCK = "DownRock.png";
	
	private GImage[] splats =  new GImage[30];
	private int splatIndex = 0;

	
	
	private Overlay overlay;
    private int numRows;
    private int numCols;
    private Board topRoom = new Board(11, 11);
	public static final String LABEL_FONT = "Times-New-Roman-Bold-22";

    private PlayerCharacter mainCharacter  = new PlayerCharacter(5, 1, 5 *cellWidth(), 0, "Sebastian");;
    int keyCode;
    private BaseRoom base;
    public String prevDirection;
    private Timer gameTimer;
	private ArrayList<GImage> balls = new ArrayList<GImage>();
    private MonsterCharacter monsterCharacter;
    private ArrayList<MonsterCharacter> monsters = new ArrayList<MonsterCharacter>();
    private int dmgCooldown = 0;
    private int numTimes = 0;
    Random random = new Random();
    private int killCount = 0;
    private GLabel killCountNumber;
    
    
    
    
	private final Set<Integer> pressedKeys = new HashSet<>();
    Point offset = new Point();
	
	public Direction directionResolver(Point dirOffset) {
		System.out.println(dirOffset);
		if (dirOffset.x == 0 && dirOffset.y == -1) {
			return Direction.UP;
		}
		else if (dirOffset.x == 0 && dirOffset.y == 1) {
			return Direction.DOWN;
		}
		else if (dirOffset.x == 1 && dirOffset.y == 0) {
			return Direction.RIGHT;
		}
		else if (dirOffset.x == -1 && dirOffset.y == 0) {
			return Direction.LEFT;
		}
		else if (dirOffset.x == 1 && dirOffset.y == -1) {
			return Direction.UP_RIGHT;
		}
		else if (dirOffset.x == -1 && dirOffset.y == -1) {
			return Direction.UP_LEFT;
		}
		else if (dirOffset.x == 1 && dirOffset.y == 1) {
			return Direction.DOWN_RIGHT;
		}
		else if (dirOffset.x == -1 && dirOffset.y == 1) {
			return Direction.DOWN_LEFT;
		}
		else {
			return Direction.STILL;
		}
	}

    public TopRoom(MainApplication app) {
    	super();
    	program = app;
    	overlay = program.getOverlay();
    	base = program.getBase();
    	int delay = 16;
    	for (int i = 0; i < 30; i ++) {
        	splats[i] = new GImage("splat.png");
        }
    	
        
        gameTimer = new Timer(delay, new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code is executed on each timer tick
            	numTimes++;
            	program.remove(killCountNumber);
            	drawCount();
            	if(numTimes % 50 == 0 && killCount < 20) {
                    int randomNumber = random.nextInt(700 - 800/12 + 1) + 800/12;

            		monsterCharacter = new MonsterCharacter("Bug", 100, randomNumber, cellHeight());
            		monsters.add(monsterCharacter);
            		monsterCharacter.monsterIMG.setSize(cellWidth() , cellHeight());

            		program.add(monsterCharacter.monsterIMG, monsterCharacter.getDX(), monsterCharacter.getDY());
            		
            	}
            	if(killCount > 20) {
            		base.setCurrentPhase(6);
            		hideContents();
            		drawtopRoom();
            		overlay.showContents();
            		overlay.startTimer();
            		overlay.addTask(10);
            	}
        		moveAllBallsOnce();
        		checkBorder();
        		if(!monsters.isEmpty()) {
        		for(MonsterCharacter mon: monsters) {
            	mon.vertMove();
            	if(mon.getDY() > program.getHeight() - cellHeight()) {
            		overlay.damageTaken++;
        			overlay.removeHeartCount();
        			overlay.heartCount();
        			program.remove(mon.monsterIMG);
        			monsters.remove(mon);
        			break;
            	}
        			}
        		
            	boolean overlap = checkCollisions();
            	if (overlap && dmgCooldown == 0) {
            		// System.out.println("Monster hit player!");
            		System.out.println("Monster hit player!");
 
            		dmgCooldown = 50; // This is basically a tick to make sure the Monster can't hit the player multiple times in one collision
            						  // since the collision check is running so rapidly
            	}
            	else if (dmgCooldown > 0) {
            		dmgCooldown -= 1; // Decrement the tick
            		}
        		}
        		for (GImage ball: balls) {
        			for(MonsterCharacter mon: monsters) {
        				
        			
            		if (projectileMonsterCollision(ball, mon.monsterIMG)) {
            			program.playBugDieSound();
            			program.remove(mon.monsterIMG);
            			splats[splatIndex].setSize(50,50);
            			program.add(splats[splatIndex], mon.getDX(), mon.getDY());
            			splatIndex++;
            			// program.remove(ball);
            			monsters.remove(mon);
            			killCount++;
            			break;
            		}
            		}
            	}
            }
        });
    }

    
private boolean checkCollisions() {

	GRectangle boundsPlayer = mainCharacter.player.getBounds();
	GRectangle boundsMonster = monsterCharacter.monsterIMG.getBounds();

//        System.out.println("Player Bounds: x=" + boundsPlayer.getX() + ", y=" + boundsPlayer.getY() +
//        		", width=" + boundsPlayer.getWidth() + ", height=" + boundsPlayer.getHeight());
//
//        System.out.println("Monster Bounds: x=" + boundsMonster.getX() + ", y=" + boundsMonster.getY() +
//                ", width=" + boundsMonster.getWidth() + ", height=" + boundsMonster.getHeight());

	return boundsPlayer.intersects(boundsMonster);
}
private boolean projectileMonsterCollision(GImage projectile, GImage monster) {
	GRectangle projectileBounds = projectile.getBounds();
	GRectangle monsterBounds = monster.getBounds();

	return projectileBounds.intersects(monsterBounds);
}

private void addABall() {
	
	GImage ball = makeBall();

	program.add(ball,mainCharacter.getDX() + cellWidth() / 2, mainCharacter.getDY() + cellHeight() / 2);
	balls.add(ball);
}

public GImage makeBall() {
	GImage temp;
		temp = new GImage(TOPROCK);		
		temp.setSize(cellWidth() / 2,cellHeight() * 2);
	
	return temp;
}


private void moveAllBallsOnce() {
	for(GImage ball:balls) {
			ball.move(0,-10);
			}
		
		}

public void setPrevDirection(String d) {
	prevDirection = d;
}
/*
private void moveAllBallsOnce(Direction d) {
	for(GOval ball:balls) {
		switch(d) {
		case UP:
			ball.move(0,-5);
	 	case DOWN:
	 		ball.move(0, 5);
		
	 	case RIGHT:
	 		ball.move(5, 0);
	 	case LEFT:
	 		ball.move(-5, 0);
	 	case DOWN_RIGHT:
	 		ball.move(5, 5);
	 	case DOWN_LEFT:
	 		ball.move(5, -5);
	 	case UP_LEFT:
	 		ball.move(-5, 5);
	 	case UP_RIGHT:
	 	ball.move(-5, -5);
		}
		

	
	
		}
	}
	
*/

private void checkBorder() {
	for (GImage ball: balls) {
		if (ball.getX() <= cellWidth() || ball.getX() >= PROGRAM_WIDTH - cellWidth() || ball.getY() <= cellHeight() || ball.getY() >= PROGRAM_HEIGHT - cellHeight()) {
			System.out.println("detected");
			program.remove(ball);
			balls.remove(ball);
			break;
		}
	}
}

    	private void drawtopRoom() {
//        baseRoom = new Board(11, 11);
//        mainCharacter = new PlayerCharacter((baseRoom.getNumCols()-1) /2,(baseRoom.getNumRows()-1) /2, "Sebastian");
       // drawGridLines();
        drawRoom();
        drawMainCharacter();
        
        // Add other drawing methods here as needed
    }
    	
    private void drawMainCharacter() {
		 mainCharacter.player.setSize(cellWidth(), cellHeight());
		 mainCharacter.setDY(9* cellHeight());
		 program.add(mainCharacter.player, mainCharacter.getDX(), mainCharacter.getDY());
	}
    private void drawCount() {
		String numberStr = Integer.toString(killCount);
        killCountNumber = new GLabel("Kill Count: " + numberStr, cellWidth() - cellWidth() * .95 + cellWidth(), cellHeight() - cellHeight() * .05 + cellHeight());
        killCountNumber.setFont(LABEL_FONT);
		killCountNumber.setColor(new Color(255, 255, 255, 200));

        program.add(killCountNumber);
    }
    private void drawRoom() {
    	if(base.getCurrentPhase() == 5) {
    	 for (int row = 0; row < topRoom.getNumRows(); row++) {
 	        for (int col = 0; col < topRoom.getNumCols(); col++) {
 	        	if (row == 0 && col % 3 == 1) {
					GImage topWall = new GImage(TOP_WALL);
					topWall.setSize(cellWidth() * 3 , cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(topWall, x, y);
				}
				if (row == topRoom.getNumRows() - 1 && col % 3 == 1) {
					GImage bottomWall = new GImage(BOTTOM_WALL);
					bottomWall.setSize(cellWidth() * 3, cellHeight());
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(bottomWall, x, y);
				}
			}
		}
		for (int col = 0; col < topRoom.getNumCols(); col++) {
			for (int row = 0; row < topRoom.getNumRows(); row++) {
				if (col == 0 && row % 3 == 1) {
					GImage leftWall = new GImage(LEFT_WALL);
					leftWall.setSize(cellWidth(), cellHeight() * 3);
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(leftWall, x, y);
				}
				if (col == topRoom.getNumCols() - 1 && row % 3 == 1) {
					GImage rightWall = new GImage(RIGHT_WALL);
					rightWall.setSize(cellWidth(), cellHeight() * 3);
					double x = col * cellWidth();
					double y = row * cellHeight();
					program.add(rightWall, x, y);
				}
	        }
    	 }
	        for (int row = 0; row < topRoom.getNumRows(); row++) {
		       	 for (int col = 0; col < topRoom.getNumCols(); col++) {
		       		if(row == 0 && col == 0) {
		       			GImage topLeft = new GImage(TOP_LEFT);
	 	        		topLeft.setSize(cellWidth(), cellHeight());
	 	        		double x = col * cellWidth();
	 	               double y = row * cellHeight();
	 	        		program.add(topLeft, x, y);
		       		}
		       		if(row == 0 && col == topRoom.getNumCols()-1) {
		       			GImage topRight = new GImage(TOP_RIGHT);
	 	        		topRight.setSize(cellWidth(), cellHeight());
	 	        		double x = col * cellWidth();
	 	               double y = row * cellHeight();
	 	        		program.add(topRight, x, y);
		       		}
		       		if(row == topRoom.getNumRows()-1 && col == 0) {
		       			GImage bottomLeft = new GImage(BOTTOM_LEFT);
		       			bottomLeft.setSize(cellWidth(), cellHeight());
	 	        		double x = col * cellWidth();
	 	               double y = row * cellHeight();
	 	        		program.add(bottomLeft, x, y);
		       		}
		       		if(row == topRoom.getNumRows() - 1 && col == topRoom.getNumCols()-1) {
		       			GImage bottomRight = new GImage(BOTTOM_RIGHT);
		       			bottomRight.setSize(cellWidth(), cellHeight());
	 	        		double x = col * cellWidth();
	 	               double y = row * cellHeight();
	 	        		program.add(bottomRight, x, y);
		       		}
		       		if (col == (topRoom.getNumCols()-1) / 2 && row == topRoom.getNumRows()-1) {
		       			GImage topDoor = new GImage(BOTTOM_CLOSED_DOOR);
		       			topDoor.setSize(cellWidth(), cellHeight());
	 	        		double x = col * cellWidth();
	 	               double y = row * cellHeight();
	 	        		program.add(topDoor, x, y);
	        }
		       	 }
	        }

	        for (int row = 1; row < topRoom.getNumRows()-1; row++) {
		       	 for (int col = 1; col < topRoom.getNumCols() -1; col++) {
		       			GImage floor = new GImage(FLOORING);
	 	        		floor.setSize(cellWidth(), cellHeight());
	 	        		double x = col * cellWidth();
	 	               double y = row * cellHeight();
	 	        		program.add(floor, x, y);
		       	 }
	        }
    	}
    	if(base.getCurrentPhase() == 6) {
       	 for (int row = 0; row < topRoom.getNumRows(); row++) {
    	        for (int col = 0; col < topRoom.getNumCols(); col++) {
    	        	if (row == 0 && col % 3 == 1) {
    					GImage topWall = new GImage(TOP_WALL);
    					topWall.setSize(cellWidth() * 3 , cellHeight());
    					double x = col * cellWidth();
    					double y = row * cellHeight();
    					program.add(topWall, x, y);
    				}
    				if (row == topRoom.getNumRows() - 1 && col % 3 == 1) {
    					GImage bottomWall = new GImage(BOTTOM_WALL);
    					bottomWall.setSize(cellWidth() * 3, cellHeight());
    					double x = col * cellWidth();
    					double y = row * cellHeight();
    					program.add(bottomWall, x, y);
    				}
    			}
    		}
    		for (int col = 0; col < topRoom.getNumCols(); col++) {
    			for (int row = 0; row < topRoom.getNumRows(); row++) {
    				if (col == 0 && row % 3 == 1) {
    					GImage leftWall = new GImage(LEFT_WALL);
    					leftWall.setSize(cellWidth(), cellHeight() * 3);
    					double x = col * cellWidth();
    					double y = row * cellHeight();
    					program.add(leftWall, x, y);
    				}
    				if (col == topRoom.getNumCols() - 1 && row % 3 == 1) {
    					GImage rightWall = new GImage(RIGHT_WALL);
    					rightWall.setSize(cellWidth(), cellHeight() * 3);
    					double x = col * cellWidth();
    					double y = row * cellHeight();
    					program.add(rightWall, x, y);
    				}
   	        }
       	 }
   	        for (int row = 0; row < topRoom.getNumRows(); row++) {
   		       	 for (int col = 0; col < topRoom.getNumCols(); col++) {
   		       		if(row == 0 && col == 0) {
   		       			GImage topLeft = new GImage(TOP_LEFT);
   	 	        		topLeft.setSize(cellWidth(), cellHeight());
   	 	        		double x = col * cellWidth();
   	 	               double y = row * cellHeight();
   	 	        		program.add(topLeft, x, y);
   		       		}
   		       		if(row == 0 && col == topRoom.getNumCols()-1) {
   		       			GImage topRight = new GImage(TOP_RIGHT);
   	 	        		topRight.setSize(cellWidth(), cellHeight());
   	 	        		double x = col * cellWidth();
   	 	               double y = row * cellHeight();
   	 	        		program.add(topRight, x, y);
   		       		}
   		       		if(row == topRoom.getNumRows()-1 && col == 0) {
   		       			GImage bottomLeft = new GImage(BOTTOM_LEFT);
   		       			bottomLeft.setSize(cellWidth(), cellHeight());
   	 	        		double x = col * cellWidth();
   	 	               double y = row * cellHeight();
   	 	        		program.add(bottomLeft, x, y);
   		       		}
   		       		if(row == topRoom.getNumRows() - 1 && col == topRoom.getNumCols()-1) {
   		       			GImage bottomRight = new GImage(BOTTOM_RIGHT);
   		       			bottomRight.setSize(cellWidth(), cellHeight());
   	 	        		double x = col * cellWidth();
   	 	               double y = row * cellHeight();
   	 	        		program.add(bottomRight, x, y);
   		       		}
   		       	if (col == (topRoom.getNumCols()-1) / 2 && row == topRoom.getNumRows() - 1) {
	       			GImage topDoor = new GImage(BOTTOM_OPEN_DOOR);
	       			topDoor.setSize(cellWidth(), cellHeight());
 	        		double x = col * cellWidth();
 	               double y = row * cellHeight();
 	        		program.add(topDoor, x, y);
   		       	}
   		       	 }
   	        }

   	        for (int row = 1; row < topRoom.getNumRows()-1; row++) {
   		       	 for (int col = 1; col < topRoom.getNumCols() -1; col++) {
   		       			GImage floor = new GImage(FLOORING);
   	 	        		floor.setSize(cellWidth(), cellHeight());
   	 	        		double x = col * cellWidth();
   	 	               double y = row * cellHeight();
   	 	        		program.add(floor, x, y);
   		       	 }
   	        }
       	}   
    }

	@Override
	public synchronized void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
 
        
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        	overlay.stopTimer();
        	program.switchToPause();
        }
        
        else if (e.getKeyCode() == KeyEvent.VK_SPACE){
        	for(GImage b:balls) {
    			if(b.getX() < 1000 ) {
    				return;
    			}
    		}
        	program.playShootingSound();
    		addABall();
      
        }
        if (!pressedKeys.isEmpty()) {
            for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
                switch (it.next()) {
               /*     case KeyEvent.VK_UP:
    					if (balls.isEmpty()) {
                    	prevDirection = "Up";
    					}
        
                        offset.y = -1;
                        break; */
                    case KeyEvent.VK_LEFT:
    					if (balls.isEmpty()) {
                    	prevDirection = "Left";
    					}
                    	offset.x = -1;
                        break;
                    case KeyEvent.VK_DOWN:
                    	if ( (mainCharacter.getDX() > cellWidth() * 4.5
    							&& mainCharacter.getDX() < cellWidth() * 6.5)) {
    						if (base.getCurrentPhase() == 6) {

    								pressedKeys.clear();
    								offset = new Point(0, 0);
    								program.playOpenSound();
    								program.switchToBaseRoom();
    								break;
    							}
    						} else {
    							System.out.println("Locked");
    							program.playLockedSound();

    						
    					}
              //          offset.y = 1;
                       break;
                    case KeyEvent.VK_RIGHT:
    					if (balls.isEmpty()) {
    					prevDirection = "Right";
    					}
                        offset.x = 1;
                        break;
                }
            }
          mainCharacter.move(directionResolver(offset),11, 11);
        }
	}
		@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		System.out.println("keyReleased called");
            if (code == KeyEvent.VK_UP) {
                    offset.y = 0;
                    System.out.println("Up offset reset.");
            }
            else if (code == KeyEvent.VK_LEFT) {
                    offset.x = 0;
                    System.out.println("Left offset reset");
            }
            else if (code == KeyEvent.VK_DOWN) {
                    offset.y = 0;
                    System.out.println("Down offset reset.");
            }
            else if (code == KeyEvent.VK_RIGHT) {
                    offset.x = 0;
                    System.out.println("Right offset reset.");
            }
		pressedKeys.remove(e.getKeyCode());
	}
	

    private double cellWidth() {
        double cellWidth = (double) PROGRAM_WIDTH / topRoom.getNumRows();
        return cellWidth;
    }

    private double cellHeight() {
        double cellHeight = (double) PROGRAM_HEIGHT / topRoom.getNumCols();
        return cellHeight;
    }
	@Override
	public void showContents() {
		
		drawtopRoom();
		overlay.showContents();
		overlay.startTimer();
		overlay.addTask(5);
		gameTimer.start();
		drawCount();

	}
	@Override
	public void hideContents() {

		program.removeAll();
		gameTimer.stop();
	}

}
