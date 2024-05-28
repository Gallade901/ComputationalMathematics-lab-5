package spring;

import functions.Function;
import functions.Function1;
import functions.Function2;
import methods.Lagrange;
import methods.Method;
import methods.NewtonOfDividedDifferences;
import methods.NewtonOfFiniteDifferences;
import util.GraphPrinter;
import util.InputReader;
import util.Points;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static double checkFinite(double[] x) {
        if (x.length == 1)
            return 0;

        double h = x[1] - x[0];
        for (int i = 1; i < x.length - 1; i++) {
            if (x[i + 1] - x[i] != h)
                return -1;
        }

        return h;
    }
    public ArrayList<ArrayList<Double>> calculateMethods(Points points, Double max, Double min) {
        Method lagrange = new Lagrange(points.getX(), points.getY());
        Method newtonOfDividedDifferences = new NewtonOfDividedDifferences(lagrange.getX(), lagrange.getY());
        Method newtonOfFiniteDifferences = null;
        double h = checkFinite(points.getX());
        boolean flag = false;
        if (h != -1) {
            newtonOfFiniteDifferences = new NewtonOfFiniteDifferences(points.getX(), points.getY(), h);
            flag = true;
        }
        ArrayList<ArrayList<Double>> ans = new ArrayList<>();
        ArrayList<Double> lagr = new ArrayList<>();
        ArrayList<Double> newtonDiv = new ArrayList<>();
        ArrayList<Double> newtonOf = new ArrayList<>();
        ArrayList<Double> x = new ArrayList<>();
        double gep = (max - min) / 100;
        for (double i = min - 1; i < max + 1; i+= gep) {
            x.add(i);
            lagr.add(lagrange.calculate(i));
            newtonDiv.add(newtonOfDividedDifferences.calculate(i));
            if (flag) {
                newtonOf.add(newtonOfFiniteDifferences.calculate(i));
            }
        }
        ans.add(x);
        ans.add(lagr);
        ans.add(newtonDiv);
        if (flag) {
            ans.add(newtonOf);
        }
        return ans;
    }


//    static void handleFunctionInput() {
//        Function[] functions = {
//                new Function1(),
//                new Function2()
//        };
//        System.out.println("Выберите функцию: ");
//        for (int i = 0; i < functions.length; i++)
//            System.out.println("\t" + (i + 1) + ". " + functions[i]);
//        InputReader inputReader = new InputReader();
//        Function function = functions[inputReader.readIndex("Введите номер: ", "Функции под таким номером не существует.", functions.length) - 1];
//
//        double left, right;
//
//        while (true) {
//            left = inputReader.readDouble("Введите левую границу интервала: ");
//            if (!(left > function.getLeftLimit() && left < function.getRightLimit())) {
//                System.out.println("Указанная граница не входит в область определения функции.");
//                continue;
//            }
//            right = inputReader.readDouble("Введите правую границу интервала: ");
//            if (!(right > function.getLeftLimit() && right < function.getRightLimit())) {
//                System.out.println("Указанная граница не входит в область определения функции.");
//                continue;
//            }
//            if (right <= left) {
//                System.out.println("Правая граница должна быть правее левой границы.");
//                continue;
//            }
//            break;
//        }
//
//        int n = inputReader.readPositiveInt("Введите кол-во точек: ");
//
//        double[] x = new double[n];
//        double[] y = new double[n];
//
//        double h = (right - left) / n;
//        for (int i = 0; i < n; i++) {
//            x[i] = left + h * i;
//            y[i] = function.calculate(left + h * i);
//        }
//
//        calculateMethods(new Points(x, y), function);
//    }



}
