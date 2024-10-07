import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class deplacement {
	int distance; //en m
	int degrees;
	
	public static void avancer(){
		Motor.B.setSpeed(300);
		Motor.D.setSpeed(300);
		Motor.B.forward();
		Motor.D.forward();
		Delay.msDelay(2000);
	}
	
	public static void reculer() {
		Motor.B.setSpeed(90);
		Motor.D.setSpeed(90);
		Motor.B.backward();
		Motor.D.backward();
		Delay.msDelay(5000);
		
	}
	//avancer
	//stop
	//tourner
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		deplacement.avancer();
		deplacement.avancer(); 
	}

}
