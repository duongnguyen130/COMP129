package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GObject;

public class CreditPane extends GraphicsPane {
	private MainApplication program;
	private GImage backgroundImage;
	private GImage creditbg;
	private GButton backButton;
	private static final Color golden_rod = new Color(238, 232, 170);
	private ArrayList<GLabel> creditTexts;
	private Timer labelAddTimer;
    private int labelIndexToAdd;

	public CreditPane(MainApplication app) {
		this.program = app;
	}
	
	private void animateLabelMovement(GLabel label) {
	    Timer labelMoveTimer = new Timer(50, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            label.move(0, -2);

	            if (label.getY() + label.getHeight() < 112) {
	                program.remove(label); // Remove the label when it's no longer visible
	                ((Timer) e.getSource()).stop(); // Stop the timer
	            }
	        }
	    });

	    labelMoveTimer.start();
	}

	@Override
	public void showContents() {
		creditTexts = new ArrayList<>();
		creditTexts.add(new GLabel("Sean Dang", 330, 490));
		creditTexts.add(new GLabel("Nguyen Duong", 330, 490));
		creditTexts.add(new GLabel("Charles Thomas", 330, 490));
		creditTexts.add(new GLabel("Ethan Chow", 330, 490));
		backgroundImage = new GImage("background.gif", 0, 0);
		backgroundImage.setSize(program.getWidth(), program.getHeight());

		creditbg = new GImage("blackbg.png", 150, 30);
		creditbg.setSize(500, 500);

		backButton = new GButton("Back to Menu", 300, 500, 200, 50);
		backButton.setFillColor(golden_rod);
		
		labelIndexToAdd = 0;

		labelAddTimer = new Timer(2000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        if (labelIndexToAdd < creditTexts.size()) {
		            GLabel creditsLabel = creditTexts.get(labelIndexToAdd);
		            creditsLabel.setColor(Color.WHITE);
		            creditsLabel.setFont(new Font("Arial", Font.BOLD, 20));

		            program.add(creditsLabel);
		            animateLabelMovement(creditsLabel);

		            labelIndexToAdd++;
		        } else {
		            labelAddTimer.stop();
		        }
		    }
		});
		program.setCreditState(true);
		program.add(backgroundImage);
		program.add(creditbg);
		program.add(backButton);
        labelAddTimer.start();
	}

	@Override
	public void hideContents() {
		program.setCreditState(false);
		program.remove(backgroundImage);
		program.remove(creditbg);
		program.remove(backButton);
        labelAddTimer.stop();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if (program.isInCredit()) {
			if (obj == backButton) {
				this.hideContents();
				program.playClickSound();
				program.switchToMenu();
			}
		}
	}
}
