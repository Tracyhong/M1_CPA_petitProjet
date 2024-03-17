package tests;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

import algorithms.DefaultTeam;
import supportGUI.Circle;
import supportGUI.Variables;

public class Test {

    //methode pour calculer le temps d'execution de l'algorithme naif
    public static long tempsNaif(ArrayList<Point> points) {
        // long start = System.nanoTime();
        long start = System.currentTimeMillis();
        Circle cercle = DefaultTeam.algoNaif(points);
        // long end = System.nanoTime();
        long end = System.currentTimeMillis();
        return end - start;
    }

    //methode pour calculer le temps d'execution de l'algorithme de Welzl
    public static long tempsWelzl(ArrayList<Point> points) {
        // long start = System.nanoTime();
        long start = System.currentTimeMillis();
        Circle cercle = DefaultTeam.algoWelzl(points);
        // long end = System.nanoTime();
        long end = System.currentTimeMillis();
        return end - start;
    }
    //methode pour lire un fichier de points
    public static ArrayList<Point> lireFichier(String filename) {
        System.out.println("Lecture du fichier " + filename);
        ArrayList<Point> points = new ArrayList();

        try {
           BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
  
           try {
              String line;
              while((line = input.readLine()) != null) {
                 String[] coordinates = line.split("\\s+");  // split the line by spaces
                 points.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
              }
  
           } catch (IOException var16) {
              System.err.println("Exception: interrupted I/O.");
           } finally {
              try {
                 input.close();
              } catch (IOException var14) {
                 System.err.println("I/O exception: unable to close " + filename);
              }
  
           }
        } catch (FileNotFoundException var18) {
           System.err.println("Input file not found.");
        }
        return points;
    }
    public static void compareSimpleDataResult(String path, String pathDataFile) throws IOException{
        DefaultTeam defaultTeam = new DefaultTeam();
        //fichier ecriture
        File dataFile = new File(pathDataFile);
        FileWriter myWriter;
        // if (dataFile.createNewFile()) {
        System.out.println("File created: " + dataFile.getName());
        myWriter = new FileWriter(pathDataFile);
        //write column names
        myWriter.write("filename, naiveRadius, welzlCircle\n");
        
        //fichier de test dans sample
        //recuperer les fichiers de test
        File folder = new File(path);
        if(!folder.exists()){
            System.out.println("Folder not found");
            return;
        }
        ArrayList<String> files = new ArrayList<String>();
        
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory() || fileEntry.getName().equals("test-1.points")) {
                continue;
            } else {
                // System.out.println(fileEntry.getName());
                files.add(fileEntry.getName());
            }
        }
        //boucler sur les fichiers de test
        for (String file : files) {
            //lire le fichier de points
            ArrayList<Point> points = lireFichier(path + file);
            
            ArrayList<Long> naiveRadiusList = new ArrayList<>();
            ArrayList<Long> welzlRadiusList = new ArrayList<>();

            for(int i = 0; i < 5; i++){ //boucler sur 5 pour avoir 5 echantillons puis faire la moyenne
                System.out.println("Echantillon " + (i+1));

                //caluler diametre
                Circle naiveCircle = defaultTeam.algoNaif(points);
                Circle welzlCircle = defaultTeam.algoWelzl(points);
                if(naiveCircle == null || welzlCircle == null){
                    continue;
                }
                naiveRadiusList.add((long)naiveCircle.getRadius());
                welzlRadiusList.add((long)welzlCircle.getRadius());

            }
            // ecrire dans un fichier de sortie les resultats de l'algo naif puis l'algo welzl
            // format : nomFichier, tempsNaif, tempsWelzl
            double naiveRadius = naiveRadiusList.stream().mapToLong(Long::longValue).average().orElse(0.0);
            double welzlRadius = welzlRadiusList.stream().mapToLong(Long::longValue).average().orElse(0.0);
            try {
            myWriter.write(file + ", " + naiveRadius + ", " + welzlRadius + "\n");
            System.out.println(file + ", " + naiveRadius + ", " + welzlRadius );
            } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            }
        }
        myWriter.close();
        //ecrire dans un fichier de sortie les resultats de l'algo naif puis l'algo welzl 
        //format : nomFichier, tempsNaif, tempsWelzl

    }
   
        //methode pour comparer simplement les deux algorithmes sur les fichiers de test 
    public static void compareSimpleData(String path, String pathDataFile) throws IOException{
        
        //fichier ecriture
        File dataFile = new File(pathDataFile);
        FileWriter myWriter;
        // if (dataFile.createNewFile()) {
        System.out.println("File created: " + dataFile.getName());
        myWriter = new FileWriter(pathDataFile);
        //write column names
        myWriter.write("filename, tempsNaif, tempsWelzl\n");
        
        //fichier de test dans sample
        //recuperer les fichiers de test
        File folder = new File(path);
        if(!folder.exists()){
            System.out.println("Folder not found");
            return;
        }
        ArrayList<String> files = new ArrayList<String>();
        
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory() || fileEntry.getName().equals("test-1.points")) {
                continue;
            } else {
                // System.out.println(fileEntry.getName());
                files.add(fileEntry.getName());
            }
        }
        //boucler sur les fichiers de test
        for (String file : files) {
            //lire le fichier de points
            ArrayList<Point> points = lireFichier(path + file);
            
            ArrayList<Long> naiveExecTime = new ArrayList<>();
            ArrayList<Long> welzlExecTime = new ArrayList<>();

            for(int i = 0; i < 5; i++){ //boucler sur 5 pour avoir 5 echantillons puis faire la moyenne
                System.out.println("Echantillon " + (i+1));

                //calculer le temps d'execution de l'algo naif
                long tempsNaif = tempsNaif(points);
                naiveExecTime.add(tempsNaif);
                //calculer le temps d'execution de l'algo de Welzl
                long tempsWelzl = tempsWelzl(points);
                welzlExecTime.add(tempsWelzl);
            }
            // ecrire dans un fichier de sortie les resultats de l'algo naif puis l'algo welzl
            // format : nomFichier, tempsNaif, tempsWelzl
            double tempsNaif = naiveExecTime.stream().mapToLong(Long::longValue).average().orElse(0.0);
            double tempsWelzl = welzlExecTime.stream().mapToLong(Long::longValue).average().orElse(0.0);
            try {
            myWriter.write(file + ", " + tempsNaif + ", " + tempsWelzl + "\n");
            System.out.println(file + ", " + tempsNaif + ", " + tempsWelzl );
            } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            }
        }
        myWriter.close();
        //ecrire dans un fichier de sortie les resultats de l'algo naif puis l'algo welzl 
        //format : nomFichier, tempsNaif, tempsWelzl

    }
   
    //methode pour combiner des fichiers de test pour avoir des resultats plus significatifs
    //combiner des fichiers pour avoir plus de points
    // sur les 1664 fichiers de tests, il y a dans chaque fichier 256 points
    // on peut commencer par combiner 2 fichiers puis 4 puis 8 puis 16 puis 32 puis 64 puis 128 puis 256 

    public static void compareCombiningData(String path, String pathDataFile, int NB_MAX_FILE) throws IOException{//fichier ecriture
        File dataFile = new File(pathDataFile);
        FileWriter myWriter;
        // if (dataFile.createNewFile()) {
        System.out.println("File created: " + dataFile.getName());
        myWriter = new FileWriter(pathDataFile);
        //write column names
        myWriter.write("tempsNaif, tempsWelzl\n");

        //recuperer les fichiers de test
        File folder = new File(path);
        if(!folder.exists()){
            System.out.println("Folder not found");
            return;
        }
        ArrayList<String> files = new ArrayList<String>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory() || fileEntry.getName().equals("test-1.points")) {
                continue;
            } else {
                // System.out.println(fileEntry.getName());
                files.add(fileEntry.getName());
            }
        }
        int min = 2;
        int max = files.size()-1;
        //boucler i = 2 jusqua NB_MAX_FILE pour combiner i fichiers
        for (int i = 2; i < NB_MAX_FILE; i++) {
            System.out.println("Combining " + i + " files");
            //boucler sur 5 pour avoir 5 echantillons puis faire la moyenne 
            ArrayList<Long> naiveExecTime = new ArrayList<>();
            ArrayList<Long> welzlExecTime = new ArrayList<>();
            for(int j = 0; j < 5; j++){
                System.out.println("Echantillon " + (j+1));
                ArrayList<Point> points = new ArrayList<>();
                for(int k = 0; k < i; k++){
                    //lire le fichier de points
                    int indexFile = (int) (Math.random() * (max - min + 1)) + min ; //scale to the range of files
                    ArrayList<Point> nextPoints = lireFichier(path + files.get(indexFile));
                    points.addAll(nextPoints);
                }
                //calculer le temps d'execution de l'algo naif et stocker le temps dans un tableau puis faire la moyenne
                long tempsNaif = tempsNaif(points);
                naiveExecTime.add(tempsNaif);
                System.out.println("Temps Naif: " + tempsNaif);

                //calculer le temps d'execution de l'algo de Welzl et stocker le temps dans un tableau puis faire la moyenne
                long tempsWelzl = tempsWelzl(points);
                welzlExecTime.add(tempsWelzl);
                System.out.println("Temps Welzl: " + tempsWelzl);
            }
            // ecrire dans un fichier de sortie les resultats de l'algo naif puis l'algo welzl
            // format : nomFichier, tempsNaif, tempsWelzl
            double tempsNaif = naiveExecTime.stream().mapToLong(Long::longValue).average().orElse(0.0);
            double tempsWelzl = welzlExecTime.stream().mapToLong(Long::longValue).average().orElse(0.0);
            try {
                myWriter.write(tempsNaif + ", " + tempsWelzl + "\n");
                System.out.println(tempsNaif + ", " + tempsWelzl );
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        
        }
        myWriter.close();
    }
    

    public static void main(String[] args) throws IOException {
        String path = "./samples/";
        String pathDataFileSimpleResult = "./src/tests/results/dataFile_256points_results_1.txt";
        String pathDataFileSimple = "./src/tests/results/dataFile_256points.txt";
        String pathDataFileCombining = "./src/tests/results/dataFile_combiningPoints.txt";

        int NB_MAX_FILE = 20;
        compareSimpleDataResult(path,pathDataFileSimpleResult);
        // compareSimpleData(path,pathDataFileSimple);
        // compareCombiningData(path, pathDataFileCombining,NB_MAX_FILE);  //20 fichiers de test pour combiner mais c'est très long donc à reduire à 5 ou 10 si besoin 

    }
}
