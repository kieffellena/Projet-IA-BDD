import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

/**
 * Une classe qui contient les méthodes pour que le robot perçoive son environnement et,
 * agisse en conséquence. Par exemple avec les palets, les obstacles (robot adverse) et les murs.
 */
public class Perception extends Mouvements{

	/**
	 * Un capteur ultrason.
	 */
	private EV3UltrasonicSensor ultrasonicSensor;

	/**
	 * Un capteur infrarouge pour percevoir les couleurs des lignes des terrains.
	 */
	private EV3ColorSensor colorSensor;
	/**
	 * Un attribut sous format RGB pour les couleurs captés.
	 */
	private SampleProvider average; 
	/**
	 * Un flottant qui prend en valeur la distance du palet le plus proche.
	 */
	private float distanceMinPalet; 
	/**
	 * Une constante entière qui a pour valeur la vitesse de rotation du robot 
	 * pour la méthode <code>recherche</code>.
	 */
	private final static int VITESSE_DE_ROTATION = 50;
	/**
	 * Une constante flottante qui a pour valeur la marge d'erreur acceptée des valeurs RGB de la couleur perçue.
	 */
	private final static double ERROR = 0.01;

	/**
	 * Un constructeur qui indente les capteurs ultrason et infrarouge, 
	 * l'attribut RGB et qui initialise la distance du palet le plus proche à 0.
	 */
	public Perception() {
		ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		colorSensor = new EV3ColorSensor(SensorPort.S1);
		average = new MeanFilter(colorSensor.getRGBMode(), 1);
		colorSensor.setFloodlight(lejos.robotics.Color.WHITE);
		distanceMinPalet=0;
	}

	
	/** 
	 * Une méthode qui retourne la distance de l'objet perçu par le capteur ultrason.
	 * @return un flottant de la distance en mètres
	 */
	public float distance() {
		SampleProvider distanceProvider = ultrasonicSensor.getDistanceMode(); 
		float[] sample = new float[distanceProvider.sampleSize()];
		distanceProvider.fetchSample(sample, 0);
		return sample[0];
	}
	
	/** 
	 * Une méthode qui vérifie si la distance du palet le plus proche
	 * est la même que la distance actuelle perçue par le capteur ultrason.
	 * @return retourne <b>true</b> si la distance est à peu près la même et <b>false</b> sinon
	 */
	public boolean verifierDistance() {
		float distPerçu = distance();  // Mesure la distance perçu actuelle
		if (getDistanceMinPalet() == Float.MAX_VALUE) {
			return false;  // Aucun palet détecté
		}
		// on verifie la precision
		if (Math.abs(distPerçu - getDistanceMinPalet()) < 0.5f)  { 
			Delay.msDelay(100);
			return true;  // Distance est la meme ou à peu pres la meme
		} else {
			Delay.msDelay(100);
			return false;  // la Distance n'est pas la meme
		}
	}

	/**
	 * Une méthode qui cherche la distance du palet le plus proche en tournant sur lui-même de 360° 
	 * et, qui tourne le robot vers ce palet. 
	 * La distance du palet le plus proche est stockée dans l'attribut <code>distanceMinPalet</code>.
	 * @param compteur le nombre entier de distances à percevoir par le capteur ultrason
	 * @return un flottant de la distance du palet le plus proche
	 */
	public float recherche(int compteur) {

		Motor.B.setSpeed(VITESSE_DE_ROTATION);  
		Motor.D.setSpeed(VITESSE_DE_ROTATION); 

		Motor.B.backward(); 
		Motor.D.forward(); 

		int compt = 0; 
		float[] tabdistance = new float[compteur]; // Tableau pour enregistrer les distances captées

		//scanner son environnement et l'enregistrer en tournant 360°
		while (compt < compteur) {
			float distance = distance();
			tabdistance[compt] = distance;
			System.out.println("Angle: " + compt + "° | Distance: " + distance + " m");
			compt++;
			Delay.msDelay(54);
		}

		stopRobot();

		//trouve distance minimale et angle associe
		float minDist = Float.MAX_VALUE; //instancie une valeur tres grande
		int indexMin = 0;
		for (int i = 0; i < tabdistance.length; i++) {
			if (tabdistance[i]==Double.POSITIVE_INFINITY || tabdistance[i]<0.05) //exception infini et robot
				continue;
			if (tabdistance[i] < minDist) {  
				minDist = tabdistance[i]; 
				indexMin=i;
			}
		}

		distanceMinPalet = minDist;
		System.out.println("Distance minimale détectée : " + minDist + " m à l'angle " + indexMin + "°");
		float angleMin = (360.0f / 220) * indexMin;
		efficaceTourner(angleMin+5f);
		return distanceMinPalet;
	}

	/**
	 * Une méthode qui retourne la valeur de l'attribut <code>distanceMinPalet</code>.
	 * @return la valeur flottante de l'attribut 
	 */
	public float getDistanceMinPalet() {
		return this.distanceMinPalet;
	}

	/**
	 * Une méthode qui éteint le capteur ultrason.
	 */
	public void close() {
		ultrasonicSensor.close(); // eteint le capteur
	}

	/**
	 * Une méthode qui détecte si le capteur infrarouge perçoit la couleur en paramètre 
	 * selon une marge d'erreur pour les valeurs RGB.
	 * @param couleurReference les valeurs RGB de la couleur
	 * @return retourne <b>true</b> si le robot est sur cette couleur et <b>false</b> sinon
	 */
	public boolean surCouleur(float[] couleurReference) {
		float[] couleurActuelle = new float[average.sampleSize()];
		average.fetchSample(couleurActuelle, 0);

		// Calculer la différence scalaire entre la couleur actuelle et la couleur de référence
		double scalaireDifference = scalaire(couleurActuelle, couleurReference);

		// Retourne vrai si la différence est inférieure à l'erreur acceptée
		return scalaireDifference < ERROR;
	}

	/**
	 * Une méthode qui calcule la différence deux couleurs selon leurs valeurs RGB.
	 * @param v1 les valeurs RGB de la couleur 1
	 * @param v2 les valeurs RGB de la couleur 2
	 * @return la valeur flottante de la différence des valeurs RGB des deux couleurs
	 */
	public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt(Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}
}
