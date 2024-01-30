package edu.pacific.comp55.starter;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GObject;

public class SettingsPane extends GraphicsPane {
    private MainApplication program;
    GImage backgroundImage;
    private GButton soundButton;
    private GButton musicButton;
    private GButton backButton;
    private boolean isSoundOn = true;
    private boolean isMusicOn = true;
    
    
	//created some new color
    private static final Color golden_rod = new Color(238, 232, 170);
    private static final Color parchment = new Color(238, 209, 156);
    private static final Color khaki = new Color(240, 230, 140);

    public SettingsPane(MainApplication app) {
    	super();
        this.program = app;

        soundButton = new GButton("Sound: On", 300, 175, 200, 50);
        musicButton = new GButton("Music: On", 300, 250, 200, 50);
        backButton = new GButton("Back to Menu", 300, 325, 200, 50);
        backgroundImage = new GImage("randombg.jpg", 0, 0);
        backgroundImage.setSize(program.getWidth(), program.getHeight());

        soundButton.addActionListener(e -> toggleSound());
        soundButton.setFillColor(golden_rod);
        musicButton.addActionListener(e -> toggleMusic());
        musicButton.setFillColor(parchment);
        backButton.addActionListener(e -> program.switchToMenu());
        backButton.setFillColor(khaki);

    }

    @Override
    public void showContents() {
	    program.setSettingMenuState(true);
	    program.add(backgroundImage);
        program.add(soundButton);
        program.add(musicButton);
        program.add(backButton);
    }

    @Override
    public void hideContents() {
	    program.setSettingMenuState(false);
	    program.remove(backgroundImage);
        program.remove(soundButton);
        program.remove(musicButton);
        program.remove(backButton);
    }

    private void toggleSound() {
        if (isSoundOn) {
            isSoundOn = false;
            soundButton.setLabel("Sound: Off");
            program.turnSoundOff();
            program.stopMusic();

        } else {
            isSoundOn = true;
            soundButton.setLabel("Sound: On");
            program.turnSoundOn();
            program.resumeMusic();
        }
    }
    private void toggleMusic() {
        if (isMusicOn) {
            isMusicOn = false;
            musicButton.setLabel("Music: Off");
            program.stopMusic();
            
        } else {
            isMusicOn = true;
            musicButton.setLabel("Music: On");
            program.resumeMusic();
        }
    }

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (program.isInSettingMenu()) {
			if (obj == soundButton) {
				program.playClickSound();
				//Add method to turn on and off sound later here!!
				toggleSound();
			} else if (obj == musicButton) {
				program.playClickSound();
				//Add method to turn on and off music later here!!
				toggleMusic();
			} else if (obj == backButton) {
				this.hideContents();
				program.playClickSound();
				program.switchToMenu();
			}
		}
	}
	
	@Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE){
        	program.switchToMenu();
        } else {
            // Handle other key presses
            char keyChar = e.getKeyChar();
            System.out.println("Key pressed: " + keyChar);
        }
	}
}
