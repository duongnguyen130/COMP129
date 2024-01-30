package edu.pacific.comp55.starter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Timer;

import acm.graphics.GImage;
import acm.graphics.GLabel;

public class Overlay extends GraphicsPane implements ActionListener {
	private MainApplication program;
	private static final String HEART = "heart.png";
	private static final String EMPTY_HEART = "EmptyHeart.png";
	public static final String LABEL_FONT = "Times-New-Roman-Bold-22";

	private HashMap<Integer, GLabel> task = new HashMap<Integer, GLabel>();
	private ArrayList<String> leader = new ArrayList<String>();

	private GImage Heart1;
	private GImage Heart2;
	private GImage Heart3;
	private GImage EmptyHeart1;
	private GImage EmptyHeart2;
	private GImage EmptyHeart3;
	private GLabel Timer;
	private GLabel Tasks;
	private Timer someTimerVar;

	public int seconds = 0;
	public int minutes = 0;
	public int damageTaken = 0;

	public Overlay(MainApplication app) {
		super();
		program = app;
		Heart1 = new GImage(HEART, 15, 10);
		Heart1.setSize(35, 35);

		Heart2 = new GImage(HEART, 55, 10);
		Heart2.setSize(35, 35);

		Heart3 = new GImage(HEART, 95, 10);
		Heart3.setSize(35, 35);

		EmptyHeart1 = new GImage(EMPTY_HEART, 15, 10);
		EmptyHeart1.setSize(35, 35);

		EmptyHeart2 = new GImage(EMPTY_HEART, 55, 10);
		EmptyHeart2.setSize(35, 35);

		EmptyHeart3 = new GImage(EMPTY_HEART, 95, 10);
		EmptyHeart3.setSize(35, 35);

		Tasks = new GLabel("Tasks: ", program.getWidth() - 300, 20);

		Tasks.setFont(LABEL_FONT);
		Tasks.setColor(new Color(255, 255, 255, 200));

		Timer = new GLabel("00:00", program.getWidth() - 80, program.getHeight() - 50);
		Timer.setFont(LABEL_FONT);
		Timer.setColor(new Color(255, 255, 255, 200));
		someTimerVar = new Timer(1000, (this));

	}

	public void addtoLeaders() {
		leader.add(totalTime());
	}

	public ArrayList<String> getLeaders() {
		return leader;
	}

	public void startTimer() {
		someTimerVar.start();
	}

	public void stopTimer() {
		someTimerVar.stop();
		System.out.println(leader.size());
	}

	public void addTask(int bug) {
		if (bug == 0 || bug == 2 || bug == 4 || bug == 6) {
			GLabel error = new GLabel("Try the Arcade Machine", program.getWidth() - 300, 45);
			task.put(bug, error);
			error.setFont(LABEL_FONT);
			error.setColor(new Color(255, 255, 255, 200));
			program.add(error);

		} else if (bug == 1) {
			GLabel error = new GLabel("Find the Maze", program.getWidth() - 300, 45);
			task.put(bug, error);
			error.setFont(LABEL_FONT);
			error.setColor(new Color(255, 255, 255, 200));
			program.add(error);
		} else if (bug == 3) {
			GLabel error = new GLabel("Save the Special Bugs", program.getWidth() - 300, 45);
			task.put(bug, error);
			error.setFont(LABEL_FONT);
			error.setColor(new Color(255, 255, 255, 200));
			program.add(error);
		} else if (bug == 5) {
			GLabel error = new GLabel("Defeat all 20 enemies", program.getWidth() - 300, 45);
			task.put(bug, error);
			error.setFont(LABEL_FONT);
			error.setColor(new Color(255, 255, 255, 200));
			program.add(error);

		} else if (bug == 10) {
			GLabel error = new GLabel("Return to Main Room", program.getWidth() - 300, 45);
			task.put(bug, error);
			error.setFont(LABEL_FONT);
			error.setColor(new Color(255, 255, 255, 200));
			program.add(error);
		}
	}

	public void removeTask(int bug) {
		program.remove(task.get(bug));
		task.remove(bug);
	}

	public void heartCount() {
		program.add(Heart1);
		program.add(Heart2);
		program.add(Heart3);
		System.out.println("test");
		if (damageTaken == 1) {
			program.remove(Heart3);
			program.add(EmptyHeart3);
		}
		if (damageTaken == 2) {
			program.remove(Heart3);
			program.add(EmptyHeart3);
			program.remove(Heart2);
			program.add(EmptyHeart2);
		}
		if (damageTaken >= 3) {
			program.remove(Heart3);
			program.add(EmptyHeart3);
			program.remove(Heart2);
			program.add(EmptyHeart2);
			program.remove(Heart1);
			program.add(EmptyHeart1);
			program.switchToDeathMenu();
		}
	}

	public void removeHeartCount() {
		program.remove(Heart1);
		program.remove(Heart2);
		program.remove(Heart3);
		program.remove(EmptyHeart1);
		program.remove(EmptyHeart2);
		program.remove(EmptyHeart3);
	}

	@Override
	public void showContents() {
		heartCount();
		program.add(Timer);
		program.add(Tasks);
	}

	@Override
	public void hideContents() {
		program.setRightRoomState(false);
		program.remove(Heart1);
		program.remove(Heart2);
		program.remove(Heart3);
		program.remove(Timer);
		program.remove(Tasks);
	}

	public void actionPerformed(ActionEvent e) {
		seconds++;
		secondsToMinutes();
		Timer.setLabel(totalTime());

	}

	// Helper Function
	public void secondsToMinutes() {
		if (seconds == 60) {
			seconds = 0;
			minutes++;
		}
	}

	public String totalTime() {
		if (minutes < 10) {
			if (seconds < 10) {
				return ("0" + minutes + ":0" + seconds);
			} else {
				return ("0" + minutes + ":" + seconds);
			}
		} else {
			if (seconds < 10) {
				return (minutes + ":0" + seconds);
			} else {
				return (minutes + ":" + seconds);
			}
		}

	}

}
