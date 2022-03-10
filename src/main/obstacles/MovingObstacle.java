package main.obstacles;

import java.awt.Toolkit;

public class MovingObstacle extends Obstacle {

	public MovingObstacle() {
		super();
		this.x = (Toolkit.getDefaultToolkit().getScreenSize().width)+1;
		this.y = (Toolkit.getDefaultToolkit().getScreenSize().height)/2;
	}

	public void movingPattern() {
		while (true) {
			if(y == (Toolkit.getDefaultToolkit().getScreenSize().height)/2) {
				for (int i = 1; i <= 5; i++) {
					this.y = this.updateView(this.y, 1);
					System.out.println(y);
				}
				this.sleep(800);
			}else {
				for (int i = 1; i <= 10; i++) {
					this.y = this.updateView(this.y, -1);
					System.out.println(y);
				}
				this.sleep(800);

				for (int i = 1; i <= 0; i++) {
					this.y = this.updateView(this.y, +1);
					System.out.println(y);
				}
				this.sleep(800);
			}
		}
	}
	public boolean validPositionY(int y) {
		int maxHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		if(y <= maxHeight && y >= 0) {
			return true;
		}else {
			return false;
		}
	}

	private int updateView(int y, int increment) {
		this.sleep(250);
		if(validPositionY(y+increment)) {
			return y + increment;
		}
		return 0;
	}

	private void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}
