import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Robot {

	private Mouvements mouvements;
	private Perception perception;

	private final static float[] ZONE_EN_BUT = {0.1f, 0.1f, 0.1f}; // Référence de couleur pour la zone d'en-but (blanche)
	//float lastDistance=0;

	private int paletAttrape;

	public Robot() {
		mouvements = new Mouvements();
		perception = new Perception();
		paletAttrape=0;
	}

	public void enRoute() {
		premierPalet();
		while(paletAttrape<5) {	
			avancerEtCapterPalet();
			mouvements.stopRobot();
			avancerVersZoneEnBut();
			mouvements.stopRobot();
			check();
		}
	}

	public void premierPalet() {
		mouvements.ouvrirPince();
		mouvements.stopRobot();

		mouvements.avancer(65);
		mouvements.stopRobot();

		mouvements.fermerPince();
		mouvements.stopRobot();

		mouvements.avancer(200);
		/*
		while(perception.distance()<=0.15f) {
			eviterObstacle();
		}
		 */
		mouvements.stopRobot();

		mouvements.ouvrirPince();
		mouvements.stopRobot();
		check();
	}

	public void check() {
		paletAttrape++;
		mouvements.reculer(15); //reculer 10cm (avant de rechercher)
		mouvements.stopRobot();
		if(mouvements.pinceOuverte==true) //vérif pince à fermer
			mouvements.fermerPince();
		mouvements.stopRobot();
	}

	public void avancerEtCapterPalet() {
		int tentativesRecherches=0;
		perception.recherche(220);

		if(!perception.verifierDistance()) {
			mouvements.stopRobot();
			avancerEtCapterPalet();  // Redémarrer la tentative à nouveau
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
			System.out.print(mouvements.compteurDeDegre);
			this.attraperPalet();  // methode pour capturer le palet
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

	public void avancerVersZoneEnBut() {
		mouvements.tournerVersZoneEnBut();
		while (perception.surCouleur(ZONE_EN_BUT)!=true) {
			mouvements.avancer(0.01);
			//eviterObstacle(); 
		}
		mouvements.stopRobot();
		mouvements.ouvrirPince();
	}

	public static void main(String[] args) {
		Robot R = new Robot();
		R.enRoute();
		//R.mouvements.tourner(90);
		//R.avancerVersZoneEnBut();
		//R.avancerVersZoneEnBut();
	}
}
