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

public class Perception extends Mouvements{
	//Capteur ULTRASON
	private EV3UltrasonicSensor ultrasonicSensor;

	//Capteur COULEUR
	private EV3ColorSensor colorSensor;
	private SampleProvider average; 
	private float DistanceMinPalet; 
	private final static int vitessederotation = 50;
	private final static double ERROR = 0.01;

	public Perception() {
		ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		colorSensor = new EV3ColorSensor(SensorPort.S1);
		average = new MeanFilter(colorSensor.getRGBMode(), 1);
		colorSensor.setFloodlight(lejos.robotics.Color.WHITE);
		DistanceMinPalet=0;
	}

	//capte une distance en m
	public float distance() {
		SampleProvider distanceProvider = ultrasonicSensor.getDistanceMode(); 
		float[] sample = new float[distanceProvider.sampleSize()];
		distanceProvider.fetchSample(sample, 0);
		return sample[0];
	}

	//verifie si la distance du palet est la meme que celle quon capte
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

	//recherche le palet le plus proche et tourner vers ce palet
	public float recherche(int compteur) {
		float []distAng = new float[2];

		Motor.B.setSpeed(vitessederotation);  
		Motor.D.setSpeed(vitessederotation); 

		Motor.B.backward(); 
		Motor.D.forward(); 

		int compt = 0; 
		float[] tabdistance = new float[compteur]; // Tableau pour enregistrer les distances captées

		//scanner son environnement et lenregistrer en tournant 360 d
		while (compt < compteur) {
			float distance = distance();
			tabdistance[compt] = distance;
			System.out.println("Angle: " + compt + "° | Distance: " + distance + " m");
			compt++;
			Delay.msDelay(54);
		}

		stopRobot();

		//trouve distance minimale et angle assoscie
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

		DistanceMinPalet = minDist;
		System.out.println("Distance minimale détectée : " + minDist + " m à l'angle " + indexMin + "°");
		float angleMin = (360.0f / 220) * indexMin;
		efficaceTourner(angleMin+5f);
		System.out.print(compteurDeDegre);
		return DistanceMinPalet;
	}

	public float getDistanceMinPalet() {
		return this.DistanceMinPalet;
	}

	/*
	//verifier si la distance captée sur le moment est egale à la distance minimale
	public boolean verifier(float[] distances, int index, int debut, int fin, float seuil) {
	    int taille = distances.length;

	    for (int i = index + debut; i <= index + fin; i++) {
	        if (i < 0 || i >= taille) continue; // si hors limites
	        if (Math.abs(distances[i] - distances[index]) > seuil) {
	            return false; // c'est pas un mur
	        }
	    }

	    return true; // c'est un mur
	}
	 */

	/*
	 //si il y a un mur ne pas prendre en compte
	public boolean estMur(float[] distances, int index, float seuil) {
	    // differents possibiltes
	    if (verifier(distances, index, -10, 0, seuil)) {
	        return true; // Les 10 distances avant sont proches
	    }
	    if (verifier(distances, index, 0, 10, seuil)) {
	        return true; // Les 10 distances après sont proches
	    }
	    if (verifier(distances, index, -5, 5, seuil)) {
	        return true; 
	    }
	    if (verifier(distances, index, -3, 7, seuil)) {
	        return true; 
	    }
	    if (verifier(distances, index, -2, 8, seuil)) {
	        return true; 
	    }
	    if (verifier(distances, index, -8, 2, seuil)) {
	        return true; 
	    }
	    if (verifier(distances, index, -7, 3, seuil)) {
	        return true; 
	    }
	    return false; // Si aucune de ces conditions ne sont valides
	}
	 */

	public void close() {
		ultrasonicSensor.close(); // eteint le capteur
	}

	// Méthode pour détecter si le robot est sur une couleur similaire à la blanche
	public boolean surCouleur(float[] couleurReference) {
		float[] couleurActuelle = new float[average.sampleSize()];
		average.fetchSample(couleurActuelle, 0);

		// Calculer la différence scalaire entre la couleur actuelle et la couleur de référence
		double scalaireDifference = scalaire(couleurActuelle, couleurReference);
		System.out.println("Scalaire: " + scalaireDifference);

		// Retourne vrai si la différence est inférieure à l'erreur acceptée
		return scalaireDifference < ERROR;
	}

	// Fonction scalaire pour comparer deux couleurs (RGB)
	public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt(Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}

	public static void main(String[] args) {
		Perception p = new Perception();
		p.recherche(220);
		p.tournerVersZoneEnBut();
	}

}
