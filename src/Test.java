import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

// b ces roue droite
//c les pinces
//d roue gauche
// backward fermer pince
//forward ouvrir
// + sens inverse des aiguilles dune montre
//rotate
//setSpeed 500 tres vite 100 pas vite
// 500 speed pour 2000
// pour ouvrir les pinces 3500

//moteur droit pour tourner a gauche
//400 cest pile 90degrees

public class Test extends Touch {
	
	public static void main(String[] args) {
		TouchSensor uTouch = new TouchSensor(SensorPort.S2);
		waitForTouch(uTouch);
		
		Motor.B.setSpeed(500);
		Motor.D.setSpeed(500);
		Motor.B.forward();
		Motor.D.forward();
		
		Delay.msDelay(10000);
		//Motor.C.forward();
		//Delay.msDelay(2500);
		
	}

}
