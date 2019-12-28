/*
 * adesso AG
 * Adessoplatz 1
 * 44269 Dortmund
 * Tel.: +49 (0) 231 7000-7000
 * Fax.: +49 (0) 231 7000-1000
 * Mail: info@adesso.de
 * Web : www.adesso.de
 *
 * Copyright (c) 2019 adesso AG, all rights reserved
 */
package school.kmeans;

import com.opencsv.CSVReader;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class KMeans {

    private List<CustomerInfo> list = new ArrayList<>();
    private List<CustomerInfo> randomInfos = new ArrayList<>();
    private int featureSize;

    public void openFile(String path, int[] features) {
        featureSize = features.length;
        try (Reader reader = Files.newBufferedReader(Paths.get(path)); CSVReader csvReader = new CSVReader(reader)) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                double[] selectedFeatures = new double[features.length];
                for (int i = 0; i < features.length; i++) {
                    selectedFeatures[i] = Double.parseDouble(nextRecord[features[i]]);
                }
                CustomerInfo customerInfo = new CustomerInfo(selectedFeatures, -1);
                list.add(customerInfo);
            }
        } catch (Exception e) {
            System.out.println("Error occured");
        }
        // Initialize clusters. Selects random values.
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int selected = random.nextInt(list.size());
            randomInfos.add(new CustomerInfo(list.get(selected)));
            list.get(selected).setCluster(i);
        }
    }

    public void calculateKMeans(int k) {
        boolean changed = false;
        Map<Integer, List<CustomerInfo>> indexClusterMap = new HashMap<>();
        while (!changed) {
            int cluster = -1;
            changed = false;
            // Add empty list for cluster map
            for (int i = 0; i < k; i++) {
                indexClusterMap.put(i, new ArrayList<>());
            }
            for (CustomerInfo customerInfoSelected : list) {
                double distance = Double.MAX_VALUE;
                for (int j = 0; j < k; j++) {
                    // Gets selected cluster
                    CustomerInfo clusterInfo = randomInfos.get(j);
                    //Calculate euclidean distance
                    double newDistance = 0;
                    for (int i = 0; i < customerInfoSelected.getFeatures().length; i++) {
                        newDistance += Math.pow(customerInfoSelected.getFeatures()[i] - clusterInfo.getFeatures()[i], 2);
                    }
                    // Check if new distance smaller
                    // If smaller change selected cluster and distance
                    // If not do nothing
                    if (newDistance < distance) {
                        distance = newDistance;
                        cluster = j;
                    }
                }
                // If selected cluster changed, set new cluster and changed true
                if (customerInfoSelected.getCluster() != cluster) {
                    changed = true;
                    customerInfoSelected.setCluster(cluster);
                }
                // Add data to selected cluster list.
                indexClusterMap.get(customerInfoSelected.getCluster()).add(customerInfoSelected);
            }
            if (changed) {
                for (int i = 0; i < k; i++) {
                    calculateNewPoint(indexClusterMap, i);
                }
            }
        }
        calculateMinSquareError(indexClusterMap, k);
    }

    private void calculateNewPoint(Map<Integer, List<CustomerInfo>> indexClusterMap, int i) {
        double[] distances = new double[featureSize];
        Arrays.fill(distances, 0);
        // Find total distance between selected cluster and cluster's dataset.
        for (CustomerInfo customerInfo : indexClusterMap.get(i)) {
            for (int j = 0; j < featureSize; j++) {
                distances[j] += customerInfo.getFeatures()[j];
            }
        }
        // set new point of cluster.
        int size = indexClusterMap.get(i).size();
        for (int j = 0; j < featureSize; j++) {
            randomInfos.get(i).getFeatures()[j] = (distances[j] / size);
        }
    }

    private void calculateMinSquareError(Map<Integer, List<CustomerInfo>> indexClusterMap, int k) {
        double mse = 0;
        // Finds all errors.
        for (int i = 0; i < k; i++) {
            // Find total distance between selected cluster and cluster's dataset and add to mse.
            for (CustomerInfo customerInfo : indexClusterMap.get(i)) {
                for (int j = 0; j < featureSize; j++) {
                    mse += Math.pow(customerInfo.getFeatures()[j] - randomInfos.get(i).getFeatures()[j], 2);
                }
            }
        }
        System.out.printf("%.2f \t", mse);
    }
}