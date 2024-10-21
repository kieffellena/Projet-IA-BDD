import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Mouvements {

	protected static final int SPEED = 500;

	public  void avancer() { 
		Motor.B.setSpeed(SPEED);
		Motor.D.setSpeed(SPEED);
		Motor.B.forward();
		Motor.D.forward();
		Delay.msDelay(2000);
	}

	public  void reculer() {
		Motor.B.setSpeed(SPEED);
		Motor.D.setSpeed(SPEED);
		Motor.B.backward();
		Motor.D.backward();
		Delay.msDelay(2000);
	}

	public void tourner(int angle) {
		int angleArr=(int) Math.round(4.44*angle);
		Motor.D.rotate(angleArr); 
		Delay.msDelay(2000); 
	}

	public  void fermerPince() {
		Motor.C.setSpeed(SPEED); 
		Motor.C.backward(); 
		Delay.msDelay(1500);
	}

	public  void ouvrirPince() {
		Motor.C.setSpeed(SPEED); 
		Motor.C.forward(); 
		Delay.msDelay(1500);
	}

}
