package main.character;

import main.obstacles.MovingObstacle;
import main.utilities.Movable;
import main.utilities.Position;
import main.utilities.Skin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Character extends Movable implements ActionListener {
	private Skin skin;
	private boolean dead; //true if the character is dead
	private boolean hit; //true if the character is hit by an obstacle
	private int velocity; //it will be used to move the character
	private int gravity; //it will determine if the character goes down (default) or up if it jumps
	private Timer timer;
	
	private final int DELAY=1; /*a constant used to make the typical effect 
										of the bird's jump in flappy bird*/
	private final int GO_UP=-12; 
	private final int GO_DOWN=8;
	private final int TIMER_DELAY=60;
	
	
	Character(Position position, Skin skin) {
		super(position);
		this.skin=skin;
		this.dead=false;
		this.hit=false;
		this.velocity=0;
		this.gravity=GO_DOWN;
		this.timer = new Timer(TIMER_DELAY, this);
		this.timer.start();
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	public void die() {
		this.dead=true;
	}
	
	public boolean isHit() {
		return this.hit;
	}
	
	public void hit() {
		this.hit=true;
	}
	
	public void jump() {
		this.gravity = GO_UP;
		//to-do: add rotation
	}
	
	public void collide(FixedObstacle fixedObstacle) {
		//variables to make it more readable
		int characterPointOfCollisionX = this.getPosition().getX() + (int) this.skin.getImage().getWidth(null);
		int characterPointOfCollisionY = this.getPosition().getY();
		int obstacleLeftLimit = fixedObstacle.getPosition().getX();
		int obstacleRightLimit = fixedObstacle.getPosition().getX() + (int) fixedObstacle.getSkin().getImage().getWidth(null);
		int upperObstacleRange = fixedObstacle.getPosition().getY() + (int) FixedObstacle.space/2;
		int lowerObstacleRange = fixedObstacle.getPosition().getY() - (int) FixedObstacle.space/2;
		
		//the purpose of this if is to check if the character is in the range of the obstacle
		if (characterPointOfCollisionX >= obstacleLeftLimit
			&& characterPointOfCollisionX <= obstacleRightLimit) {
					
			//the purpose of this if is to check if the character hits the obstacle
			if (characterPointOfCollisionY >= upperObstacleRange
				|| characterPointOfCollisionY <= lowerObstacleRange) {
						
				this.isHit();
				this.isDead();
			}
		}
	}

	public void collide(MovingObstacle movingObstacle) {
		//variables to make it more readable
		int characterY = this.getPosition().getY();
		int obstacleHeightUpperLimit = movingObstacle.getPosition().getY();
		int obstacleHeightLowerLimit = characterY + (int) movingObstacle.getSkin().getImage().getHeight(null);
		int characterPointOfCollision = this.getPosition().getX() + (int) this.skin.getImage().getWidth(null);
		int obstacleX = movingObstacle.getPosition().getX();
		
		//the purpose of this if is to check if the character is in the range of the obstacle
		if (characterY >= obstacleHeightUpperLimit
			&& characterY <= obstacleHeightLowerLimit) {
			
			//the purpose of this if is to check if the character hits the obstacle
			if (characterPointOfCollision == obstacleX) {
						
				this.isHit();
				this.isDead();
			}
		}
	}
	
	public void collideBorders() {
		if (this.getPosition().getX() 
			== 0 
			|| this.getPosition().getX() 
			== (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
			
			this.isHit();
			this.isDead();
		}
	}
	
	public void setSkin(Skin skin){
		this.skin = skin;
	}
	
	public Skin getSkin() {
		return this.skin;
	}
	
	@Override
	public void animate(Graphics2D canvas) {
		canvas.drawImage(this.skin.getImage(), this.getPosition().getX(),
												this.getPosition().getY() + this.velocity,
												(int) this.skin.getImage().getWidth(null),
												(int) this.skin.getImage().getHeight(null),	null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		gravity += DELAY;
		velocity += gravity;
		
		repaint(); /* to show the changes, I'll need to call this method on the
		 				game GUI*/
	}
	
}
