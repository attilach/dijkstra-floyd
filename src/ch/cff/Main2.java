/**
 * Class Main2.
 *
 * Entry point of the simulation of CFF.
 *
 * Creates the graph, load the XML file, and print the user menu.
 *
 * @author  Rudolf Hohn
 * @date    March 23 2015
 * @version 1.0
 */

package ch.cff;

import java.io.*;
import java.io.ObjectInputStream.GetField;
import java.util.*;

import ch.cff.GrapheCFF;

public class Main2 {

    public static void main(String[] args) throws IOException {

        GrapheCFF grapheCFF = new GrapheCFF();
        grapheCFF.readXML("villes.xml");

        // permet de prendre les entrees pour le menu
        // soit du clavier, d'un fichier ou de la ligne de commande
        Scanner in;
        switch (args.length) {
            case 0:
                in = new Scanner(System.in);
                break;
            case 1:
                in = new Scanner(new File(args[0]));
                break;
            default:
                String source = args[0];
                for (int i = 1; i < args.length; i++)
                    source += " " + args[i];
                in = new Scanner(source);
        }

        String filePath = System.getProperty("user.dir") + File.separator
                + "villes.xml";
        // lire le fichier villes.xml avec votre code
        System.err.println("Le fichier XML " + filePath + " a ete charge\n");
        int choix = 0;
        do {
            // les impressions du menu sont envoyees sur le canal d'erreur
            // pour les differencier des sorties de l'application
            // lesquelles sont envoyees sur la sortie standard
            System.err.println("Choix  0: quitter");
            System.err.println("Choix  1: liste des villes");
            System.err.println("Choix  2: matrice des poids");
            System.err.println("Choix  3: liste des poids");
            System.err
                    .println("Choix  4: matrice des temps de parcours (Floyd)");
            System.err.println("Choix  5: matrice des precedences (Floyd)");
            System.err
                    .println("Choix  6: temps de parcours entre deux villes (Floyd)");
            System.err.println("Choix  7: parcours entre deux villes (Floyd)");
            System.err
                    .println("Choix  8: tableau des temps de parcours (Dijkstra)");
            System.err.println("Choix  9: tableau des precedences (Dijkstra)");
            System.err
                    .println("Choix 10: temps de parcours entre deux villes (Dijkstra)");
            System.err
                    .println("Choix 11: parcours entre deux villes (Dijkstra)");
            System.err.println("Choix 12: ajout d'une ville");
            System.err.println("Choix 13: ajout d'une liaison");
            System.err.println("Choix 14: suppression d'une ville");
            System.err.println("Choix 15: suppression d'une liaison");
            System.err.println("Choix 16: graphe connexe?");
            System.err.println("Choix 17: sauver (format XML)");

            System.err.println("Entrez votre choix: ");
            choix = in.nextInt();
            String str1, str2, str3;
            switch (choix) {
                case 1:
                    // Liste des villes
                    grapheCFF.getListOfCities();
                    // System.out.println(grapheCFF.getCost(grapheCFF.arraySearchId.get("Geneve"),
                    // grapheCFF.arraySearchId.get("Lausanne")));
                    break;
                case 2:
                    // Matrices des poids
                    int[][] matrix = grapheCFF
                            .getMatrixWeight(grapheCFF.arrayCitiesId.size());
                    grapheCFF.printMatrix(matrix);

                    break;
                case 3:
                    // Listes des poids
                    grapheCFF.toString();
                    break;
                case 4:
                    // Matrice des temps de parcours (Floyd)
                    grapheCFF.generatePrecCostFloyd(grapheCFF.arrayCitiesId.size());
                    grapheCFF.printMatrix(grapheCFF.getMatrixCostFloyd());
                    break;
                case 5:
                    // Matrice des precedences (Floyd)
                    grapheCFF.generatePrecCostFloyd(grapheCFF.arrayCitiesId.size());
                    grapheCFF.printMatrix(grapheCFF.getMatrixPrecFloyd());
                    break;
                case 6:
                    // Temps de parcours entre deux villes (Floyd)
                    System.err.println("Ville d'origine:");
                    str1 = in.next();
                    System.err.println("Ville de destination:");
                    str2 = in.next();
                    System.err.print("Distance: ");

                    grapheCFF.generatePrecCostFloyd(grapheCFF.arrayCitiesId.size());
                    System.out.println(grapheCFF.getCostFloyd(
                            grapheCFF.arraySearchId.get(str1),
                            grapheCFF.arraySearchId.get(str2)));
                    break;
                case 7:
                    // Parcours entre deux villes (Floyd)
                    System.err.println("Ville d'origine:");
                    str1 = in.next();
                    System.err.println("Ville de destination:");
                    str2 = in.next();
                    System.err.print("Parcours: ");

                    grapheCFF.generatePrecCostFloyd(grapheCFF.arrayCitiesId.size());
                    System.out.println(grapheCFF.getBestPathFloydCity(
                            grapheCFF.arraySearchId.get(str1),
                            grapheCFF.arraySearchId.get(str2)));
                    break;

                /**
                 * TODO : Mettre en place l'algorithme de Djikstra
                 **********************************************************************************************
                 *
                 * L'algorithme est en place mais il n'est pas operationnel pour le menu.
                 *
                 */
                case 8:
                    System.err.println("Ville d'origine:");
                    str1 = in.next();
                    // format de sortie -> a generer avec votre code
                    System.out
                            .println("[Geneve:0] [Lausanne:34] [Neuchatel:74] [Delemont:123] [Bale:157] [Berne:101] $[Lucerne:184] [Zurich:180] [Schaffouse:222] [St.-Gall:246] [Coire:271] [St.-Moritz:387] [Bellinzone:316] [Andermatt:263] [Sion:101]"); // resultat
                    // pour
                    // Geneve
                    break;
                case 9:
                    System.err.println("Ville d'origine:");
                    str1 = in.next();
                    // format de sortie -> a generer avec votre code
                    System.out
                            .println("[Geneve<-Lausanne] [Lausanne<-Neuchatel] [Neuchatel<-Delemont] [Delemont<-Bale] [Lausanne<-Berne] [Berne<-Lucerne] [Berne<-Zurich] [Zurich<-Schaffouse] [Zurich<-St.-Gall] [Zurich<-Coire] [Coire<-St.-Moritz] [Lucerne<-Bellinzone] [Sion<-Andermatt] [Lausanne<-Sion]"); // resultat
                    // pour
                    // Geneve
                    break;
                case 10:
                    System.err.println("Ville d'origine:");
                    str1 = in.next();
                    System.err.println("Ville de destination:");
                    str2 = in.next();
                    System.err.print("Distance: ");
                    // format de sortie -> a generer avec votre code
                    // imprimer "inf" a la place Integer.MAX_VALUE
                    System.out.println(267); // resultat pour Bale a St.-Moritz
                    break;
                case 11:
                    System.err.println("Ville d'origine:");
                    str1 = in.next();
                    System.err.println("Ville de destination:");
                    str2 = in.next();
                    System.err.print("Parcours: ");
                    // format de sortie -> a generer avec votre code
                    System.out.println("[Bale:Zurich:Coire:St.-Moritz]"); // resultat
                    // pour
                    // Bale
                    // e
                    // St.-Moritz
                    break;
                /********************************************************************************************
                 */

                case 12:
                    // Ajout d'une ville
                    System.err.println("Nom de la ville:");
                    str1 = in.next();
                    grapheCFF.addCity(str1);
                    break;
                case 13:
                    // Ajout d'une liaison
                    System.err.println("Ville d'origine:");
                    str1 = in.next();
                    System.err.println("Ville de destination:");
                    str2 = in.next();
                    System.err.println("Temps de parcours:");
                    str3 = in.next();
                    grapheCFF.addCitiesLiaison(str1, str2, str3);
                    break;
                case 14:
                    // Suppression d'une ville
                    System.err.println("Nom de la ville:");
                    str1 = in.next();
                    grapheCFF.deleteCity(str1);
                    break;
                case 15:
                    // Suppression d'une liaison
                    System.err.println("Ville d'origine:");
                    str1 = in.next();
                    System.err.println("Ville de destination:");
                    str2 = in.next();
                    grapheCFF.deleteCitesLiaison(str1, str2);
                    break;
                case 16:
                    // Graphe connexe ? Si on supprime une ville, il faut en rajouter une apres, pour ne pas qu'il y ait de trou dans les index
                    grapheCFF.generatePrecCostFloyd(grapheCFF.arrayCitiesId.size());
                    try {
                        System.out.println(grapheCFF.isConnected()); // reponse true
                        // ou false
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println(false);
                    }
                    break;
                case 17:
                    // Sauver au format XML
                    System.err.println("Nom du fichier XML:");
                    str1 = in.next();
                    grapheCFF.writeXML(str1);
                    break;
            }
        } while (choix != 0);

    }

}