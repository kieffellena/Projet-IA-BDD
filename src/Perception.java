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

//Motor B roue droite
//Motor C les pinces
//Motor D roue gauche
//+ sens inverse des aiguilles d'une montre
//500 speed pour 2000
//pour ouvrir les pinces 3500
//moteur droit pour tourner a gauche

public class Perception extends Mouvements{
	//ULTRASON
	private EV3UltrasonicSensor ultrasonicSensor;
	//Couleur
	private EV3ColorSensor colorSensor;
	private final static double ERROR = 0.01;
	private SampleProvider average; 
	private final static int vitessederotation = 50;
	private float mindistancePalet; 

	public Perception() {
		ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2); // allume sens0r, port 2 ultrason
		 colorSensor = new EV3ColorSensor(SensorPort.S1);
        	average = new MeanFilter(colorSensor.getRGBMode(), 1);
        	colorSensor.setFloodlight(lejos.robotics.Color.WHITE);
	}

	//PERCEPTION INFRAROUGE
	//trouver Palet le plus proche, y aller. 

	//capte une distance
	public float distance() {
		SampleProvider distanceProvider = ultrasonicSensor.getDistanceMode(); 
		float[] sample = new float[distanceProvider.sampleSize()];
		distanceProvider.fetchSample(sample, 0); // Récupérer l'échantillon
		// Distance en mètres !!!
		return sample[0];
	}

	//recherche le palet le plus proche et tourne vers ce palet.
	public void rechercheEtTourner(int compteur) {
		float []distAng = new float[2];
		//compteur 220 360 degrees ave une vitesse de 50
		//compteur 110 180 degrees ave une vitesse de 50

		Motor.B.setSpeed(vitessederotation);  
		Motor.D.setSpeed(vitessederotation); 

		Motor.B.backward(); 
		Motor.D.forward(); 

		int compt = 0; 
		float[] tabdistance = new float[compteur]; // Tableau pour enregistrer les distances captées

		// scanner son environnement et lenregistrer en tournant 360 d
		while (compt < compteur) {
			float distance = distance();
			tabdistance[compt] = distance;
			System.out.println("Angle: " + compt + "° | Distance: " + distance + " m");
			compt++;
			Delay.msDelay(50);
		}

		stopRobot();

		//trouve dist min et angle
		float minDist = Float.MAX_VALUE; //instancie une valeur ++ pour quil puisse trouver, 
		int indexMin = 0;
		for (int i = 0; i < tabdistance.length; i++) {
			if (tabdistance[i]==Double.POSITIVE_INFINITY || tabdistance[i]<0.3) //exception infini et robot
				continue;
			if (tabdistance[i] < minDist) {  
				minDist = tabdistance[i]; 
				indexMin=i;
			}
		}
		mindistance = minDist;
		System.out.println("Distance minimale détectée : " + minDist + " m à l'angle " + indexMin + "°");
		int anglePalet = Math.round(indexMin);
		efficaceTourner(anglePalet);
	}

	//distance parcouru
	public float distanceParcouru(int duree) {
		//il fait en 2 secondes a 500 , 90 cm 
		return SPEED * (duree); 
	}

	public void close() {
		ultrasonicSensor.close(); // re-eteint
	}

	// Méthode pour détecter si le robot est sur une couleur similaire à celle de référence
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
		//pour tester rceherche dist palet a MUSE
		Perception P = new Perception();
		//P.rechercheEtTourner(220);

		//P.calibrerCouleur();
		//P.afficherHistoriqueCouleurs();

		//Delay.msDelay(5000); 
		//P.rechercheMinDist(360); //why not tourner
		//P.close(); 

		/*
            // Si la distance est inférieure ou égale à 0,3 m, faire tourner le robot a droite ? -> cest un autre robit
            if (distance <= 0.3) {
                M.tourner(90);

            if(distance>0.3) // palet donc fonce dessus

          	else {
                // Continuer d'avancer si pas d'obstacle
               M.avancer();
            }
		 */

	}

}
