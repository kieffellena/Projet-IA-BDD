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

//b roue droite
//c les pinces
//d roue gauche
//backward fermer pinces
//forward ouvrir pinces
//+ sens inverse des aiguilles d'une montre
//rotate
//setSpeed 500 tres vite 100 pas vite
//500 speed pour 2000
//pour ouvrir les pinces 3500

//moteur droit pour tourner a gauche
//400 cest pile 90degrees

public class Perception extends Mouvements{
	//ULTRASON
	private EV3UltrasonicSensor ultrasonicSensor;
	private final static int vitessederotation = 100;

	public Perception() {
		ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2); // allume sens0r, port 2 ultrason
		compteur=0;
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
		//compteur 110 360 degrees ave une vitesse de 100
		//compteur 50 180 degrees ave une vitesse de 100

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

		Motor.B.stop();
		Motor.D.stop();

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
		System.out.println("Distance minimale détectée : " + minDist + " m à l'angle " + indexMin + "°");

		int anglePalet = Math.round(indexMin);

		//if(anglePalet<180) 
		//si sur la droite
		tourner(anglePalet);
		/*
		    else  {
		    	//si doit aller a gauche
		    	int nvAngle=360-anglePalet;
	    		tourner(nvAngle);
		    }
		 */
	}
	/*
	 else {
		    if(anglePalet<90) {
		    	//tourne a droite
		    	tourner(anglePalet);
		    	System.out.print("AnglePalet "+anglePalet);
		    }
		    else {
		    	//tourne vers gauche
		    int nvAngle=180- anglePalet;
	    	tourner(-nvAngle);
	    	System.out.print("AnglePalet "+anglePalet);
		    }
	 } 
	 }
	 */


	//distance parcouru
	public float distanceParcouru(int duree) {
		//il fait en 2 secondes a 500 , 90 cm 
		return SPEED * (duree); 
	}

	public void close() {
		ultrasonicSensor.close(); // re-eteint
	}

	public static void main(String[] args) {
		//pour tester rceherche dist palet a MUSE
		Perception P = new Perception();
		P.rechercheEtTourner(110);

		//P.calibrerCouleur();
		//P.afficherHistoriqueCouleurs();

		//Delay.msDelay(5000); 
		//P.rechercheMinDist(360); //why not tourner
		//P.close(); 

		/*
            // Si la distance est inférieure ou égale à 0,3 m, faire tourner le robot a droite ?-> cest un autre robit
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
