package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import acm.graphics.*;

public class PauseMenu extends GraphicsPane {
	private MainApplication program;
	private GButton Resume;
	private GButton settings;
	private GButton instruction;
	private GButton exit;
	GImage backgroundImage;
	private Overlay overlay;
	// created some new color, we can change it later for better looking
	private static final Color golden_rod = new Color(238, 232, 170);
	private static final Color parchment = new Color(238, 209, 156);
	private static final Color khaki = new Color(240, 230, 140);

	public PauseMenu(MainApplication app) {
		super();
		program = app;

		backgroundImage = new GImage("randombg.jpg", 0, 0);
		backgroundImage.setSize(program.getWidth(), program.getHeight());

		Resume = new GButton("Resume", 300, 175, 200, 50);
		Resume.setFillColor(golden_rod);

		settings = new GButton("Settings", 300, 250, 200, 50);
		settings.setFillColor(parchment);


		instruction = new GButton("Instruction", 300, 325, 200, 50);
		instruction.setFillColor(golden_rod);

		exit = new GButton("Exit", 300, 400, 200, 50);
		exit.setFillColor(khaki);
	}

	@Override
	public void showContents() {
		program.setPauseMenuState(true);
		program.add(backgroundImage);
		program.add(Resume);
		program.add(settings);
		program.add(exit);
		program.add(instruction);
	}

	@Override
	public void hideContents() {
		program.setPauseMenuState(false);
		program.remove(backgroundImage);
		program.remove(Resume);
		program.remove(settings);
		program.remove(exit);
		program.remove(instruction);

	}

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (program.isInPauseMenu()) {
			if (obj == Resume) {
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
			} else if (obj == settings) {
				program.playClickSound();
				program.switchToPauseSettings();
			} else if (obj == exit) {
				program.playClickSound();
				program.switchToMenu();
			}
			else if (obj == instruction) {
				program.playClickSound();
				program.switchToGameInstructionPanel();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE) {
			if (program.isInPauseMenu()) {
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
