
import javax.management.monitor.Monitor;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Mouvements {

	protected static final int SPEED = 500;
	private int compteurDeDegre;

    public Mouvements() {
        compteurDeDegre=0; //robot dirigé vers la zone en-but
    }


	public  void avancer(float distance) { 
		Motor.B.setSpeed(SPEED);
		Motor.D.setSpeed(SPEED);
		Motor.B.forward();
		Motor.D.forward();
		Delay.msDelay(2000);
	}

	public void stopRobot() {
		Motor.B.stop();
		Motor.D.stop();
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
        compteurDeDegre+=angle;
	}

	public void tournerVersZoneEnBut() {
		tourner(-compteurDeDegre);
		compteurDeDegre=0;
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
	
	public void efficaceTourner(int angle) {
		//si +180 // - 180
		// +90 - 90
		if(angle<180) //-180
			//si sur la droite
			tourner(angle);
		else  {
			//si doit aller a gauche
			int nvAngle=360-angle;
			tourner(nvAngle);
		}
	}
}
