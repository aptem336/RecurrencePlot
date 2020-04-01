package com.vycka;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecurrencePlot extends JFrame {

    public static void main(String[] args) throws IOException {
        //читаем данные из файла
        List<Double> TSList = new ArrayList<>();
        BufferedReader TSBufferedReader = new BufferedReader(new FileReader("resources/8"));
        String line;
        while ((line = TSBufferedReader.readLine()) != null) {
            TSList.add(Double.parseDouble(line));
        }
        Double[] TS = TSList.toArray(new Double[0]);
        //инициализируем фрейм
        RecurrencePlot me = new RecurrencePlot();
        me.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        me.setLayout(new BorderLayout());
        me.setSize(1000, 800);
        me.setLocationRelativeTo(null);
        //инициализруем панель с контроллами
        JPanel controlsPanel = new JPanel();
        controlsPanel.setSize(200, 800);
        me.add(controlsPanel, BorderLayout.LINE_END);
        //инициализируем панель с диаграммой
        double R = 1;
        BufferedImage imgEuclid = buildImage(TS, (v1, v2) -> Math.hypot(v2.x - v1.x, v2.y - v1.y), R);
        BufferedImage imgManhattan = buildImage(TS, (v1, v2) -> Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y), R);
        BufferedImage imgInfinity = buildImage(TS, (v1, v2) -> Math.max(v1.x - v2.x, v1.y - v2.y), R);//Q
        JPanel diagramPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(imgEuclid, 0, 0, 800, 800, null);
            }
        };
        diagramPanel.setSize(800, 800);
        me.add(diagramPanel, BorderLayout.CENTER);

        me.setVisible(true);
    }

    private static BufferedImage buildImage(Double[] TS, LenCalculationStrategy lenCalculationStrategy, double R) {
        Vector[] vectors = new Vector[TS.length - 1];
        for (int i = 0; i < TS.length - 1; i++) {
            vectors[i] = new Vector(TS[i], TS[i + 1]);
        }
        BufferedImage img = new BufferedImage(vectors.length, vectors.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors.length; j++) {
                img.setRGB(i, j, lenCalculationStrategy.calc(vectors[i], vectors[j]) > R ? 0 : 0xFFFFFF);
            }
        }
        return img;
    }

    private interface LenCalculationStrategy {
        double calc(Vector v1, Vector v2);
    }

    private static class Vector {
        private double x;
        private double y;

        Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }

}
