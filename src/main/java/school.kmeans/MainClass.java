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

import java.io.IOException;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args) throws IOException {
        System.out.println("Please choose features with comma(1 2 3 4): Example = 1,3,4");
        Scanner scanner = new Scanner(System.in);
        String inputString = scanner.nextLine();
        String[] splited = inputString.trim().split(",");
        int[] selectedFeatures = new int[splited.length];
        for (int i = 0; i < splited.length; i++) {
            selectedFeatures[i] = Integer.parseInt(splited[i]) - 1;
        }
        KMeans kMeans = new KMeans();
        kMeans.openFile(args[0], selectedFeatures);

        double mse1 = kMeans.calculateKMeans(2);
        int selected = 2;
        double difference = 0;
        double mse2;
        for (int i = 3; i <= 10; i++) {
            mse2 = kMeans.calculateKMeans(i);
            if (mse1 - mse2 > difference) {
                difference = mse1 - mse2;
                selected = i;
            }
            mse1 = mse2;
        }
        System.out.println("Best number of cluster = " + selected);
    }

}