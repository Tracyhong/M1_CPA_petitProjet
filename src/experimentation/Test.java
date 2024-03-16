package experimentation;

import java.util.ArrayList;
import java.util.List;
import algorithms.DefaultTeam;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
    private int nb_tests = 5;

    public ArrayList<Point> read(String path) {
        ArrayList<Point> points = new ArrayList<Point>();

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

            try {
                String line;
                while ((line = input.readLine()) != null) {
                    String[] coordinates = line.split("\\s+");
                    points.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
                }
            } finally {
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }

    public static void sauvegarde(String path, List<Long> algoNife, List<Long> algoWelzl) {
        try {
            FileWriter writer = new FileWriter(path);

            for (int i = 0; i < algoNife.size(); i++) {
                writer.write(algoNife.get(i) + " " + algoWelzl.get(i) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de l'écriture dans le fichier : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        DefaultTeam defaultTeam = new DefaultTeam();
        List<Long> algoNifeTime = new ArrayList<Long>();
        List<Long> algoWelzlTime = new ArrayList<Long>();
        List<Double> stdDevNife = new ArrayList<Double>();
        List<Double> stdDevWelzl = new ArrayList<Double>();

        for (int i = 2; i < 1662; i++) {
            System.out.println(" " + i);
            ArrayList<Point> points = test.read("src/experimentation/samples/test-" + i + ".points");
            if (points.size() != 256) {
                throw new IllegalArgumentException("The number of points is not correct");
            }

            long startTime = System.currentTimeMillis();
            for (int j = 0; j < test.nb_tests; j++) {
                defaultTeam.algoNaif(points);
            }
            long endTime = System.currentTimeMillis();
            long time = (endTime - startTime) / test.nb_tests;
            algoNifeTime.add(time);

            double meanNife = (double) algoNifeTime.stream().mapToLong(Long::valueOf).sum() / algoNifeTime.size();
            double varNife = 0;
            if (algoNifeTime.size() > 1) {
                for (long t : algoNifeTime) {
                    varNife += Math.pow(t - meanNife, 2);
                }
                varNife /= (test.nb_tests - 1);
                double stdDevN = Math.sqrt(varNife);
                stdDevNife.add(stdDevN);
            }

            startTime = System.currentTimeMillis();
            for (int j = 0; j < test.nb_tests; j++) {
                defaultTeam.calculCercleMin(points);
            }
            endTime = System.currentTimeMillis();
            time = (endTime - startTime) / test.nb_tests;
            algoWelzlTime.add(time);

            double meanWelzl = (double) algoWelzlTime.stream().mapToLong(Long::valueOf).sum() / algoWelzlTime.size();
            double varWelzl = 0;
            if (algoWelzlTime.size() > 1) {
                for (long t : algoWelzlTime) {
                    varWelzl += Math.pow(t - meanWelzl, 2);
                }
                varWelzl /= (test.nb_tests - 1);
                double stdDevW = Math.sqrt(varWelzl);
                stdDevWelzl.add(stdDevW);
            }
        }

        sauvegarde("src/experimentation/resultats/times", algoNifeTime, algoWelzlTime);
        sauvegarde("src/experimentation/resultats/ecartypes", algoNifeTime, algoWelzlTime);   
       }
}
