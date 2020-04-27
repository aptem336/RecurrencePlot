package com.vycka;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RecurrencePlot {
    /**
     * Чтение массива значений сигнала из файла
     *
     * @param timeSeriesFileIndex индекс файла
     * @return массив значений сигнала
     * @throws IOException ошибка чтения
     */
    public static Double[] readTimeSeries(Integer timeSeriesFileIndex) throws IOException {
        ArrayList<Double> timeSeries = new ArrayList<>();
        BufferedReader timeSeriesBufferedReader = new BufferedReader(new FileReader("resources/" + timeSeriesFileIndex));
        String line;
        while ((line = timeSeriesBufferedReader.readLine()) != null) {
            timeSeries.add(Double.parseDouble(line));
        }
        return timeSeries.toArray(new Double[0]);
    }

    /**
     * Формирует вектора из из значений сигнала
     *
     * @param timeSeries массив значений сигнала
     * @param D          количество сигналов в наборе
     * @param d          "расстояние" между сигналами в наборе
     * @return массив котежей из значений сигнала
     */
    public static Double[][] collectTimeSeriesCorteges(Double[] timeSeries, Integer D, Integer d) {
        Double[][] timerSeriesVectors = new Double[timeSeries.length - (D - 1) * d][D];
        for (int i = 0; i < timerSeriesVectors.length; i++) {
            for (int j = 0; j < D; j++) {
                timerSeriesVectors[i][j] = timeSeries[i + j * d];
            }
        }
        return timerSeriesVectors;
    }

    /**
     * Вычисляет расстояния "каждый с каждым" для кортежей значений сигнала
     *
     * @param timeSeriesVectors массив кортежей значений сигнала
     * @return расстояние "каждый с каждым" между кортежами
     */
    public static Double[][] calcTimeSeriesCortegesDifferences(Double[][] timeSeriesVectors, LenCalculationMethod lenCalculationMethod) {
        Double[][] timeSeriesDifferences = new Double[timeSeriesVectors.length][timeSeriesVectors.length];
        for (int i = 0; i < timeSeriesDifferences.length; i++) {
            for (int j = 0; j < timeSeriesDifferences[i].length; j++) {
                double[] differences = new double[timeSeriesVectors[i].length];
                for (int k = 0; k < differences.length; k++) {
                    differences[k] = timeSeriesVectors[j][k] - timeSeriesVectors[i][k];
                }
                timeSeriesDifferences[i][j] = lenCalculationMethod.calc(differences);
            }
        }
        return timeSeriesDifferences;
    }

    /**
     * Вычисляет параметр толеряции
     *
     * @param timeSeriesCortegesDifferences матрица разностей между кортежами значений сигнала
     * @param blackPointsPercent            процент черных точек
     * @param blackPointsPercentEps         допустимая погрешность
     * @return параметр толеряции
     */
    public static Double calcR(Double[][] timeSeriesCortegesDifferences, Double blackPointsPercent, Double blackPointsPercentEps) {
        double fx0 = timeSeriesCortegesDifferences[0][0];
        double fx1 = timeSeriesCortegesDifferences[0][0];
        double fx;
        for (Double[] timeSeriesCortegesDifference : timeSeriesCortegesDifferences) {
            for (Double jDouble : timeSeriesCortegesDifference) {
                fx0 = Math.min(jDouble, fx0);
                fx1 = Math.max(jDouble, fx1);
            }
        }
        double x0 = 0;
        double x1 = 1;
        double x = 0;
        do {
            if (blackPointsPercent - x > 0) {
                fx = linearInterpolation(x0, x1, fx0, fx1, blackPointsPercent);
            } else {
                fx = linearInterpolation(x1, x0, fx1, fx0, blackPointsPercent);
            }
            double blackPointsCount = 0;
            for (Double[] timeSeriesCortegesDifference : timeSeriesCortegesDifferences) {
                for (Double jDouble : timeSeriesCortegesDifference) {
                    blackPointsCount += jDouble <= fx ? 1 : 0;
                }
            }
            x = blackPointsCount / (timeSeriesCortegesDifferences.length * timeSeriesCortegesDifferences.length);

            fx1 = fx0;
            x1 = x0;
            fx0 = fx;
            x0 = x;
        } while (Math.abs(blackPointsPercent - x) > blackPointsPercentEps);
        return fx;
    }

    /**
     * Линейная интерполяция
     *
     * @param x0  точка x0
     * @param x1  точка x1
     * @param fx0 значение функции в точке x0
     * @param fx1 знаечние функции в точке x1
     * @param x   искомая точка
     * @return приближенное значение искомой точки
     */
    private static double linearInterpolation(double x0, double x1, double fx0, double fx1, double x) {
        return fx0 + (fx1 - fx0) / (x1 - x0) * (x - x0);
    }

    /**
     * Формирует изображение из матрицы разностей между кортежами значений сигнала
     *
     * @param timeSeriesDifferences матрица разностей между кортежами значений сигнала
     * @param r                     папраметр толерации
     * @return изображение из матрицы разностей
     */
    public static BufferedImage buildRecurrencePlotImage(Double[][] timeSeriesDifferences, Double r) {
        BufferedImage recurrencePlotImage = new BufferedImage(timeSeriesDifferences.length, timeSeriesDifferences[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < recurrencePlotImage.getWidth(); i++) {
            for (int j = 0; j < recurrencePlotImage.getHeight(); j++) {
                recurrencePlotImage.setRGB(i, j, timeSeriesDifferences[i][j] <= r ? 0 : 0xFFFFFF);
            }
        }
        return recurrencePlotImage;
    }
}
