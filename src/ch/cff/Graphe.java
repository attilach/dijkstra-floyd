/**
 * @fichier GrapheCFF.java
 * @titre Réseau CFF
 * @description Class implementing Floyd and Djikstra algorithm for the shortest paths in Graph.
 * @auteurs Kevin Estalella & Federico Lerda
 * @date 21 Mars 2017
 * @version 1.0
 */

package ch.cff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import ch.cff.Ville;
import jdk.nashorn.internal.objects.NativeArray;

import javax.management.Query;

public class Graphe {

    // Store the graph with the cites, the links and the weight of the paths
    public HashMap<Integer, HashMap<Integer, Integer>> edges;

    // Floyd's Matrix
    private int[][] matrixCostFloyd;
    private int[][] matrixPrecFloyd;

    // Djikstra's Matrix
//    private ArrayList<Integer> listCostDij;
//    private ArrayList<Integer> listPrecDij;
    private HashMap<Integer, Integer> listCostDij;
    private HashMap<Integer, Integer> listPrecDij;

    /**
     * Constructor.
     */
    public Graphe() {
        // graph
        this.edges = new HashMap<Integer, HashMap<Integer, Integer>>();
//        this.listCostDij = new ArrayList<Integer>();
//        this.listPrecDij = new ArrayList<Integer>();
        this.listCostDij = new HashMap<Integer, Integer>();
        this.listPrecDij = new HashMap<Integer, Integer>();
    }

    /*************************************
     *
     * GETTERS AND SETTERS
     *
     *************************************
     */
    public int[][] getMatrixCostFloyd() {
        return matrixCostFloyd;
    }

    public void setMatrixCostFloyd(int[][] matrixCostFloyd) {
        this.matrixCostFloyd = matrixCostFloyd;
    }

    public int[][] getMatrixPrecFloyd() {
        return matrixPrecFloyd;
    }

    public void setMatrixPrecFloyd(int[][] matrixPrecFloyd) {
        this.matrixPrecFloyd = matrixPrecFloyd;
    }

    public HashMap<Integer, Integer> getListCostDji() {
        return listCostDij;
    }

    public void setListCostDji(HashMap<Integer, Integer> listCostDji) {
        this.listCostDij = listCostDji;
    }

    public HashMap<Integer, Integer> getListPrecDji() {
        return listPrecDij;
    }

    public void setListPrecDji(HashMap<Integer, Integer> listPrecDji) {
        this.listPrecDij = listPrecDji;
    }
    /*****************************************/


    /**
     * Add a city to the graph
     *
     * @param name  Id of the city to add
     */
    public void addSommet(Integer name) {
        edges.put(name, new HashMap<Integer, Integer>());
    }

    /**
     * Delete a city of the graph
     *
     * @param name  Id of the city to delete
     */
    public void deleteSommet(Integer name) {
        edges.remove(name);
    }

    /**
     * Add a link between two cities
     *
     * @param name1  Id of the first city
     * @param name2	 Id of the second city
     * @param value  Weight of the link
     */
    public void addLiaison(Integer name1, Integer name2, Integer value) {
        edges.get(name1).put(name2, value);
    }

    /**
     * Delete a link between two cities
     *
     * @param name1  Id of the first city
     * @param name2	 Id of the second city
     */
    public void deleteLiaison(Integer name1, Integer name2) {
        edges.get(name1).remove(name2);
    }

    /**
     * Check if a sommet exist
     *
     * @param name  Id of the city
     */
    public boolean existSommet(Integer name) {
        return edges.containsKey(name);
    }

    public Comparator<Integer> dijkstraComp = (o1, o2) -> {

        if (o1 < o2)
            return 1;

        if (o2 < o1)
            return -1;

        return 0;
    };

    /**
     * Use the Dijkstra algorithm to find the best paths in the graph
     *
     * @param size  Number of elements in the graph
     * @param sourcePath	The start point of the path
     */
    public void generatePrecCostDijkstra(int size, int sourcePath) {

        // Initialisation with generation of the list of all ids
        for (Integer idCity : edges.keySet()) {
            listCostDij.put(idCity, Integer.MAX_VALUE);
            listPrecDij.put(idCity, null);
//            listPrecDij.add(idCity, null);
        }


//        listPrecDij.set(sourcePath, 0);
        listPrecDij.put(sourcePath, 0);
        listCostDij.put(sourcePath, 0);

        PriorityQueue<Integer> qpDij = new PriorityQueue<Integer>(size, dijkstraComp);
        qpDij.add(sourcePath);


        while (!qpDij.isEmpty()) {

            int currentPoint = qpDij.poll();

            for (Entry<Integer, Integer> adj : edges.get(currentPoint).entrySet()) {

//                System.out.println("edges:");
//                System.out.println(edges);
//                System.out.println("adj:");
//                System.out.println(adj);
//                System.out.println("adj.getkeys:");
//                System.out.println(adj.getKey());
//                System.out.println("listCostDij.get(adj.getKey()):");
//                System.out.println(this.getCost(currentPoint, adj.getKey()));
//                System.out.println(listCostDij.get(currentPoint));
//
//
//                System.out.println("listCostDij");
//                System.out.println(listCostDij);
//
////
//                System.out.println("partie gauche");
//                System.out.println(listCostDij.get(adj.getKey()));
//
//                System.out.println("partie droite");
//                System.out.println(listCostDij.get(currentPoint) + this.getCost(currentPoint, adj.getKey()));
//
//
//                System.out.println("adj.getkeys:");
//                System.out.println(adj.getKey());

                if (listCostDij.get(adj.getKey()) != null) {

                if (listCostDij.get(adj.getKey()) > listCostDij.get(currentPoint) + this.getCost(currentPoint, adj.getKey()))  {

                    listCostDij.put(adj.getKey(), listCostDij.get(currentPoint) + this.getCost(currentPoint, adj.getKey()));
                    listPrecDij.put(adj.getKey(), currentPoint);

//                        System.out.println("Key");
//                        System.out.println(adj.getKey());
//
//                        System.out.println("CurrentPoint");
//                        System.out.println(currentPoint);


//                    System.out.println("ADD GETKEYS TO qpDIJ");
                        qpDij.add(adj.getKey());

                    }
                }
            }

        }

    }


//    @Override
//    public String toString() {
//        for (Integer key : edges.keySet()) {
//            HashMap<Integer, Integer> edge = edges.get(key);
//
//            System.out.print(key + " : { ");
//
//            for (Integer key2 : edge.keySet()) {
//                Integer value = edge.get(key2);
//
//                System.out.print("(" + key2 + ", " + value + ") ");
//            }
//            System.out.println(" } ");
//        }
//        return "";
//    }






    /**
     * Use the Floyd's algorithm to find the best paths in the graph
     *
     * @param size  Number of elements in the graph
     */
    public void generatePrecCostFloyd(int size) {
        int[][] actualCost = getMatrixWeight(size);
        int[][] actualPrec = new int[size][size];

        // Initialisation of the matrix of precedence with Matrix of Weights
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j || actualCost[i][j] == Integer.MAX_VALUE)
                    actualPrec[i][j] = -1;
                else
                    actualPrec[i][j] = i;
            }
        }

        // Shortest path for all the edges
        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (actualCost[i][k] + actualCost[k][j] < actualCost[i][j]
                            && (actualCost[i][k] < Integer.MAX_VALUE && actualCost[k][j] < Integer.MAX_VALUE)) {
                        actualCost[i][j] = actualCost[i][k] + actualCost[k][j];
                        actualPrec[i][j] = actualPrec[k][j];
                    }
                }
            }
        }

        matrixCostFloyd = actualCost;
        matrixPrecFloyd = actualPrec;

    }

    /**
     * Get the best path between two cities after the Floyd's matrix were generated
     *
     * @param sourcePath  Id of the first city
     * @param destPath 	  Id of the second city
     * @return The list which contains the best path.
     */
    public ArrayList<Integer> getBestPathFloyd(Integer sourcePath,
                                               Integer destPath) {
        ArrayList<Integer> bestPath = new ArrayList<Integer>();

        Integer previousPoint = destPath;
        bestPath.add(previousPoint);

        while (!(previousPoint == sourcePath)) {
            previousPoint = matrixPrecFloyd[sourcePath][previousPoint];
            bestPath.add(previousPoint);
        }

        // As we started by the end, we put now the list in the order source to
        // destination
        Collections.reverse(bestPath);

        return bestPath;
    }

    /**
     * Get the weight of the path between two cities (Floyd)
     *
     * @param sourcePath  Id of the first city
     * @param destPath	  Id of the second city
     * @return The weight
     */
    public Integer getCostFloyd(Integer sourcePath, Integer destPath) {
        return matrixCostFloyd[sourcePath][destPath];
    }

    /**
     * Get the weight of the path between two cities
     *
     * @param sourcePath  Id of the first city
     * @param destPath	  Id of the second city
     * @return The weight
     */
    public Integer getCost(Integer sourcePath, Integer destPath) {
        return edges.get(sourcePath).get(destPath);
    }

    /**
     * Generate the matrix weight for Floyd's Algorithm.
     *
     * @param size  Number of elements in the graph
     * @return The matrix
     */
    public int[][] getMatrixWeight(int size) {
        int[][] matrix = new int[size][size];

        for (Integer key : edges.keySet()) {
            HashMap<Integer, Integer> edge = edges.get(key);
            for (Integer key2 : edge.keySet()) {
                Integer value = edge.get(key2);
                // For graph in both sides
                matrix[key][key2] = value;
                matrix[key2][key] = value;
            }
        }

        // replace all the 0 that we don't want by inf
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] == 0)
                    if (i != j)
                        matrix[i][j] = Integer.MAX_VALUE;
            }
        }

        return matrix;
    }

    /**
     * From a city, try to go to all other cities to see if the graph is connected.
     *
     * @return if it's connected or not
     */
    public boolean isConnected() {

        int i = 0;

        // Choose the first city in the graph
        while (!edges.containsKey(i)) {
            i++;
        }

        int srcCity = i;
        i++;

        // Try to go to every city in the graph
        while (edges.containsKey(i)) {
            if (getBestPathFloyd(srcCity, i).isEmpty())
                return false;
            i++;
        }

        return true;
    }

    @Override
    public String toString() {
        for (Integer key : edges.keySet()) {
            HashMap<Integer, Integer> edge = edges.get(key);

            System.out.print(key + " : { ");

            for (Integer key2 : edge.keySet()) {
                Integer value = edge.get(key2);

                System.out.print("(" + key2 + ", " + value + ") ");
            }
            System.out.println(" } ");
        }
        return "";
    }

}