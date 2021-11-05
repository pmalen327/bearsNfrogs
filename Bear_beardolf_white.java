package a9;

import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

public class Bear_beardolf_white extends Zombie {

	public Bear_beardolf_white(Double startingPosition, Double initHitbox, BufferedImage img, int health, int coolDown,
			double speed, int attackDamage) {
		super(startingPosition, initHitbox, img, health, coolDown, speed, attackDamage);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setCollisionStatus(Actor other) {
		if (this != other && this.isCollidingOther(other)) {
			setColliding(false);
		}
			
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
