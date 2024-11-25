import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Robot {

	private Mouvements mouvements;
	private Perception perception;

	//private final static float[] ZONE_EN_BUT = {1.0f, 1.0f, 1.0f}; // Référence de couleur pour la zone d'en-but (blanche)
	//float lastDistance=0;

	public Robot() {
		mouvements = new Mouvements();
		perception = new Perception();
	}




	public void avancerEtCapterPalet() {
		int tentativesRecherches=0;
		perception.recherche(220);

		if(!verifierDistance()) {
			mouvements.stopRobot();
			//avancerEtCapterPalet();  // Redémarrer la tentative à nouveau
		}

		else {	

			float distanceActuelle = perception.getDistanceMinPalet();  // Distance initiale au palet
			float distanceCible = distanceActuelle;  // Distance cible souhaitée
			boolean paletCapture = false; 

			// condition d'arret si le palet est à moins de 30cm
			while (distanceActuelle > 0.35f && !paletCapture) {
				mouvements.avancer(1);  //approche progressive
				Delay.msDelay(50);  // Délai pour stabiliser la mesure

				distanceActuelle = perception.distance();  // Met à jour la distance actuelle


				// Si le robot capte un objet à une distance inferieur à 10cm, c'est un obstacle ou un autre robot
				if (distanceActuelle <= 0.1f) {
					eviterObstacle();  
					tentativesRecherches ++;  
					perception.recherche(220);  // Nouvelle recherche 
					distanceCible = perception.getDistanceMinPalet();  // Met à jour la cible avec la nouvelle distance

				}


				// Si la distance ne diminue pas, on fais une nouvelle recherche
				else if (distanceActuelle >= distanceCible + 0.30) {
					mouvements.stopRobot(); 
					if(tentativesRecherches<=3) {
						tentativesRecherches++;
						mouvements.avancer(25);  // Avancer un peu pour ajustement
						perception.recherche(220);  // Nouvelle recherche 
						distanceCible = perception.getDistanceMinPalet();  // Met à jour la cible
					} 
					else { //c'est pour eviter d'avoir une boucle infini si le robot ne retrouve pas a chaque fois le palet 
						mouvements.stopRobot(); 
													
					}

				} else {
					distanceCible = distanceActuelle;  // on Met à jour la distance cible à chaque fois si elle diminue
				}
			}
			paletCapture = true;
			mouvements.stopRobot();
			this.attraperPalet();  // methode pour capturer le palet
			//tournerVersZoneEnBut();
		} 

	}

	public void attraperPalet() {
		mouvements.ouvrirPince();
		mouvements.stopRobot();
		mouvements.avancer(30);  // Avance de 25 cm pour capturer le palet

		mouvements.stopRobot();

		mouvements.fermerPince();
		mouvements.stopRobot();
	}



	//si obstacle rencontré, tourne à droite et avance
	public void eviterObstacle() {
		mouvements.stopRobot();
		mouvements.tourner(90);
		mouvements.stopRobot();
		mouvements.avancer(50);
		mouvements.stopRobot();
		mouvements.tourner(-90);
		mouvements.stopRobot();
		mouvements.avancer(50);

	}


	public boolean verifierDistance() {

		float distPerçu = perception.distance();  // Mesure la distance perçu actuelle
		if (perception.getDistanceMinPalet() == Float.MAX_VALUE) {
			System.out.println("Aucun palet détecté.");
			return false;  // Aucun palet détecté
		}
		// on verifie la precision
		if (Math.abs(distPerçu - perception.getDistanceMinPalet()) < 0.5f)  { 
			System.out.println("?????????????????????");
			Delay.msDelay(100);
			return true;  // Distance est la meme ou à peu pres la meme
		} else {
			System.out.println("@@@@@@@@@Aucun palet détecté.");
			Delay.msDelay(100);
			return false;  // la Distance n'est pas la meme
		}
	}

	public void check() {
		if(mouvements.pinceOuverte==true) //vérif pince à fermer
			mouvements.fermerPince();
		mouvements.reculer(0.1); //reculer 10cm (avant de rechercher)
	}

	public void premierPalet() {
		mouvements.avancer(40);
		mouvements.stopRobot();
		mouvements.ouvrirPince();
		mouvements.stopRobot();
		mouvements.avancer(20);
		mouvements.stopRobot();
		mouvements.fermerPince();
		mouvements.stopRobot();
		mouvements.avancer(240);
		//mouvements.eviter();
		mouvements.ouvrirPince();
		mouvements.stopRobot();
	}

	public void enRoute() {
		premierPalet();
		// à revoir
		while (/*5min temps de compet*/) { 
			avancerEtCapterPalet();
			mouvements.stopRobot();
			avancerVersZoneEnBut();
			check();
		}
	}
	
	public static void main(String[] args) {
		// System.out.println("hello");
		Robot R = new Robot();
		R.avancerEtCapterPalet();
		//R.tournerVersZoneEnBut();
		//R.verifierDistance();
		//R.eviterObstacle();
		// R.attraperPalet();
		//  Mouvements M = new Mouvements();
		//  M.avancer(5);
		// M.fermerPince();
		//Perception P = new Perception();
		//P.rechercheEtTourner(220);
		//M.avancer(50);

	}
}
