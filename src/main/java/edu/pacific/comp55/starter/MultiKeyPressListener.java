package edu.pacific.comp55.starter;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// Code adapted from https://stackoverflow.com/questions/2623995/swings-keylistener-and-multiple-keys-pressed-at-the-same-time

public class MultiKeyPressListener implements KeyListener {
	// Set of currently pressed keys
	private final Set<Integer> pressedKeys = new HashSet<>();
	Point offset = new Point();

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		pressedKeys.add(e.getKeyCode());

		if (!pressedKeys.isEmpty()) {
			for (Iterator<Integer> it = pressedKeys.iterator(); it.hasNext();) {
				switch (it.next()) {
				case KeyEvent.VK_UP:
					offset.y = -1;
					break;
				case KeyEvent.VK_LEFT:
					offset.x = -1;
					break;
				case KeyEvent.VK_DOWN:
					offset.y = 1;
					break;
				case KeyEvent.VK_RIGHT:
					offset.x = 1;
					break;
				}
			}
		}
		System.out.println(offset); // Do something with the offset.
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		System.out.println("keyReleased called");
		if (code == KeyEvent.VK_UP) {
			offset.y = 0;
			System.out.println("Up offset reset.");
			return;
		} else if (code == KeyEvent.VK_LEFT) {
			offset.x = 0;
			System.out.println("Left offset reset");
			return;
		} else if (code == KeyEvent.VK_DOWN) {
			offset.y = 0;
			System.out.println("Down offset reset.");
			return;
		} else if (code == KeyEvent.VK_RIGHT) {
			offset.x = 0;
			System.out.println("Right offset reset.");
			return;
		}
		pressedKeys.remove(e.getKeyCode());
	}
}