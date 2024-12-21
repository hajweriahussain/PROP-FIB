package Domini;

/**
 * Aquesta interfície defineix els mètodes que ha d'implementar qualsevol classe que 
 * generi una solució per disposar productes basant-se en una matriu de similituds.
 * Les classes dels dos algoritmes que implementan aquesta interfície han de proporcionar una manera de generar 
 * un layout (disposició) òptima, obtenir el resultat d'aquesta disposició i calcular la millor similitud.
 */
public interface GeneradorSolucio {

    /**
     * Genera un layout òptim per als productes en funció de la matriu de similituds.
     * 
     * @return La matriu amb la disposició òptima dels productes.
     */
    Integer[][] generarLayout();

    /**
     * Obté el resultat de la disposició generada dels productes.
     * 
     * @return La matriu amb el layout resultant després d'optimitzar la disposició.
     */
    Integer[][] getResultat();

    /**
     * Obté la suma total de totes les similituds dels productes (millor similitud) de la millor disposició trobada.
     * 
     * @return La millor similitud trobada entre productes segons la disposició generada.
     */
    double getMillorSimilitud();
}
