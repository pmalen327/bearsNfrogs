package a9;

import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

public class Frog_robot extends Plant {

	public Frog_robot(Double startingPosition, Double initHitbox, BufferedImage img, int health, int coolDown,
			int attackDamage) {
		super(startingPosition, initHitbox, img, health, coolDown, attackDamage);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void decrementCooldown() {
		for (int i = 0; i < 2; i++) {
			super.decrementCooldown();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
