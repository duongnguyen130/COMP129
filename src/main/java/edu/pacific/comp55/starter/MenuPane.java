package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import acm.graphics.*;

public class MenuPane extends GraphicsPane {
	private MainApplication program;
	private GButton play;
	private GButton settings;
	private GButton leaderboard;
	private GButton quit;
	private GButton instruction;
	private GButton credit;

	GImage backgroundImage;
	private static final Color golden_rod = new Color(238, 232, 170);

	public MenuPane(MainApplication app) {
		super();
		program = app;

		backgroundImage = new GImage("background.gif", 0, 0);
		backgroundImage.setSize(program.getWidth(), program.getHeight());

		// Buttons
		play = new GButton("Play", 450, 150, 200, 50);
		play.setFillColor(golden_rod);

		settings = new GButton("Settings", 450, 225, 200, 50);
		settings.setFillColor(golden_rod);

		leaderboard = new GButton("Leaderboard", 450, 300, 200, 50);
		leaderboard.setFillColor(golden_rod);

		instruction = new GButton("Instruction", 450, 375, 200, 50);
		instruction.setFillColor(golden_rod);

		credit = new GButton("Credit", 450, 450, 200, 50);
		credit.setFillColor(golden_rod);

		quit = new GButton("Quit", 450, 525, 200, 50);
		quit.setFillColor(golden_rod);

	}

	@Override
	public void showContents() {
		program.setMainMenuState(true);
		program.add(backgroundImage);
		program.add(play);
		program.add(settings);
		program.add(leaderboard);
		program.add(instruction);
		program.add(credit);
		program.add(quit);
	}

	@Override
	public void hideContents() {
		program.setMainMenuState(false);
		program.remove(backgroundImage);
		program.remove(play);
		program.remove(settings);
		program.remove(leaderboard);
		program.remove(instruction);
		program.remove(credit);
		program.remove(quit);
	}

	private void quit() {
		System.exit(0);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (program.isInMainMenu()) {
			if (obj == play) {
				this.hideContents();
				program.playClickSound();
				program.switchToBaseRoom();
			} else if (obj == settings) {
				this.hideContents();
				program.playClickSound();
				program.switchToSettings();
			}

			else if (obj == leaderboard) {
				this.hideContents();
				program.playClickSound();
				program.switchToLeaderBoard();
			}

			else if (obj == instruction) {
				program.playClickSound();
				program.switchToGameInstructionPanel();
			}

			else if (obj == credit) {
				program.playClickSound();
				program.switchToCredit();
			}

			else if (obj == quit) {
				program.playClickSound();
				quit();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_UP) {
			System.out.println("Up arrow key pressed");
		} else if (keyCode == KeyEvent.VK_DOWN) {
			// Handle the Down arrow key press
			System.out.println("Down arrow key pressed");
		} else if (keyCode == KeyEvent.VK_LEFT) {
			// Handle the Left arrow key press
			System.out.println("Left arrow key pressed");
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			// Handle the Right arrow key press
			System.out.println("Right arrow key pressed");
		} else if (keyCode == KeyEvent.VK_ESCAPE) {
			if (program.isInbaseRoom()) {
				program.switchToPause();
			} else if (program.isInPauseMenu()) {
				program.switchToBaseRoom();
			}
		} else {
			// Handle other key presses
			char keyChar = e.getKeyChar();
			System.out.println("Key pressed: " + keyChar);
		}
	}

}
