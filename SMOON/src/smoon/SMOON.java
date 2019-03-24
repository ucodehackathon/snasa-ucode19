/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smoon;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import static java.lang.Math.round;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Albert
 */
public class SMOON {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = null;
//         try (ObjectInputStream in = new ObjectInputStream(
//            new FileInputStream("continuo.dat"))) {
        float[] prueba = null;
        Random rn = new Random();

        int i = 0;
        boolean subida = false;
        FileWriter fichero = null;
        PrintWriter pw = null;
        int contadorp = 0;
        float threshold = 0;
        int primerpic = 0;
        int desplaz = 0;
        int ultim=0;
        int tercerpic=0;
        int antepen=0;
        int penultim=0;
        int deci=0;
        Scanner reader=null;

//        for(int l=0;l<prueba.length;l++){
//            if(rn.nextFloat()>0.9){
//            prueba[l]=(float) 9;
//        }
//            else{
//                prueba[l]=0;
//            }
//        }
        String csvFile = "C:/no.csv";
        BufferedReader brr = null;
        BufferedReader brrr = null;
        String line = "";
        String cvsSplitBy = ",";
        int linecount = 0;
        try {

            brr = new BufferedReader(new FileReader(csvFile));
            while ((line = brr.readLine()) != null) {
                linecount++;

            }
            int t = 0;
            Float[][] dades = new Float[linecount][14];
            brrr = new BufferedReader(new FileReader(csvFile));

            while ((line = brrr.readLine()) != null) {
                String[] si = line.split(cvsSplitBy);
                for (int y = 0; y < 14; y++) {
                    dades[t][y] = Float.parseFloat(si[y]);
                }
                t++;
            }
            prueba = new float[linecount];
            for (int row = 0; row < linecount; row++) {
                prueba[row] = dades[row][1];
            }

            System.out.println("Escoge el valor del threshold de pico: ");

            reader = new Scanner(System.in);

            threshold = reader.nextFloat();
            System.out.println("El valor de threshold actual es de: " + threshold);

            fichero = new FileWriter("c:/prueba.txt");
            pw = new PrintWriter(fichero);
            int k = 0;
            while (k < prueba.length) {
                if (subida == false && prueba[k] >= threshold) {
                    subida = true;

                    if (contadorp < 3) {
                        if (contadorp == 0) {
                            primerpic = k;
                        }
                        if(contadorp==2){
                            tercerpic=k;
                        }
                        pw.println( k + "  " + prueba[k] + ". Esta muestra es uno de los 3 primeros picos de control");
                    } else {
                        pw.println( k + "  " + prueba[k]);
                    }
                    contadorp++;
                antepen=penultim;
                penultim=ultim;
                ultim=k;
                } else if (subida == true && prueba[k] < threshold) {
                    subida = false;
                }
                
                

                k++;
            }
            pw.println("El valor de los 3 ultimos picos se corresponde a picos de control");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (brr != null) {
                try {
                    brr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           System.out.println("Desea que se recorte la parte sobrante de la señal para que solo se vea aquello que se alla \nentre el primer pico y el último (1) o prefiere escoger cuántas posiciones moverse (negativo para izq y viceversa)\n [Valor máximo hacia la izquierda de: "+ primerpic+"] (2)");
           deci=reader.nextInt();
           if(deci==1){
           

            float[] arreglo = new float[ultim - primerpic];
            for (int m = primerpic; m < ultim; m++) {
                arreglo[m-primerpic] = prueba[m];
            }
            float[] arreglo2 = new float[antepen - tercerpic];
            for(int f=tercerpic;f<antepen;f++){
                arreglo2[f-tercerpic] =prueba[f];
            }
            PrintStream fileO = new PrintStream("Mat.txt");
            System.setOut(fileO);
            for (int p = 0; p<prueba.length; p++) {
                System.out.println(prueba[p]);
            }

            PrintStream fileOn = new PrintStream("Mat2.txt");
            System.setOut(fileOn);
            for (int l = 0; l <arreglo.length; l++) {
                System.out.println(arreglo[l]);
            }
            PrintStream fileOnd = new PrintStream("Mat3.txt");
            System.setOut(fileOnd);
            for (int l = 0; l <arreglo2.length; l++) {
                System.out.println(arreglo2[l]);
            }

        }
           else{
               
               System.out.println("Introduzca el valor de muestras que desea desplazar la grafica (positivo hacia der, negativo hacia izq)");
               desplaz = reader.nextInt();
               System.out.println("El valor a desplazar es: " + desplaz);
               float[] arreglo = new float[prueba.length + Math.abs(desplaz)];
               
               if (desplaz < 0) {
                    for(int l=0;l<prueba.length;l++){
                        arreglo[l]=prueba[l];
                    }
                    for(int l=0;l<Math.abs(desplaz);l++){
                        arreglo[prueba.length+l]=0;
                    }
                    for (int m = 0 ; m<prueba.length; m++){
                               arreglo[m] = arreglo[m+Math.abs(desplaz)];
                     }
//                       for(int m=0;m<Math.abs(desplaz)-1;m++){
//                           arreglo[m+prueba.length]=0;
//                       }
//                       
                   
//               for (int b=0; b < Math.abs(desplaz); b++){
//                   arreglo[prueba.length+b-2]=0;
//                           }              
                
                 PrintStream fileO = new PrintStream("Mat.txt");
                System.setOut(fileO);
                for(int p=0;p<prueba.length;p++){
                    System.out.println(prueba[p]);
                }
                
                PrintStream fileOn = new PrintStream("Mat2.txt");
                for(int l=0;l<arreglo.length;l++){
                    System.out.println(arreglo[l]);
                }
               }else if (desplaz > 0) {

                   for (int j = 0; j < prueba.length; j++) {
                       arreglo[arreglo.length - 1 - j] = prueba[prueba.length - 1 - j];

                   }
                   for (int o = 0; o < desplaz+1; o++) {
                       arreglo[o] = 0;
                   }
                PrintStream fileO = new PrintStream("Mat.txt");
                System.setOut(fileO);
                for(int p=0;p<prueba.length;p++){
                    System.out.println(prueba[p]);
                }
                
                PrintStream fileOn = new PrintStream("Mat2.txt");
                System.setOut(fileOn);
                for(int l=0;l<arreglo.length;l++){
                    System.out.println(arreglo[l]);
                }
                   

               }
           }
    }
    }

}
