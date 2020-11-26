package com.company;
import java.util.*;
public class Main {
    public static double computeArea(Point3d point1, Point3d point2, Point3d point3) {
        double side1, side2,side3;
        side1 = point1.distanceTo(point2);
        side2 = point2.distanceTo(point3);
        side3 = point3.distanceTo(point1);
        double p = (side1+side2+side3)/2;
        double area = Math.sqrt(p*(p-side1)*(p-side2)*(p-side3));
        return area;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in).useLocale(Locale.ENGLISH);
        Point3d point1 = new Point3d();
        Point3d point2 = new Point3d();
        Point3d point3 = new Point3d();
        System.out.print("Введите координаты первой точки: ");
        String s1 = in.nextLine();
        System.out.print("Введите координаты второй точки: ");
        String s2 = in.nextLine();
        System.out.print("Введите координаты третьей точки: ");
        String s3 = in.nextLine();
        String[] s11 = s1.split(" ");
        String[] s22 = s2.split(" ");
        String[] s33 = s3.split(" ");
        point1.setX(Double.parseDouble(s11[0]));
        point1.setY(Double.parseDouble(s11[1]));
        point1.setZ(Double.parseDouble(s11[2]));
        point2.setX(Double.parseDouble(s22[0]));
        point2.setY(Double.parseDouble(s22[1]));
        point2.setZ(Double.parseDouble(s22[2]));
        point3.setX(Double.parseDouble(s33[0]));
        point3.setY(Double.parseDouble(s33[1]));
        point3.setZ(Double.parseDouble(s33[2]));
        if (point1.comparison(point2) || point2.comparison(point3) || point1.comparison(point3)) {
            System.out.print("Вы ввели несколько идентичных точек");
        }
        else {
            double res = computeArea(point1, point2, point3);
            if (Double.isNaN(res)) {
                System.out.println("Такого треугольника не существует");
            }
            else {
                System.out.println("Площадь треугольника равна " + res);
            }
        }

    }
}
