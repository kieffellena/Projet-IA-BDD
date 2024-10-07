import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.BrickFinder;
//b ces roue droite
//c les pinces
//d roue gauche
//backward fermer pince
//forward ouvrir
//+ sens inverse des aiguilles dune montre
//rotate
//setSpeed 500 tres vite 100 pas vite
//500 speed pour 2000
//pour ouvrir les pinces 3500

//moteur droit pour tourner a gauche
//400 cest pile 90degrees
public class Perception extends Mouvements{

	public float distance() {
		EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2); //port 2 ultrason
	    SampleProvider distanceProvider = ultrasonicSensor.getDistanceMode(); 
		float[] sample = new float[distanceProvider.sampleSize()];
		distanceProvider.fetchSample(sample, 0); // Récupérer l'échantillon
        float distance = sample[0]; // Distance en mètres
        return distance;
	}
	
	public float[] rechercheDistPalet() {
		float[] tabdistance = new float[10]; 
		float[] tabAngles = {0,36,72,108,144,180,216,252,288,324,360}; //angles assoscies
		float[] DistAngl = new float[2]; //la distance la plus minimale et langle assoscie
		// faire en meme temps
		//tourner sur lui meme;
		tourner(360);
		// scanner son environnement et lenregistrer tous les 36 degres.
		for (int i=0;i<10;i++) {
			tabdistance[i]=distance();
		}      
		//return la distance la plus petite
		float minDist=0;  
		int index=0;
		for (int k=0; k<tabdistance.length;k++) {
				if (tabdistance[k]==Double.POSITIVE_INFINITY)  		//exception infini
					continue;
				if (tabdistance[k]<0.3) //alors robot on ne prend pas en compte
					continue;
		        if (tabdistance[k] < minDist)                                     
		            minDist = tabdistance[k]; 
		        	index=k;
		}
		DistAngl[0]=minDist;
		DistAngl[1]=tabAngles[index];
		return DistAngl; //renvoie minDist = 0 si il a toruve aucun palet. 
	}
	
    public static void main(String[] args) {
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
