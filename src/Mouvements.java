import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
public class Mouvements {
	//distance en cm
	//Motor D roue gauche
	//Motor B roue droite
	//Motor C les pinces

	protected static final int SPEED = 500;
	public float compteurDeDegre;
	public boolean pinceOuverte;

	public Mouvements() {
		compteurDeDegre=0;
		pinceOuverte=false;
	}

	public void avancer(double d) { 
		Motor.B.setSpeed(SPEED);
		Motor.D.setSpeed(SPEED);
		Motor.B.forward();
		Motor.D.forward();
		int delay = (int) Math.round(d * 40);
		Delay.msDelay(delay);
	}

	public  void reculer(double d) {
		Motor.B.setSpeed(SPEED);
		Motor.D.setSpeed(SPEED);
		Motor.B.backward();
		Motor.D.backward();
		int delay = (int) Math.round(d * 40);
		Delay.msDelay(delay);
	}

	public void tourner(float angle) {
		Motor.D.setSpeed(SPEED);
		int angleArr=(int) Math.round(4.44*angle);
		Motor.D.rotate(angleArr); 
		Delay.msDelay(2000); 
		compteurDeDegre+=angle;
	}

	public void efficaceTourner(float angle) {
		if(angle<180) 
			tourner(angle);
		else  {
			float nvAngle=360-angle;
			tourner(-nvAngle);
		}
	}

	public void tournerVersZoneEnBut() {
		stopRobot();
		efficaceTourner(-compteurDeDegre);
		compteurDeDegre=0;
	}

	public void fermerPince() {
		Motor.C.setSpeed(500); 
		Motor.C.backward(); 
		pinceOuverte=false;
		Delay.msDelay(3000);
	}

	public void ouvrirPince() {
		Motor.C.setSpeed(500); 
		Motor.C.forward(); 
		pinceOuverte=true;
		Delay.msDelay(3000);
	}

	public void stopRobot() {
		Motor.B.flt(true);
		Motor.D.flt(true);
		Motor.C.flt(true);
	}
	
}
