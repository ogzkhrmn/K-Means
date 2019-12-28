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

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerInfo {

    private double[] features;
    private int cluster;

    public CustomerInfo(CustomerInfo customerInfo) {
        int size = customerInfo.getFeatures().length;
        features = new double[size];
        System.arraycopy(customerInfo.getFeatures(), 0, features, 0, size);
        this.cluster = customerInfo.getCluster();
    }

}