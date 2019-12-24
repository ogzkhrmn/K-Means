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

public class MainClass {

    public static void main(String[] args) throws IOException {
        KMeans kMeans = new KMeans();
        kMeans.openFile(args[0]);
        kMeans.calculateKMeans(2);
        kMeans.calculateKMeans(3);
        kMeans.calculateKMeans(4);
        kMeans.calculateKMeans(5);
        kMeans.calculateKMeans(6);
        kMeans.calculateKMeans(7);
        kMeans.calculateKMeans(8);
        kMeans.calculateKMeans(9);
        kMeans.calculateKMeans(10);
    }

}