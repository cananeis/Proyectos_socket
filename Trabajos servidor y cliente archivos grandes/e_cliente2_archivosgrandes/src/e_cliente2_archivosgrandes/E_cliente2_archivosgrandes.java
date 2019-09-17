/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e_cliente2_archivosgrandes;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ITLM
 */
public class E_cliente2_archivosgrandes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Declaracion de variables a utilizar
        Socket client = null;
        PrintWriter escritor = null;
        String d_archivo = null, msj = null;
        Scanner scanner = new Scanner(System.in);
        BufferedInputStream bis;
        BufferedOutputStream bos;
        boolean bandera = false,salida = false;
        String file = "";
        byte[] byteArray;
        long porc = 0, aux = 0,rec=0;
        int tama = 50,in;
        DataInputStream dis = null;
        String prueba = System.getProperty("user.home");
        try {
            //conectamos al cliente con el servidor con su ip y puerto
            client = new Socket(args[0], Integer.valueOf(args[1]));
        } catch (IOException e) {
            System.out.println("No se puedo conectar con el servidor");
            System.out.println("Por motivo de: " + e.toString());
            if ((e.toString()).equals("java.net.UnknownHostException: " + e.getMessage() + "")) {
                System.out.println("Cliente no conocido: " + e.getMessage());
            }
            if ((e.toString()).equals("java.net.ConnectException: Connection refused: connect")) {
                System.out.println("El servidor no responde");
            }
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException ex) {
            if ((ex.toString()).equals("java.lang.ArrayIndexOutOfBoundsException: 0")) {
                System.out.println("Faltan los parametros('Servidor' y 'Puerto')");
            }
            if ((ex.toString()).equals("java.lang.ArrayIndexOutOfBoundsException: 1")) {
                System.out.println("Falta un segundo parametro(puerto)");
            }
        } catch (NumberFormatException ex2) {
            if ((ex2.toString()).equals("java.lang.NumberFormatException: For input string: \"" + args[0] + "\"")) {
                System.out.println("El primer parametro no es numerico");
            }
            if ((ex2.toString()).equals("java.lang.NumberFormatException: For input string: \"" + args[1] + "\"")) {
                System.out.println("El segundo parametro no es numerico");
            }
        }

        try {
            //Un escritor para enviarle mensaje al servidor
            escritor = new PrintWriter(
                    client.getOutputStream(), true
            );
        } catch (IOException e) {
            System.out.println("Error con la salida de datos");
            System.out.println("Por motivo de: " + e.toString());
            System.exit(1);
        }
        //un ciclo para cumplir que ese envio al servidor
        while (bandera != true) {
            //Pedimos al cliente la direccion del archivo por consola
            System.out.print("Dirección del archivo a descargar:");
            d_archivo = scanner.nextLine();
            escritor.println(d_archivo);
            //mostramos un mensaje para que sepa donde se descargara
            System.out.println("Se descargara en:" + prueba + "\\downloads\\");
            bandera = true;
        }
        try {
            while (porc != 100 && salida != true) {
                //un mensaje por consola para donde compuebre que entro para enviar el archivo
                System.out.println("Comprobando el archivo...");
                //Buffer de 1024 bytes
                byteArray = new byte[8192];
                //hacemos un BufferedInputStream para poder recibir los byte del servidor
                bis = new BufferedInputStream(client.getInputStream());
                //hacemos un DataInputStream para poder recibir el tamañao y nombre del archivo si es que existe
                dis = new DataInputStream(client.getInputStream());
                //Recibimos el mensaje del servidor para saber si no ocurrio un error
                if ((msj = dis.readUTF()).equals("Archivo no existe")) {
                    System.out.println(msj);
                    salida = true;
                    System.exit(0);
                }
                else if (msj.equals("Especifique  bien la direccion del archivo con su extension, porfavor")) {
                    System.out.println(msj);
                    salida = true;
                    System.exit(0);
                } 
                //si no al final le asignamor el nombre al string file
                else {
                    file = msj;
                }
                //Creamos una variable de tamaño para saber cuanto pesa total el archivo
                long tamaño = dis.readLong();
                //Apuntamos a donde vamos a guardar el archivo, la direccion a descargar
                bos = new BufferedOutputStream(new FileOutputStream(prueba + "\\downloads\\" + file));
                //entramos a un ciclo donde iremos escribiendo todo el archivo si todo salio bien, y damos a ig el tamaño de lo 
                //tranferido.
                //si no la variable salida se encargara de ni siquiera entrara al while
                while ((in = bis.read(byteArray)) != -1 && salida != true) {
                    //escribir el archivo
                    bos.write(byteArray, 0, in);
                    //sacamos el total de informacion que llevamos guardada
                    rec = in + rec;
                    //Calculamos el porcentaje de lo que llevamos descargado
                    porc = (rec * 100) / tamaño;
                    //un if que siempre se cumpla hasta que porc y aux llegue a ser 100
                    if (aux != porc) {
                        //for para borrar lo escrito en la linea de comando
                        for (long y = 0; y < tama + 10; y++) {
                            System.out.print("\b");
                        }
                        System.out.print("[");
                        //Hacermos una calculo para saber cuanto vamos a pintar de la barra
                        long pintar = (tama * ((100 * rec) / tamaño)) / 100;
                        // Escribimos la barra con el total que llevamos
                        for (long y = 0; y < pintar + 1; y++) {
                            System.out.print("▄");
                        }
                        //Escribimos la barra de lo que aun falta por descargar
                        for (long y = 0; y < tama - pintar; y++) {
                            System.out.print("_");
                        }
                        System.out.print("]");
                        //escribimos el porcentaje que llevmos
                        System.out.print(porc + "%");
                        aux = porc;

                    }
                    //un if en caso de que el porc sea 0 imprima la barra igual pero con un porcentaje de 1%
                    else if (aux == 0) {
                        for (long y = 0; y < tama + 4; y++) {
                            System.out.print("\b");
                        }
                        System.out.print("[");
                        for (long y = 0; y < tama; y++) {
                            System.out.print("_");
                        }
                        System.out.print("]");
                        System.out.print(1 + "%");
                    }
                }
                //cerramos las conexiones de BufferedOutputStream Y DataInputStream
                bos.close();
                dis.close();
            }
        } catch (Exception e) {
            if(e.toString().equals("java.net.SocketException: Connection reset")){
              System.out.println("!!!!! El SERVIDOR SE A IDO");  
            }else{
                System.err.println(e);
                System.out.println("Por motivo: " + e.toString());
            }
        }
    }

}
