package colorblindness;

import processing.core.PApplet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class colorblindness {

    PApplet myParent;

    int myVariable = 0;
    int colors[][][];

    public final static String VERSION = "##library.prettyVersion##";



    public colorblindness(PApplet theParent, boolean createTable) {
        myParent = theParent;
        welcome();

        colors = new int[256][256][256];

        if(createTable){
            //call to create lookuptable
        }

    }

    public static double[][] multiplyMatrices(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if(m1ColLength != m2RowLength) return null; // matrix multiplication is not possible
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length
        double[][] mResult = new double[mRRowLength][mRColLength];
        for(int i = 0; i < mRRowLength; i++) {         // rows from m1
            for(int j = 0; j < mRColLength; j++) {     // columns from m2
                for(int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return mResult;
    }

    public static BufferedImage open(String imageName){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static void saveImage(String savePath, BufferedImage img){
        File outputFile = new File(savePath);
        try {
            ImageIO.write(img, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void correctImage(BufferedImage img, String savePath){
        //Correction using colordifference method!
    }

    public static void rgbContrast(String imageName, String savePath){
        BufferedImage img = open(imageName);
        int width = img.getWidth();
        int height = img.getHeight();

        double factor = 0.28;

        for(int i=0;i<height;i++) {
            for (int j = 0; j < width; j++) {
                Color myColor = new Color(img.getRGB(i,j));
                double newRed = (1 - (myColor.getRed()/255) * factor) + myColor.getRed()/255;
                double newGreen = (1 - (myColor.getGreen()/255) * factor) + myColor.getGreen()/255;
                double newBlue;
                if(newRed > newGreen){
                    newBlue = myColor.getBlue()/255 - (myColor.getBlue()/255 * factor);
                }else{
                    newBlue = (1 - (myColor.getBlue()/255) * factor) + myColor.getBlue()/255;
                }

                Color newColor = new Color((int) (newRed * 255),(int) (newGreen * 255), (int) (newBlue*255));

                int rgb = newColor.getRGB();

                img.setRGB(i,j,rgb);

                saveImage(savePath, img);
            }
        }

    }

    public static void simulate(int type, String imageName, String savePath, boolean correct){
        BufferedImage img = open(imageName);
        int width = img.getWidth();
        int height = img.getHeight();
        double current[][] = new double[3][1];
        double toLMS[][] = {{17.8824, 43.5161, 4.11935}, {3.45565, 27.1554, 3.86714}, {0.0299566, 0.184309, 1.46709}};
        double protanope[][] = {{0, 2.02344, -2.52581}, {0, 1, 0}, {0, 0, 1}};
        double deutranope[][] = {{1, 0, 0}, {0.494207, 0, 1.24827}, {0, 0, 1}};
        double tritanope[][] = {{1, 0, 0}, {0, 1, 0}, {-0.395913, 0.801109, 0}};
        double toRGB[][] = {{17.8824, 43.5161, 4.11935}, {3.45565, 27.1554, 3.86714}, {0.0299566, 0.184309, 1.46709}};

        for(int i=0;i<height;i++) {
            for (int j = 0; j < width; j++) {
                Color myColor = new Color(img.getRGB(i, j));
                current[0][0] = myColor.getRed() / 255;
                current[1][0] = myColor.getGreen() / 255;
                current[2][0] = myColor.getBlue() / 255;

                current = multiplyMatrices(toLMS, current);
                switch (type){
                    case 1:
                        current = multiplyMatrices(protanope,current);
                        break;
                    case 2:
                        current = multiplyMatrices(deutranope,current);
                        break;
                    case 3:
                        current = multiplyMatrices(tritanope,current);
                        break;
                    default:
                        //throw error
                        break;
                }

                current = multiplyMatrices(toRGB,current);
                Color newColor = new Color((int) (current[0][0] * 255),(int) (current[1][0] * 255), (int) (current[2][0]*255));

                int rgb = newColor.getRGB();

                img.setRGB(i,j,rgb);
            }
        }

        if(!correct) {
            saveImage(savePath, img);
        } else{
            correctImage(img,savePath);
            }
        }



    private void welcome() {
        System.out.println("##library.name## ##library.prettyVersion## by ##author##");
    }


    public String sayHello() {
        return "Colorblindness 1.0";
    }

    public static String version() {
        return VERSION;
    }

}
