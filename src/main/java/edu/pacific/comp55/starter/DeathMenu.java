package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import acm.graphics.*;

public class DeathMenu extends GraphicsPane {
	private MainApplication program;
	private GButton Resume;
	GButton settings;
	private GButton exit;
	GImage backgroundImage;
    private static final Color golden_rod = new Color(238, 232, 170);
    private static final Color khaki = new Color(240, 230, 140);

    public DeathMenu(MainApplication app) {
        super();
        program = app;

        backgroundImage = new GImage("diedbackground.jpg", 0, 0);
        backgroundImage.setSize(program.getWidth(), program.getHeight());

        Resume = new GButton("Play Again", 300, 400, 200, 50);
        Resume.setFillColor(golden_rod);
        
        exit = new GButton("Exit", 300, 475, 200, 50);
        exit.setFillColor(khaki);
    }

	@Override
	public void showContents() {
	    program.add(backgroundImage);
		program.add(Resume);
		program.add(exit);
	}

	@Override
	public void hideContents() {
	    program.remove(backgroundImage);
		program.remove(Resume);
		program.remove(exit);
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
			if (obj == Resume) {
				program.playClickSound();
				program.deleteGame();
				program.startGame();
				program.switchToBaseRoom();
			}
			else if (obj == settings) {
				program.playClickSound();
				program.switchToPauseSettings();
			}
			else if (obj == exit) {
				program.playClickSound();
				program.deleteGame();
				program.startGame();
				program.switchToMenu();
			}
		}
	
	
	@Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE){
        	if (program.isInPauseMenu()) {
				program.playClickSound();
        		program.switchToBaseRoom();
        	
        	}
        }
    }

}
