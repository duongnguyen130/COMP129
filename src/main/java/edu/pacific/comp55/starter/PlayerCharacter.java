package edu.pacific.comp55.starter;

import java.awt.Point;
import java.io.File;
import java.util.HashMap;

import acm.graphics.GImage;


public class PlayerCharacter implements Character {
	private String charName = "";
	private int startRow;
	private int startCol;
	private double dx;
	private double dy;
	public  int playerSpeed = 4;
	private Character main;
	Location currLocation;
	public GImage player;
	public static final int PROGRAM_WIDTH = 800;
	public static final int PROGRAM_HEIGHT = 600;
	public static final String BOTTOM_OPEN_DOOR = "Bottom_Open_Door.png";
	public static final String IDLE_FORWARD = "images/Character/Idle forward.png";
	public static final String FACE_LEFT = "images/Character/Face Left.png";
	public static final String IDLE_BACKWARD = "images/Character/Idle backward.png";
	public static final String FACE_RIGHT = "images/Character/Face Right.png";
	public static final String FACE_UP_RIGHT = "images/Character/left foot backward.png";
	public static final String FACE_UP_LEFT = "images/Character/Right foot backward.png";
	public static final String FACE_DOWN_LEFT = "images/Character/Left foot forward.png";
	public static final String FACE_DOWN_RIGHT = "images/Character/Right foot forward.png";
	public Direction prevDirection;
    private int currentFrame = 0;
    private boolean goingUp = true;
    private GImage[] sprites;
    private GImage[] playerSprites;
    private GImage playerSprite;
    private GImage sprite;
    
    private GImage currentSprite; // The current displayed sprite
    private GImage[] spritesRight;
    private GImage[] spritesLeft;
    private GImage[] spritesUp;
    private GImage[] spritesDown;
    
    private GImage[] playerSpritesRight;
    private GImage[] playerSpritesLeft;
    private GImage[] playerSpritesUp;
    private GImage[] playerSpritesDown;
    private GImage[] playerSpritesUpRight;
    private GImage[] playerSpritesUpLeft;
    private GImage[] playerSpritesDownRight;
    private GImage[] playerSpritesDownLeft;
    
    

	
//	cell width 72.72727272727273
//	cell height 54.54545454545454
	
	public double getDX() {
		return dx;
	} 

	public void setDX(double dx) {
		this.dx = dx;
	}

	public double getDY() {
		return dy;
	}

	public void setDY(double dy) {
		this.dy = dy;
	}
	
	public void setPlayerImageAndSize(String fileName, double rows, double cols) {
		player.setImage(fileName);
		player.setSize(PROGRAM_WIDTH / rows,  PROGRAM_HEIGHT / cols);
	}
	
	public void moveMaze(Direction d, double rows, double cols) {
		System.out.println(d);
		switch (d) {
			case UP:
				updateSprite(Direction.UP);
				if (dy - PROGRAM_HEIGHT / (rows * 4) > PROGRAM_HEIGHT / rows) {
					System.out.print("Moving up!");
					player.move(0, -(PROGRAM_HEIGHT / (rows * 4)));
					dy = dy - (PROGRAM_HEIGHT / (rows * 4));
				}
				else {
					player.setLocation(dx, PROGRAM_HEIGHT / rows);
					dy = PROGRAM_HEIGHT / rows;
				}
	//			currLocation.setRow(currLocation.getRow() - 1);
	//			this.startRow = currLocation.getRow();
				
				break;
			case DOWN:
				updateSprite(Direction.DOWN);
//				if (dy + (PROGRAM_HEIGHT / (rows * 4))  < PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2) {
					System.out.print("Moving down!");
					player.move(0, (PROGRAM_HEIGHT / (rows * 4)));
					dy = dy + (PROGRAM_HEIGHT / (rows * 4));
//				}
//				else {
//					player.setLocation(dx, PROGRAM_HEIGHT - PROGRAM_HEIGHT /rows * 2);
//					dy = PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2;
//				}
	//			currLocation.setRow(currLocation.getRow() + 1);
	//			this.startRow = currLocation.getRow();
				
				break;
			case LEFT:
				updateSprite(Direction.LEFT);
				if (dx - (PROGRAM_WIDTH / (cols * 4)) > PROGRAM_WIDTH / cols) { 
					System.out.print("Moving left!");
					player.move(-(PROGRAM_WIDTH / (cols * 4)), 0);
					dx = dx - (PROGRAM_WIDTH / (cols * 4));
				}
				else {
					player.setLocation( PROGRAM_WIDTH / cols, dy);
					dx =  PROGRAM_WIDTH / cols;
				}
	//			currLocation.setCol(currLocation.getCol() - 1);
	//			this.startCol = currLocation.getCol();
	
				break;
			case RIGHT:
				updateSprite(Direction.RIGHT);
				if (dx + (PROGRAM_WIDTH / (cols * 4)) <  PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2) {
				System.out.print("Moving right!");
				player.move((PROGRAM_WIDTH / (cols * 4)), 0);
				dx = dx + (PROGRAM_WIDTH / (cols * 4));
					}
				else {
					player.setLocation(PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2, dy);
					dx =  PROGRAM_WIDTH - PROGRAM_WIDTH / cols  * 2;
				}
		}
	}

	public void move(Direction d, double rows, double cols) {
		System.out.println(d);
		switch (d) {
			case UP:
				updatePlayerSprite(Direction.UP);
				
				if (dy - PROGRAM_HEIGHT / (rows * 4) > PROGRAM_HEIGHT / rows) {
					System.out.print("Moving up!");
					player.move(0, -(PROGRAM_HEIGHT / (rows * 4)));
					dy = dy - (PROGRAM_HEIGHT / (rows * 4));
				}
				else {
					player.setLocation(dx, PROGRAM_HEIGHT / rows);
					dy = PROGRAM_HEIGHT / rows;
				}
//				currLocation.setRow(currLocation.getRow() - 1);
//				this.startRow = currLocation.getRow();
				
				break;
			case DOWN:
				updatePlayerSprite(Direction.DOWN);
				
				if (dy + (PROGRAM_HEIGHT / (rows * 4))  < PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2) {
					System.out.print("Moving down!");
					player.move(0, (PROGRAM_HEIGHT / (rows * 4)));
					dy = dy + (PROGRAM_HEIGHT / (rows * 4));
				}
				else {
					player.setLocation(dx, PROGRAM_HEIGHT - PROGRAM_HEIGHT /rows * 2);
					dy = PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2;
				}
//				currLocation.setRow(currLocation.getRow() + 1);
//				this.startRow = currLocation.getRow();
				
				break;
			case LEFT:
				updatePlayerSprite(Direction.LEFT);
				
				if (dx - (PROGRAM_WIDTH / (cols * 4)) > PROGRAM_WIDTH / cols) { 
					System.out.print("Moving left!");
					player.move(-(PROGRAM_WIDTH / (cols * 4)), 0);
					dx = dx - (PROGRAM_WIDTH / (cols * 4));
				}
				else {
					player.setLocation( PROGRAM_WIDTH / cols, dy);
					dx =  PROGRAM_WIDTH / cols;
				}
//				currLocation.setCol(currLocation.getCol() - 1);
//				this.startCol = currLocation.getCol();

				break;
			case RIGHT:
				updatePlayerSprite(Direction.RIGHT);
				
				if (dx + (PROGRAM_WIDTH / (cols * 4)) <  PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2) {
					System.out.print("Moving right!");
					player.move((PROGRAM_WIDTH / (cols * 4)), 0);
					dx = dx + (PROGRAM_WIDTH / (cols * 4));
				}
				else {
					player.setLocation(PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2, dy);
					dx =  PROGRAM_WIDTH - PROGRAM_WIDTH / cols  * 2;
				}
//				currLocation.setCol(currLocation.getCol() + 1);
//				this.startCol = currLocation.getCol();
				
				break;
			case UP_RIGHT:
				updatePlayerSprite(Direction.UP_RIGHT);
				
				if (dy - (PROGRAM_HEIGHT / (rows * 4)) > PROGRAM_HEIGHT / rows && dx + (PROGRAM_WIDTH / (cols * 4)) < PROGRAM_WIDTH - PROGRAM_WIDTH / cols  * 2) {

				//	System.out.print("Moving up_right!");
					player.move(7.07106781187, -7.07106781187);
					dx = dx + 7.07106781187;
					dy = dy - 7.07106781187;
	//				currLocation.setRow(currLocation.getRow() - 1);
	//				currLocation.setCol(currLocation.getCol() + 1);
	//				this.startRow = currLocation.getRow();
	//				this.startCol = currLocation.getCol();

				}
				else if (dx + (PROGRAM_WIDTH / (cols * 4)) < PROGRAM_WIDTH - PROGRAM_WIDTH / cols  * 2) {
					player.move(10, 0);
					dx = dx + 10;
					if(dy - 10 < PROGRAM_HEIGHT / rows) {
						player.setLocation(dx, PROGRAM_HEIGHT / rows);
						dy = PROGRAM_HEIGHT / rows;
					}
				}
				else if(dy - 10 > PROGRAM_HEIGHT / rows) {
					player.move(0, -10);
					dy = dy - 10;
					if (dx + 10 > PROGRAM_WIDTH - PROGRAM_WIDTH / cols  * 2) {
						player.setLocation( PROGRAM_WIDTH - PROGRAM_WIDTH / cols  * 2, dy);
						dx =  PROGRAM_WIDTH - PROGRAM_WIDTH / cols  * 2;
						}
					}
				else {
					player.setLocation(PROGRAM_WIDTH - PROGRAM_WIDTH / cols  * 2, PROGRAM_HEIGHT / rows);
					dy = PROGRAM_HEIGHT / rows;
					dx =  PROGRAM_WIDTH - PROGRAM_WIDTH / cols  * 2;
				}
				break;
			case UP_LEFT:
				updatePlayerSprite(Direction.UP_LEFT);
				
				if (dy - 10 > PROGRAM_HEIGHT / rows && dx - 10 > PROGRAM_WIDTH / cols) {

				//System.out.print("Moving up_left!");
				player.move(-7.07106781187, -7.07106781187);
				dx = dx - 7.07106781187;
				dy = dy - 7.07106781187;
//				currLocation.setRow(currLocation.getRow() - 1);
//				currLocation.setCol(currLocation.getCol() - 1);
//				this.startRow = currLocation.getRow();
//				this.startCol = currLocation.getCol();
				}
				else if (dx - 10 > PROGRAM_WIDTH / cols) {
					player.move(-10, 0);
					dx = dx - 10;
					if(dy - 10 < PROGRAM_HEIGHT / rows) {
						player.setLocation(dx, PROGRAM_HEIGHT / rows);
						dy = PROGRAM_HEIGHT / rows;
					}
				}
				else if(dy - 10 > PROGRAM_HEIGHT / rows) {
					player.move(0, -10);
					dy = dy - 10;
					if (dx + 10 < PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2) {
						player.setLocation(PROGRAM_WIDTH / cols, dy);
						dx = PROGRAM_WIDTH / cols;
						}
					}
				else {
					player.setLocation(PROGRAM_WIDTH / cols, PROGRAM_HEIGHT / rows);
					dy = PROGRAM_HEIGHT / rows;
					dx =  PROGRAM_WIDTH / cols;
				}
				break;
			case DOWN_RIGHT:
				updatePlayerSprite(Direction.DOWN_RIGHT);
				if (dy + 10  < PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2 && dx + 10 < PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2) {

				//System.out.print("Moving down_right!");
				player.move(7.07106781187, 7.07106781187);
				dx = dx + 7.07106781187;
				dy = dy + 7.07106781187;
//				currLocation.setRow(currLocation.getRow() + 1);
//				currLocation.setCol(currLocation.getCol() + 1);
//				this.startRow = currLocation.getRow();
//				this.startCol = currLocation.getCol();
				}
				else if (dx + 10 < PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2) {
					player.move(10, 0);
					dx = dx + 10;
					if(dy + 10  > PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2) {
						player.setLocation(dx, PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2);
						dy = PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2;
					}
				}
				else if(dy + 10  < PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2) {
					player.move(0, 10);
					dy = dy + 10;
					if (dx + 10 > PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2) {
						player.setLocation(PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2, dy);
						dx =  PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2;
						}
					}
				else {
					player.setLocation(PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2,PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2);
					dy = PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2;
					dx = PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2;
				}
				break;
			case DOWN_LEFT:
				updatePlayerSprite(Direction.DOWN_LEFT);
				if (dy + 10  < PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2 && dx - 10 > PROGRAM_WIDTH / cols) {
				System.out.print("Moving down_left!");
				player.move(-7.07106781187, 7.07106781187);
				dx = dx - 7.07106781187;
				dy = dy + 7.07106781187;
//				currLocation.setRow(currLocation.getRow() + 1);
//				currLocation.setCol(currLocation.getCol() - 1);
//				this.startRow = currLocation.getRow();
//				this.startCol = currLocation.getCol();
				}
				else if (dx - 10 > PROGRAM_WIDTH / cols) {
					player.move(-10, 0);
					dx = dx - 10;
					if(dy + 10  > PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2) {
						player.setLocation(dx, PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2);
						dy = PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2;
					}
				}
				else if(dy + 10  < PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2) {
					player.move(0, 10);
					dy = dy + 10;
					if (dx + 10 < PROGRAM_WIDTH - PROGRAM_WIDTH / cols * 2) {
						player.setLocation(PROGRAM_WIDTH / cols, dy);
						dx = PROGRAM_WIDTH / cols;
						}
					}
				else {
					player.setLocation(PROGRAM_WIDTH / cols,PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2);
					dy = PROGRAM_HEIGHT - PROGRAM_HEIGHT / rows * 2;
					dx =  PROGRAM_WIDTH / cols;
				}
				break;
		default:
			System.out.print("Invalid direction");
		}
	}
	
	private GImage[] loadSpritesFromFolder(String[] fileNames) {
	    GImage[] sprites = new GImage[fileNames.length];
	    for (int i = 0; i < fileNames.length; i++) {
	        sprites[i] = new GImage(fileNames[i]);
	    }
	    return sprites;
	}
	
	public PlayerCharacter(int startRow, int startCol, double dx, double dy, String name) {
		charName = name;
		this.startRow = startRow;
		this.startCol = startCol;
		this.dx = dx;
		this.dy = dy;;
		player = new GImage(IDLE_FORWARD);
		currLocation = new Location (startRow, startCol);
		
		
//		String[] rightFiles = {"pac_right/pac1.png", "pac_right/pac2.png", "pac_right/pac3.png", "pac_right/pac4.png"};
//	    String[] leftFiles = {"pac_left/pac1.png", "pac_left/pac2.png", "pac_left/pac3.png", "pac_left/pac4.png"};
//	    String[] upFiles = {"pac_up/pac1.png", "pac_up/pac2.png", "pac_up/pac3.png", "pac_up/pac4.png"};
//	    String[] downFiles = {"pac_down/pac1.png", "pac_down/pac2.png", "pac_down/pac3.png", "pac_down/pac4.png"};
		String[] rightFiles = {"pac_right/pac1.png", "pac_right/pac2.png", "pac_right/pac3.png"};
	    String[] leftFiles = {"pac_left/pac1.png", "pac_left/pac2.png", "pac_left/pac3.png"};
	    String[] upFiles = {"pac_up/pac1.png", "pac_up/pac2.png", "pac_up/pac3.png"};
	    String[] downFiles = {"pac_down/pac1.png", "pac_down/pac2.png", "pac_down/pac3.png"};
	    
	    
	    String[] playerLeftFiles = {"Images/Character/left/left1.png", "Images/Character/left/left2.png", "Images/Character/left/left3.png"};
	    String[] playerRightFiles = {"Images/Character/right/right1.png", "Images/Character/right/right2.png", "Images/Character/right/right3.png"};
	    String[] playerUpFiles = {"Images/Character/up/up1.png", "Images/Character/up/up2.png", "Images/Character/up/up3.png"};
	    String[] playerDownFiles = {"Images/Character/down/down1.png", "Images/Character/down/down2.png", "Images/Character/down/down3.png"};
	    
	    String[] playerUpLeftFiles = {"Images/Character/up left/up left1.png", "Images/Character/up left/up left2.png"};
	    String[] playerUpRightFiles = {"Images/Character/up right/up right1.png", "Images/Character/up right/up right2.png"};
	    String[] playerDownLeftFiles = {"Images/Character/down left/down left1.png", "Images/Character/down left/down left2.png"};
	    String[] playerDownRightFiles = {"Images/Character/down right/down right1.png", "Images/Character/down right/down right2.png"};
	    spritesRight = loadSpritesFromFolder(rightFiles);
	    spritesLeft = loadSpritesFromFolder(leftFiles);
	    spritesUp = loadSpritesFromFolder(upFiles);
	    spritesDown = loadSpritesFromFolder(downFiles);
	    
	    playerSpritesRight = loadSpritesFromFolder(playerRightFiles);
	    playerSpritesLeft = loadSpritesFromFolder(playerLeftFiles);
	    playerSpritesUp = loadSpritesFromFolder(playerUpFiles);
	    playerSpritesDown = loadSpritesFromFolder(playerDownFiles);
	    playerSpritesUpRight = loadSpritesFromFolder(playerUpRightFiles);
	    playerSpritesUpLeft = loadSpritesFromFolder(playerUpLeftFiles);
	    playerSpritesDownRight = loadSpritesFromFolder(playerDownRightFiles);
	    playerSpritesDownLeft = loadSpritesFromFolder(playerDownLeftFiles);
	    
	    playerSprites = playerSpritesLeft;
	    for (int i = 0; i < playerSpritesLeft.length; i++) {
	        playerSpritesLeft[i] = new GImage("Images/Character/left/left" + (i + 1) + ".png");
	        //playerSpritesLeft[i].setSize(26, 25); 
	    }
	    sprites = spritesDown;
	    
	    for (int i = 0; i < sprites.length; i++) {
	        sprites[i] = new GImage("pac_down/pac" + (i + 1) + ".png");
	        sprites[i].setSize(26, 25); 
	    }
	    sprite = sprites[0];
	    currentSprite = sprites[0]; 
	}
	
	public int getStartRow() {
		return startRow;
	}
	public int getStartCol() {
		return startCol;
	}
	@Override
	public void Character(String name) {
		charName = name;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	public void updatePlayerSprite(Direction direction) {
        switch (direction) {
	        case UP:
	            sprites = playerSpritesUp;
	            break;
	        case DOWN:
	            sprites = playerSpritesDown;
	            break;
	        case LEFT:
	            sprites = playerSpritesLeft; 
	            break;
	        case RIGHT:
	            sprites = playerSpritesRight;
	            break;
        }
        
        currentFrame = (currentFrame + 1) % sprites.length;
        currentSprite = sprites[currentFrame];

        // Update the player image to the current sprite
        player.setImage(currentSprite.getImage());
        player.setSize(PROGRAM_WIDTH/8, PROGRAM_HEIGHT/8);
        
    }
	
    public void updateSprite(Direction direction) {
        switch (direction) {
	        case UP:
	            sprites = spritesUp;
	            break;
	        case DOWN:
	            sprites = spritesDown;
	            break;
	        case LEFT:
	            sprites = spritesLeft; 
	            break;
	        case RIGHT:
	            sprites = spritesRight;
	            break;
        }
        
        currentFrame = (currentFrame + 1) % sprites.length;
        currentSprite = sprites[currentFrame];

        // Update the player image to the current sprite
        player.setImage(currentSprite.getImage());
        
    }
    
    private GImage getNextSprite(GImage[] sprites) {
        currentFrame = (currentFrame + 1) % sprites.length;
        return sprites[currentFrame];
    }

    public GImage getSprite() {
        return player;
    }

	public void setPlayerImageAndSize(GImage sprite2, int mazeRows, int mazeCols) {
		// TODO Auto-generated method stub
		player = sprite2;
		player.setSize(26,  25);
	}

	

}

