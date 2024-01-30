package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GObject;

public class GameInstructionPanel extends GraphicsPane {
	private MainApplication program; // you will use program to get access to
	GImage backgroundImage;
	private GButton backButton;
	private GImage movement;
	private GImage shooting;
    private GLabel movementLabel;
    private GLabel shootingLabel;
    private GLabel interactLabel;
    private GButton resumeButton;



    private static final Color golden_rod = new Color(238, 232, 170);
    private static final Color blue = new Color(51, 119, 255);


	public GameInstructionPanel(MainApplication app) {
		this.program = app;
		shooting = new GImage("space.png", 480, 100);
		shooting.setSize(200, 200);
        shootingLabel = new GLabel("Press SPACE to shoot", 480, 320);
        shootingLabel.setColor(blue);
        shootingLabel.setFont(new Font("Arial", Font.BOLD, 20));

        interactLabel = new GLabel("or to Interact", 520, 350);
        interactLabel.setColor(blue);
        interactLabel.setFont(new Font("Arial", Font.BOLD, 20));
		
        movement = new GImage("arrow3.png", 120, 100);
        movement.setSize(200, 200);
        movementLabel = new GLabel("Use arrow keys to move", 110, 320);
        movementLabel.setColor(blue);
        movementLabel.setFont(new Font("Arial", Font.BOLD, 20));

		backButton = new GButton("Back to Menu", 300, 475, 200, 50);
		backgroundImage = new GImage("beige.jpg", 0, 0);
		backgroundImage.setSize(program.getWidth(), program.getHeight());
		backButton.setFillColor(golden_rod);
		
		resumeButton = new GButton("Resume", 300, 400, 200, 50);
		backgroundImage = new GImage("beige.jpg", 0, 0);
		backgroundImage.setSize(program.getWidth(), program.getHeight());
		resumeButton.setFillColor(golden_rod);
	}

    @Override
    public void showContents() {
    	program.setGameInstructionState(true);
	    program.add(backgroundImage);
        program.add(backButton);
        program.add(shooting);
        program.add(movement);
        program.add(shootingLabel);
        program.add(movementLabel);
        program.add(interactLabel);  
        program.add(resumeButton);
    }

    @Override
    public void hideContents() {
    	program.setGameInstructionState(false);
	    program.remove(backgroundImage);
        program.remove(backButton);
        program.remove(shooting);
        program.remove(movement);
        program.remove(shootingLabel);
        program.remove(movementLabel);
        program.remove(interactLabel);
        program.remove(resumeButton);
    }


	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (program.isInGameInstruction()) {
			if (obj == backButton) {
				this.hideContents();
				program.playClickSound();
				program.switchToMenu();
			}
			if (obj == resumeButton) {
				program.playClickSound();
				if (program.isInbaseRoom()) {
					program.switchToBaseRoom();
				}
				if (program.isInRightRoom()) {
					program.switchToRightRoom();
				}
				if (program.isInPacman()) {
					program.switchToPacmanGraphic();
				}
				if(program.isInMaze()) {
					program.switchToMazeGraphic();
				}
				
				if(program.isInTopRoom()) {
					program.switchToTopRoom();
				}
				if(program.isInBottomRoom()) {
					program.switchToBottomRoom();
				}
		}
	}

}
}
