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

    private double gender;
    private double age;
    private double annualIncome;
    private double spendingScore;
    private int cluster;

    public CustomerInfo(CustomerInfo customerInfo) {
        this.gender = customerInfo.getGender();
        this.age = customerInfo.getAge();
        this.annualIncome = customerInfo.getAnnualIncome();
        this.spendingScore = customerInfo.getSpendingScore();
    }

}