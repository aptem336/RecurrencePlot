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

    private static BufferedImage img;

    public static void main(String[] args) throws IOException {
        //читаем данные из файла
        List<Double> TSList = new ArrayList<>();
        BufferedReader TSBufferedReader = new BufferedReader(new FileReader("resources/TS"));
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
        //инициализируем панель с диаграммой
        JPanel diagramPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(getImg(), 0, 0, null);
            }
        };
        diagramPanel.setSize(800, 800);
        me.add(diagramPanel, BorderLayout.CENTER);
        //инициализруем панель с контроллами
        JPanel controlsPanel = new JPanel();
        controlsPanel.setSize(200, 800);
        me.add(controlsPanel, BorderLayout.LINE_END);

        me.setVisible(true);
    }

    private static BufferedImage getImg() {
        if (img == null) {
            img = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
        }
        return img;
    }
}
