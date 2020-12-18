package com.company;

import org.w3c.dom.css.RGBColor;

import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;


public class JImageDisplay extends JComponent {
    public BufferedImage img;


    public JImageDisplay(int w, int h){
        img = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(w,h));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage (img, 0, 0, img.getWidth(), img.getHeight(), null);
    }
    public void clearImage(int rbgColor){
        for (int i = 0; i<img.getWidth();i++){
            for (int j=0; j<img.getHeight();j++){
                img.setRGB(i,j,0);
            }
        }
    }
    public void drawPixel(int x, int y, int rgbColor){
        img.setRGB(x,y,rgbColor);
    }
}
