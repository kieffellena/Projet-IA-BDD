import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

/**
 * Une classe qui contient toutes les méthodes de mouvements que le robot a besoin de faire.
 * Ces méthodes sont utilisées dans la classe <code>Perception</code> et <code>Robot</code>.
 */
public class Mouvements {

	/**
	 * Une constante entière pour définir la vitesse du robot.
	 */
	protected static final int SPEED = 500;
	/**
	 * Un flottant pour compter l'angle tourné en degrés, 
	 * par rapport à la zone d'en-but qui est en référence à 0°.
	 */
	public float compteurDeDegre;
	/**
	 * Un booléen qui a pour valeur <b>true</b> lorsque les pinces sont ouvertes.
	 */
	public boolean pinceOuverte;

	/**
	 * Un constructeur sans paramètre qui
	 * initialise le <code>compteurDeDegre</code> à 0 et, 
	 * <code>pinceOuverte</code> à <b>false</b>.
	 */
	public Mouvements() {
		compteurDeDegre=0;
		pinceOuverte=false;
	}

	
	/** 
	 * Une méthode qui permet au robot d'avancer de la distance en paramètres (en centimètres).
	 * @param d la distance
	 */
	public void avancer(double d) { 
		Motor.B.setSpeed(SPEED);
		Motor.D.setSpeed(SPEED);
		Motor.B.forward();
		Motor.D.forward();
		int delay = (int) Math.round(d * 40);
		Delay.msDelay(delay);
	}

	
	/** 
	 * Une méthode qui permet au robot de reculer de la distance donnée en paramètres (en centimètres).
	 * @param d la distance
	 */
	public void reculer(double d) {
		Motor.B.setSpeed(SPEED);
		Motor.D.setSpeed(SPEED);
		Motor.B.backward();
		Motor.D.backward();
		int delay = (int) Math.round(d * 40);
		Delay.msDelay(delay);
	}

	/**
	 * Une méthode qui permet au robot de tourner d'un angle donné en paramètres (en degrès).
	 * @param angle l'angle
	 */
	public void tourner(float angle) {
		Motor.D.setSpeed(SPEED);
		int angleArr=(int) Math.round(4.44*angle);
		Motor.D.rotate(angleArr); 
		Delay.msDelay(2000); 
		compteurDeDegre+=angle;
	}

	/**
	 * Une méthode qui permet au robot de tourner d'un angle donné en paramètres (en degrès), 
	 * de manière efficace, sans tourner d'un angle trop grand inutilement.
	 * @param angle l'angle
	 */
	public void efficaceTourner(float angle) {
		if(angle<180) 
			tourner(angle);
		else  {
			float nvAngle=360-angle;
			tourner(-nvAngle);
		}
	}

	/**
	 * Une méthode qui permet au robot de se tourner dans la direction de la zone d'en-but.
	 */
	public void tournerVersZoneEnBut() {
		stopRobot();
		efficaceTourner(-compteurDeDegre);
	}

	/**
	 * Une méthode qui permet au robot de fermer ses pinces. 
	 * Et met à jour la valeur de <code>pinceOuverte</code> à <b>false</b>.
	 */
	public void fermerPince() {
		Motor.C.setSpeed(500); 
		Motor.C.backward(); 
		pinceOuverte=false;
		Delay.msDelay(3000);
	}

	/**
	 * Une méthode qui permet au robot d'ouvrir ses pinces. 
	 * Et met à jour la valeur de <code>pinceOuverte</code> à <b>true</b>.
	 */
	public void ouvrirPince() {
		Motor.C.setSpeed(500); 
		Motor.C.forward(); 
		pinceOuverte=true;
		Delay.msDelay(3000);
	}

	/**
	 * Une méthode qui permet au robot de stopper tous ses moteurs. 
	 * Moteur B : roue droite, Moteur D : roue gauche et Moteur C : les pinces.
	 */
	public void stopRobot() {
		Motor.B.flt(true);
		Motor.D.flt(true);
		Motor.C.flt(true);
	}
}
