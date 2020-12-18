package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

public class FractalEXplorer {

    private int windowWidth;
    private int windowHeight;

    private JImageDisplay updateDisplay;
    private FractalGenerator gen;
    private Rectangle2D.Double range;


    public FractalEXplorer(int w, int h){
        windowWidth = w;
        windowHeight = h;
        gen = new Mandelbrot();
        range = new Rectangle2D.Double();
        gen.getInitialRange(range);

    }

    public void createAndShowGUI(){

        JFrame myFrame = new JFrame("Визуалиация фрактала");
        updateDisplay = new JImageDisplay(windowWidth, windowHeight);
        JButton myBtn = new JButton("Сборос приближения");
        JButton save = new JButton("Сохранить изображение");
        JPanel south = new JPanel();
        JPanel topPanel = new JPanel();
        JLabel comLabel = new JLabel("Тип фрактала: ");
        JComboBox myComBox = new JComboBox();


        myBtn.setActionCommand("reset");
        myBtn.addActionListener(new MyActionListener());

        myComBox.addItem("Mandelbrot");
        myComBox.addItem("Tricorn");
        myComBox.addItem("Burning Ship");
        myComBox.addActionListener(new MyComBoxListener());

        save.setActionCommand("save");
        save.addActionListener(new MyActionListener());

        //topPanel Action
        topPanel.add(comLabel);
        topPanel.add(myComBox);

        south.add(myBtn);
        south.add(save);

        myFrame.getContentPane().add(updateDisplay, BorderLayout.CENTER);
        myFrame.getContentPane().add(south,BorderLayout.SOUTH);
        myFrame.getContentPane().add(topPanel, BorderLayout.NORTH);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Выход из приложения при закрытии

        myFrame.getContentPane().addMouseListener(new MyMouseListener());

        myFrame.pack ();
        myFrame.setVisible (true);
        myFrame.setResizable (false);
    }




    private void drawFractal (){
        for (int x = 0; x < windowWidth; x++) {
            for (int y = 0; y < windowHeight; y++) {
                double xCoord = gen.getCoord(range.x, range.x + range.width, windowWidth, x);
                double yCoord = gen.getCoord(range.y, range.y + range.width, windowWidth, y);

                double numIters = gen.numIterations(xCoord, yCoord);

                int rgbColor = 0;
                if (numIters != -1){
                    float hue = 0.7f + (float) numIters / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                }

                updateDisplay.drawPixel(x, y, rgbColor);
                updateDisplay.repaint();
            }
        }
    }

    public class MyComBoxListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JComboBox box = (JComboBox)e.getSource();
            String item = (String)box.getSelectedItem();

            if (item.equals("Tricorn")) {
                gen = new Tricorn();
            } else if (item.equals("Burning Ship")) {
                gen = new BurningShip();
            } else if (item.equals("Mandelbrot")) {
                gen = new Mandelbrot();
            }
            gen.getInitialRange(range);
            drawFractal();
        }
    }

    public class MyActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == "reset") {
                gen.getInitialRange(range);
                drawFractal();
            } else if (e.getActionCommand() == "save") {

                JFileChooser chooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);

                int saving = chooser.showSaveDialog(updateDisplay);
                System.out.println(saving);
                if (saving==JFileChooser.APPROVE_OPTION){
                    File dir = chooser.getSelectedFile();
                    if (!(dir.toPath().endsWith(".png"))){
                        dir = new File(dir + ".png");
                    }
                    try {
                        ImageIO.write(updateDisplay.img,"png", dir);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        JOptionPane.showMessageDialog(updateDisplay,ioException.getMessage(),"Не можем сохранить изображение", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    public class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            double xCoord = gen.getCoord(range.x, range.x + range.width, windowWidth, e.getX());
            double yCoord = gen.getCoord(range.y, range.y + range.height, windowWidth, e.getY());
            gen.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public static void main(String[] args) {
        FractalEXplorer MainExploer = new FractalEXplorer(600,600);
        MainExploer.createAndShowGUI();
        MainExploer.drawFractal();

    }
}