import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
public class Mouvements {

	protected static final int SPEED = 500;
	public int compteurDeDegre=0;


	//distance en cm
	//2000 delay pr 0,4cm
	public void avancer(double d) { 
		Motor.B.setSpeed(SPEED);
		Motor.D.setSpeed(SPEED);
		Motor.B.forward();
		Motor.D.forward();
		int delay = (int) Math.round(d * 40);  // 40 ms par cm
		Delay.msDelay(delay);

	}

	public void stopRobot() {
		Motor.B.flt(true);
		Motor.D.flt(true);
	}
	public  void reculer(double d) {
		Motor.B.setSpeed(SPEED);
		Motor.D.setSpeed(SPEED);
		Motor.B.backward();
		Motor.D.backward();
		int delay = (int) Math.round(d * 40);  // 40 ms par cm
		Delay.msDelay(delay);
	}

	public void tourner(float angle) {
		Motor.D.setSpeed(SPEED);
		int angleArr=(int) Math.round(4.44*angle);
		Motor.D.rotate(angleArr); 
		Delay.msDelay(2000); 
		compteurDeDegre+=angle;
	}

	/*public void tournerVersZoneEnBut() {
		tourner(-compteurDeDegre);
		compteurDeDegre=0;
	}*/

	public  void fermerPince() {
		Motor.C.setSpeed(500); 
		Motor.C.backward(); 
		Delay.msDelay(4000);
	}

	public  void ouvrirPince() {
		Motor.C.setSpeed(500); 
		Motor.C.forward(); 
		Delay.msDelay(3000);
	}

	public void efficaceTourner(int angle) {
		compteurDeDegre+=angle;
		//si +180 // - 180
		// +90 - 90
		if(angle<180) //-180
			//si sur la droite
			tourner(angle);
		else  {
			//si doit aller a gauche
			int nvAngle=360-angle;
			tourner(-nvAngle);
		}
	}
	public static void main(String[] args) {
		Mouvements M = new Mouvements();
		//M.tourner(360);
	//	M.ouvrirPince();
	M.fermerPince();
		//M.reculer(30);
	}
}
