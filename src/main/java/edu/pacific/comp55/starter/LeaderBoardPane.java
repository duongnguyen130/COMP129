package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GObject;

public class LeaderBoardPane extends GraphicsPane {
	private MainApplication program;
	GImage backgroundImage;
	private GButton backButton;
	private GImage creditbg;

	private static final Color khaki = new Color(240, 230, 140);
	private ArrayList<String> leaders = new ArrayList<String>();
	private ArrayList<GLabel> text = new ArrayList<GLabel>();

	private Overlay overlay;
	int y = 100;

	public LeaderBoardPane(MainApplication app) {
		this.program = app;
		overlay = program.getOverlay();
		backButton = new GButton("Back to Menu", 300, 525, 200, 50);
		backgroundImage = new GImage("randombg.jpg", 0, 0);
		backgroundImage.setSize(program.getWidth(), program.getHeight());

		backButton.addActionListener(e -> program.switchToMenu());
		backButton.setFillColor(khaki);

		creditbg = new GImage("blackbg.png", 150, 30);
		creditbg.setSize(500, 500);
	}

	private void transferArray() {
		for (String array : overlay.getLeaders()) {
			leaders.add(array);
		}
	}

	public void addLeaders() {
		transferArray();
		System.out.println(overlay.getLeaders().size());
		int holder = y;

		for (String leader : leaders) {
			GLabel create = new GLabel(leader, program.getWidth() / 2, holder);
			create.setColor(Color.WHITE);
			program.add(create);
			text.add(create);
			holder += 50;
		}
	}

	public void removeLeaders() {
		for (GLabel label : text) {
			program.remove(label);
		}
	}

	@Override
	public void showContents() {
		program.setLeaderBoardState(true);
		program.add(backgroundImage);
		program.add(backButton);
		program.add(creditbg);
		addLeaders();
	}

	@Override
	public void hideContents() {
		leaders.clear();
		program.setLeaderBoardState(false);
		program.remove(backgroundImage);
		program.remove(backButton);
		removeLeaders();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (program.isInLeaderBoard()) {
			if (obj == backButton) {
				this.hideContents();
				program.playClickSound();
				program.switchToMenu();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE) {
			program.switchToMenu();
		} else {
			// Handle other key presses
			char keyChar = e.getKeyChar();
			System.out.println("Key pressed: " + keyChar);
		}
	}
}
