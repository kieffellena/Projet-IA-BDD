import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;
 
/**
 * Une classe qui contient les méthodes pour que le robot interagisse avec son environnement et,
 * mette en exécution la stratégie pour attraper le plus de palets.
 */
public class Robot {
	
	/**
	 * Un attribut de type <code>Mouvements</code>
	 */
	private Mouvements mouvements;
	/**
	 * Un attribut de type <code>Perception</code>
	 */
	private Perception perception;
	/**
	 * Un tableau constant des valeurs flottantes qui référencie le code RGB de la couleur blanche
	 */
	private final static float[] ZONE_EN_BUT = {0.1f, 0.1f, 0.1f};
	/**
	 * Un entier qui a pour valeur le nombre de palet(s) attrapé(s)
	 */
	private int paletAttrape;

	/**
	 * Un constructeur qui instancie les attributs <code>mouvements</code> et <code>perception</code> et, 
	 * qui initialise le nombre de palet attrapé à 0.
	 */
	public Robot() {
		mouvements = new Mouvements();
		perception = new Perception();
		paletAttrape=0;
	}
	

	/**
	 * Une méthode qui met en route le robot pour qu'il élabore la stratégie 
	 * en lançant des méthodes des classes <code>Robot</code> et <code>Mouvements</code>.
	 */
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

	/**
	 * Une méthode qui permet au robot d'aller chercher le premier palet et, 
	 * de le déposer dans la zone d'en-but en utilisant <code>avancerVersZoneEnBut()</code> puis, 
	 * de réinitialiser son état (pinces et position).
	 */
	public void premierPalet() {
		mouvements.ouvrirPince();
		mouvements.stopRobot();

		mouvements.avancer(65);
		mouvements.stopRobot();

		mouvements.fermerPince();
		mouvements.stopRobot();
		
		mouvements.tourner(-50);
		mouvements.stopRobot();
		mouvements.avancer(30);
		mouvements.stopRobot();
		mouvements.tourner(50);
		mouvements.stopRobot();
		mouvements.avancer(180);
		mouvements.stopRobot();

		mouvements.ouvrirPince();
		mouvements.stopRobot();
		check();
	}

	/**
	 * Une méthode qui réinitialise l'état du robot lorsqu'il a déposé un palet dans la zone d'en-but. 
	 * Il recule de 15 cm et vérifie que les pinces sont fermées et le fait si besoin.
	 */
	public void check() {
		paletAttrape++;
		mouvements.reculer(15); //reculer 10cm (avant de rechercher)
		mouvements.stopRobot();
		if(mouvements.pinceOuverte==true) //vérif pince à fermer
			mouvements.fermerPince();
		mouvements.stopRobot();
	}

	/**
	 * Une méthode qui permet au robot de chercher un palet et de s'adapter selon les situations; 
	 * d'éviter tous les obstacles, de réitérer sa recherche s'il ne trouve pas de palet, 
	 * d'aller attraper le palet lorsqu'il l'a trouvé. 
	 */
	public void avancerEtCapterPalet() {
		int tentativesRecherches=0;
		perception.recherche(107);

		if(!perception.verifierDistance()) {
			mouvements.stopRobot();
			avancerEtCapterPalet();  // Redémarrer la tentative à nouveau
		}

		else {	

			float distanceActuelle = perception.getDistanceMinPalet();  // Distance initiale au palet
			float distanceCible = distanceActuelle;  // Distance cible souhaitée
			boolean paletCapture = false; 

			// condition d'arret si le palet est à moins de 30cm
			while (distanceActuelle > 0.33f && !paletCapture) {
				mouvements.avancer(1);  //approche progressive
				Delay.msDelay(50);  // Délai pour stabiliser la mesure
				distanceActuelle = perception.distance();  // Met à jour la distance actuelle
/*
				// Si le robot capte un objet à une distance inferieur à 10cm, c'est un obstacle ou un autre robot
				if (distanceActuelle <= 0.25f || perception.surCouleur(ZONE_EN_BUT)==true) {
					eviterObstacle();  
					tentativesRecherches ++;  
					perception.recherche(220);  // Nouvelle recherche 
					distanceCible = perception.getDistanceMinPalet();  // Met à jour la cible avec la nouvelle distance
				}
				*/
				if (perception.surCouleur(ZONE_EN_BUT)==true) {
					mouvements.tourner(90);
					perception.recherche(53);
				}
				// Si la distance ne diminue pas, on fais une nouvelle recherche
				if (distanceActuelle >= distanceCible + 0.33) {
					mouvements.stopRobot(); 
					if(tentativesRecherches<=3) {
						tentativesRecherches++;
						mouvements.avancer(25);  // Avancer un peu pour ajustement
						perception.recherche(107);  // Nouvelle recherche 
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

	/**
	 * Une méthode qui permet au robot d'attraper le palet 
	 * en utilisant des méthodes de <code>Mouvements</code>.
	 */
	public void attraperPalet() {
		mouvements.ouvrirPince();
		mouvements.stopRobot();
		mouvements.avancer(30);  // Avance de 25 cm pour capturer le palet
		mouvements.stopRobot();
		mouvements.fermerPince();
		mouvements.stopRobot();
	}

	/**
	 * Une méthode qui permet au robot d'éviter les obstacles lorsqu'il en rencontre. 
	 * Il tourne à droite, avance et se retourne à gauche puis avance à nouveau.
	 */
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

	/**
	 * Une méthode qui permet au robot de se tourner dans la direction de la zone d'en-but 
	 * en utilisant <code>tournerVersZoneEnBut()</code> dans <code>Mouvements</code> puis, 
	 * d'avancer jusqu'à la zone d'en-but (lorsqu'il est sur la bande blanche) en utilisant 
	 * <code>surCouleur(valeurs RGB couleur blanche)</code> dans <code>Perception</code> et 
	 * de déposer le palet tout en évitant les obstacles.
	 */
	public void avancerVersZoneEnBut() {
		mouvements.tournerVersZoneEnBut();
		float distanceActuelle = perception.distance();  // Distance initiale objet
		while (perception.surCouleur(ZONE_EN_BUT)!=true || distanceActuelle <=0.2f) {
			mouvements.avancer(1);
			Delay.msDelay(50);  // Délai pour stabiliser la mesure
			// Si le robot capte un objet à une distance inferieur à 10cm, c'est un obstacle ou un autre robot
			if (distanceActuelle <= 0.1f) {
				eviterObstacle();
				mouvements.tournerVersZoneEnBut();
			}
			distanceActuelle = perception.distance();  // Met à jour la distance actuelle
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
