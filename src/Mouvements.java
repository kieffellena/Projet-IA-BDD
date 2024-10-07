import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
public class Mouvements {

	    private static final int SPEED = 500;
	    
	    
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

	    public  void tourner(int angle) {
	        Motor.B.rotate(((int) 4.44*angle)); 
	        Motor.D.rotate(((int) 4.44*angle)); 
	        Delay.msDelay(2000); 
	    }
	    
	    public  void fermerPince() {
	        Motor.C.setSpeed(100); 
	        Motor.C.backward(); 
	        Delay.msDelay(2000);
	    }

	    public  void ouvrirPince() {
	        Motor.A.setSpeed(100); 
	        Motor.A.forward(); 
	        Delay.msDelay(2000);
	    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}