package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FractalEXplorer {

    private int windowWidth;
    private int windowHeight;

    private JImageDisplay updateDisplay;
    private FractalGenerator gen;
    private Rectangle2D.Double range;
    public int rowsRemaining;
    private JButton myBtn = new JButton("Сборос приближения");
    private JButton save = new JButton("Сохранить изображение");
    private JComboBox myComBox = new JComboBox();



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
        JPanel south = new JPanel();
        JPanel topPanel = new JPanel();
        JLabel comLabel = new JLabel("Тип фрактала: ");


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
        enableUI(false);
        rowsRemaining = windowHeight;
        for (int i = 0;i<windowHeight;i++){
            FractalWorker temp = new FractalWorker(i);
            temp.execute();
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
                    if (!(dir.toString().endsWith(".png"))){
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
            if (rowsRemaining == 0) {
                enableUI(true);
                double xCoord = gen.getCoord(range.x, range.x + range.width, windowWidth, e.getX());
                double yCoord = gen.getCoord(range.y, range.y + range.height, windowWidth, e.getY());
                gen.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
                drawFractal();
            }
            else enableUI(false);
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
    void enableUI(boolean val){
            myComBox.setEnabled(val);
            save.setEnabled(val);
            myBtn.setEnabled(val);
    }
    private class FractalWorker extends SwingWorker<Object, Object> {
            int y = 0;
            ArrayList colors = new ArrayList();

            public FractalWorker(int y) {
                this.y = y;
            }

            @Override
            protected Object doInBackground() throws Exception {
                for (int x = 0; x < windowWidth; x++) {
                    double xCoord = gen.getCoord(range.x, range.x + range.width, windowWidth, x);
                    double yCoord = gen.getCoord(range.y, range.y + range.width, windowWidth, y);

                    double numIters = gen.numIterations(xCoord, yCoord);

                    int rgbColor = 0;
                    if (numIters != -1) {
                        float hue = 0.7f + (float) numIters / 200f;
                        rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    }
                    colors.add(rgbColor);

                }
                return null;
            }

            @Override
            protected void done() {
                for (int i = 0; i< colors.size(); i++){
                    updateDisplay.drawPixel(i,y,(int)colors.get(i));
                }
                updateDisplay.repaint(0,y,windowWidth,1);
                rowsRemaining-=1;
                if (rowsRemaining==0) enableUI(true);
        }
    }

    public static void main(String[] args) {
        FractalEXplorer MainExploer = new FractalEXplorer(600, 600);
        MainExploer.createAndShowGUI();
        MainExploer.drawFractal();

        }
}
