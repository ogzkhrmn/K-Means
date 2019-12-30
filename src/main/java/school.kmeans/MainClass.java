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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

public class MainClass extends ApplicationFrame {

    private static KMeans kMeans;
    private static String[] titles = new String[]{"Gender","Age","Salary","Spending Score"};

    public static void main(String[] args) throws IOException {
        System.out.println("Please choose features with comma(1 2 3 4): Example = 1,3,4");
        StringBuilder title = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        String inputString = scanner.nextLine();
        String[] splited = inputString.trim().split(",");
        int[] selectedFeatures = new int[splited.length];
        for (int i = 0; i < splited.length; i++) {
            selectedFeatures[i] = Integer.parseInt(splited[i]) - 1;
            title.append(titles[selectedFeatures[i]]).append(" - ");
        }
        kMeans = new KMeans();
        kMeans.openFile(args[0], selectedFeatures);

        MainClass chart = new MainClass(
            "Cluster Vs MSE" ,
            title.substring(0, title.length() - 3));
        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
        chart.toFront();
        chart.setAlwaysOnTop(true);
    }

    public MainClass( String applicationTitle , String chartTitle ) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
            chartTitle,
            "Cluster","Mse",
            createDataset(),
            PlotOrientation.VERTICAL,
            true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
    }

    private DefaultCategoryDataset createDataset( ) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        double mse1 = kMeans.calculateKMeans(2);
        int selected = 2;
        double difference = 0;
        double mse2;
        dataset.addValue( mse1 , "mse" , 2 +"" );
        for (int i = 3; i <= 10; i++) {
            mse2 = kMeans.calculateKMeans(i);
            if (mse1 - mse2 > difference) {
                difference = mse1 - mse2;
                selected = i;
            }
            mse1 = mse2;
            dataset.addValue( mse1 , "mse" , i +"" );
        }
        return dataset;
    }

}