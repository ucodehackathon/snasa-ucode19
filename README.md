# snasa-ucode19

# Información del Proyecto SMOON#

  ## Miembros

      Elisabet Rufas Talamás
      Pau Rodríguez Inserte
      Albert Molina Alvarez
      Eric Santaigo Garcia 

  ## Explicación

Este proyecto tiene como objetivo simplificar la sincronización de datos recibidos por video y por sensor mediante el uso de Matlab y Java. Al inicio del proyecto se nos presentó el problema de que la sincronización de estos dos elementos era un trabajo más arduo de lo que deberia debido a que se hacia manualmente analizando los frames del video y los picos de señal captados por el sensor. Conociendo que existia una serie de puntos comunes de calibración en los dos medios, hemos desarrollado un codigo Java que analiza el espectro de la señal recibida del archivo .csv del sensor, localiza todos los picos que sobrepasan un cierto umbral escogido y los imprime en un fichero .txt. Con esta información, se ofrece al usuario 2 opciones: seguir un proceso automatizado que devuelve la información pero esta vez cortada de manera que se obvien los datos no relevantes previos a los mencionados elementos de sincronización o escoger cuanto se desea desplazar la grafica a derecho o izquierda dentro de unos límites razonables. Esta información queda plasmada en una serie de ficheros .txt y posteriormente son procesados por Matlab para mostrar la parte más relevante (almacenada en Mat2.txt). Finalmente, en Matlab son tambien ejecutadas una serie de funciones para obtener del video el espectro del sonido y este es recortado para que quede también eliminada la parte no relevante previa a los picos de sonido. Además, tanto en este como en la primera opción del programa de Java, es eliminada la información posterior al último pico de sincronización del final del test para quedarnos solo con la información relevante comprimida enmedio.  Finalmente, Mediante una interfaz de Matlab se muestra el video grabado de la sesión comenzando a partir del primer frame donde se realize la sincronización, la grafica del sonido recortada y la grafica de la información almacenada en Mat2.txt de modo que en caso de haber escogido la primera opción se puede observar como la información procesada por Java y por Matlab ahora presentan una sincronización con unas prestaciones mucho mejores que las iniciales.

## Elementos utilizados

Los requisitos de materiales para este proyecto no son excesivos. Es necesario disponer de uno o más ordenadores que dispongan de una versión no obsoleta de Matlab, un programa de escritura, compilación y ejecución de lenguaje Java (en nuestro caso, Netbeans), un sensor deportivo con acelerometro (el cual fue proporcionado por Adidas) y una libreria especial de Matlab para mostrar el video en la interfaz. De manera complementaria hemos disponido de una serie de datos en formato .csv con diferentes jugadas y configuraciones que hemos utilizado en la fase inicial para testear el código aunque esto también puede ser realizado con datos registrados propios. 

##  Que aporta?

El resultado final del proyecto es un codigo que automatiza el proceso de sincronización de la señal de manera que tan solo es necesario situar el archivo

# Descripción del codigo

## Java

Una vez realizados todos los imports necesarios y algunas inicializaciones básicas para no tener problemas en puntos posteriores del código, el primer paso importante es recibir la información del archivo .csv para poder trabajar con ella

```
        String csvFile = "**RUTA_DEL_FICHERO_CSV**";
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
```
Una vez entrado en el try, se crean dos BufferedReader que leen el fichero csvFile. En la primera ejecución de readLine simplemente se realiza un conteo del número de muestras de información que tenemos para crear una matriz de datos de dimensiones *linecount* x 14. Una vez hecho esto. Se realiza la segunda ejecución de readLine() con el propósito de esta vez ir guardando toda la información del fichero dentro de la matriz dades.

```
            prueba = new float[linecount];
            for (int row = 0; row < linecount; row++) {
                prueba[row] = dades[row][1];
            }

```
Mediante esta instrucción se realiza la selección de qué dato vamos a utlizar. En nuestro caso hemos escogido la columna 1 de todas ya que la columna 0 no contenia más que numeración.

```
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
                        if (contadorp == 2) {
                            tercerpic = k;
                        }
                        pw.println(k + "  " + prueba[k] + ". Esta muestra es uno de los 3 primeros picos de control");
                    } else {
                        pw.println(k + "  " + prueba[k]);
                    }
                    contadorp++;
                    antepen = penultim;
                    penultim = ultim;
                    ultim = k;
                } else if (subida == true && prueba[k] < threshold) {
                    subida = false;
                }

                k++;
            }
            pw.println("El valor de los 3 ultimos picos se corresponde a picos de control");
```

Se escoge el threshold del valor a partir del cual consideraremos que se ha producido un pico de nivel suficientemente alto como para ser relevante. Una vez se recibe esta información, se crea el fichero donde sera guardada la información. Dentro del while se analizan todas las muestras y en caso de detectar una subida que supera el valor de threshold, es anotado en el fichero, se etiquetan los 3 primeros picos y se va procesando el resto de picos obtenidos, guardando el primero, tercero, último y antepenúltimo. El parámetro subida garantiza que, en caso de que un pico de señal dure más de una muestra sin bajar, no se detecte como doble sino que se espera a que se baje del nivel de threshold antes de contabilizar más datos como picos de señal.

```
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
            ...
```
Se le pregunta al usuario qué opción de las dos mencionadas en apartados posteriores quiere ejecutar y en caso de ser la primera, se realizan las instrucciones de arriba, las cuales aprovechan la información de la posición de los 4 picos relevantes almacenados arriba para recortar la señal y guardarla de 2 maneras distintas (cortando de primer a último y de tercer a antepenúltimo pico). Para realizar esto, se ejecuta 3 veces el último bloque a partir del PrintStream para almacenar en los archivos Mat.txt, Mat2.txt y Mat3.txt la información de prueba, arreglo y arreglo2 respectivamente.

```
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
```
En caso de escoger la segunda opción, se le pregunta al usuario cual es la distancia en muestras que desea mover los datos almacenados en el array prueba. Si el número es negativo, este se desplaza hacia la izquierda medainte las instrucciones superiores y se mandan a los archivos Mat.txt y Mat2.txt de la misma manera que se hizo en el bloque de código previo a este.

```
for (int j = 0; j < prueba.length; j++) {
                       arreglo[arreglo.length - 1 - j] = prueba[prueba.length - 1 - j];

                   }
                   for (int o = 0; o < desplaz+1; o++) {
                       arreglo[o] = 0;
                   }
``` 
En caso de que el número sea positivo, se desplazan a la derecha y se repite el proceso de sacar los datos a los archivos .txt.

**Los archivos se crearan en el directorio del proyecto de Netbeans, por lo que si se desea moverlos a otro es necesario especificar su ruta entera**

## Matlab

En Matlab el codigo se encuentra repartido en diferentes ficheros .m que se ejecutan como funciones dentro de otras partes del codigo. Para simplificar la redacción de este apartado, se mostraran los bloques de código siguiendo una progresión que permita conocer lo que sucede por niveles.

```
function [Ni,Nf] = timeobt(video)

signal = audioread(video);

Ns = length(signal);
i = 1;
n = 1:Ns;
for n = 1:1:Ns
    if signal(n) > 0.6
        max(i) = n;
        i = i+1;
    end
end
figure
plot((signal));
end
```
Con la función timeobt se obtiene la señal de audio mediante la función audioread de Matlab del video pasado por parámetro. Luego se obtiene el número de muestras, se recorre toda buscando los máximos y almacenandolos y se muestra graficamente.


```
fileID= fopen('Mat.txt','r');
fileID2=fopen('Mat2.txt','r');
fileID3=fopen('Mat3.txt','r');
formatSpec= '%f\n';
A=fscanf(fileID,formatSpec);
B=fscanf(fileID2,formatSpec);
C=fscanf(fileID3,formatSpec);
figure();
subplot(2,2,1);
plot(A);
subplot(2,2,2);
plot(B);
subplot(2,2,3);
plot(C);
```
Esta última parte del código contenida en Untitled2.m muestra la imagen obtenida de los archivos .txt obtenidos al final del proceso realizado en Netbeans. No tiene demasiada complejidad ya que se basa en leer de los ficheros .txt con el formato especificado y hacer plots en la figura para ver comparativamente como ha cambiado la señal del archivo .csv a lo largo del proceso.


Se ha implementado tambien una interfaz que muestra simultaneamente el video, la grafica del sonido recortado y la grafica de los datos almacenados en Mat2.txt

## Resultados

Señal inicial:

![alt text](https://i.imgur.com/1slaEoj.png)

Señales tras el proceso de recorte para sincronización:

![alt text](https://i.imgur.com/hElCSAp.png)

Interfaz con toda la información simultaneamente:

![alt text](https://i.imgur.com/JZ6fLNT.jpg)

