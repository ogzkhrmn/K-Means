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

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class KMeans {

    private List<CustomerInfo> list = new ArrayList<>();
    List<CustomerInfo> randomInfos = new ArrayList<>();

    public List<CustomerInfo> openFile(String path) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(path)); CSVReader csvReader = new CSVReader(reader)) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                CustomerInfo customerInfo =
                    new CustomerInfo(Double.parseDouble(nextRecord[0]), Double.parseDouble(nextRecord[1]), Double.parseDouble(nextRecord[2]),
                        Double.parseDouble(nextRecord[3]), -1);
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
        return list;
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
                    double newDistance = Math.pow(customerInfoSelected.getAge() - clusterInfo.getAge(), 2) +
                        Math.pow(customerInfoSelected.getAnnualIncome() - clusterInfo.getAnnualIncome(), 2) +
                        Math.pow(customerInfoSelected.getGender() - clusterInfo.getGender(), 2) +
                        Math.pow(customerInfoSelected.getSpendingScore() - clusterInfo.getSpendingScore(), 2);
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
        double genderDistance = 0;
        double ageDistance = 0;
        double salaryDistance = 0;
        double spendingDistance = 0;
        // Find total distance between selected cluster and cluster's dataset.
        for (CustomerInfo customerInfo : indexClusterMap.get(i)) {
            genderDistance += customerInfo.getGender();
            ageDistance += customerInfo.getAge();
            salaryDistance += customerInfo.getAnnualIncome();
            spendingDistance += customerInfo.getSpendingScore();
        }
        // set new point of cluster.
        int size = indexClusterMap.get(i).size();
        randomInfos.get(i).setGender(genderDistance / size);
        randomInfos.get(i).setAge(ageDistance / size);
        randomInfos.get(i).setAnnualIncome(salaryDistance / size);
        randomInfos.get(i).setSpendingScore(spendingDistance / size);
    }

    private void calculateMinSquareError(Map<Integer, List<CustomerInfo>> indexClusterMap, int k) {
        double mse = 0;
        // Finds all errors.
        for (int i = 0; i < k; i++) {
            // Find total distance between selected cluster and cluster's dataset and add to mse.
            for (CustomerInfo customerInfo : indexClusterMap.get(i)) {
                mse += Math.pow(customerInfo.getAge() - randomInfos.get(i).getAge(), 2) +
                    Math.pow(customerInfo.getAnnualIncome() - randomInfos.get(i).getAnnualIncome(), 2) +
                    Math.pow(customerInfo.getGender() - randomInfos.get(i).getGender(), 2) +
                    Math.pow(customerInfo.getSpendingScore() - randomInfos.get(i).getSpendingScore(), 2);
            }
        }
        System.out.printf("%.2f \t", mse);
    }
}