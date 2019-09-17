/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e_servidor2_archivosgrandes;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ITLM
 */
public class E_servidor2_archivosgrandes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //un do while que siempre ejecute el servidor
        do {
            //Declaracion de variables
            ServerSocket ss = null;
            Socket socket = null;
            BufferedReader lector = null;
            boolean salir = false;
            int in;
            BufferedInputStream bis;
            BufferedOutputStream bos;
            byte[] byteArray;
            DataOutputStream dos = null;
            String archivo = null;
            try {
                //iniciamos el server y quedamos a la espera de un cliente
                ss = new ServerSocket(Integer.valueOf(args[0]));
                System.out.println("Esperando un cliente...");
                socket = ss.accept();
            } catch (IOException e) {
                System.out.println("Error al iniciar el server");
                System.out.println("Por motivo de: " + e.toString());
                System.exit(1);
            } catch (ArrayIndexOutOfBoundsException ex) {
                if ((ex.toString()).equals("java.lang.ArrayIndexOutOfBoundsException: 0")) {
                    System.out.println("Faltan parametros; no se especifica el puerto");
                }
                System.exit(1);
            } catch (NumberFormatException ex2) {
                if ((ex2.toString()).equals("java.lang.NumberFormatException: For input string: \"" + args[0] + "\"")) {
                    System.out.println("El puerto no es numerico");
                }
                System.exit(1);
            } catch (NullPointerException e) {
                System.out.println("No se realizo la conexion con el cliente");

            }
            try {
                //Creamos un lector para recibir mensajes del parte del cliente
                lector = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
            } catch (IOException e) {
                System.out.println("Error al extraer informacion en el buffer");
                System.out.println("Por motivo: " + e.toString());
            }
            //Se ejecutara para comprobar que el cliente mando un mensaje
            do {
                try {
                    //comprobamos que el servidor si envio un mensaje y no sea null
                    if ((archivo = lector.readLine()) != null) {
                        System.out.println("el cliente dice: " + archivo);
                    }
                     salir = true;
                } catch (Exception ex) {
                    System.out.println("no recibio la ruta");
                    salir = true;
                }
            } while (salir != true);
            try {
                //Realizamos otra comprobacion de que el mensajo con el archivo(direccion) no sea null
                if (archivo != null) {
                    //Hacemos un file que apunte al archivo que nos pidio
                    File localFile = new File(archivo);
                    //hacemos la conexion al archivo que nos pidio para mandarlo por por bufferInput al extraerlo con un FileInputStream
                    bis = new BufferedInputStream(new FileInputStream(localFile));
                    //Hacemos la conexion gracias al socket del cliente para poder enviar los byte
                    bos = new BufferedOutputStream(socket.getOutputStream());
                    //Hacemos otra comunicacion para poder enviar mensaje de nombre y tamaño del archivo 
                    dos = new DataOutputStream(socket.getOutputStream());
                    //comprobamos que el archivo existe en caso de que no enviar un mensaje al cliente
                    if (!localFile.exists()) {
                        dos.writeUTF("Archivo no existe");
                    } else if (localFile.exists()) {
                        //un mensaje al servidor que compruebe que existe el archivo
                        System.out.println("!!!!SI EXISTEE!!!");
                    }
                    //Enviamos el nombre del archivo y su tamaño
                    dos.writeUTF(localFile.getName());
                    dos.writeLong(localFile.length());
                    //Un buffer 8192 byte para enviar
                    byteArray = new byte[8192];
                    //un ciclo para donde enviamos el archivo en byte por medio del BufferedOutputStream
                    while ((in = bis.read(byteArray)) != -1) {
                        System.out.print("Enviando archivo... \r");
                        bos.write(byteArray, 0, in);
                    }
                    //al terminar de enviar el archivo decimos que se envio y Cerramos coneziones con los buffers
                    System.out.println("\nArchivo Enviado");
                    bis.close();
                    bos.close();
                }
                else{ 
                    //si llega hacer null el mensaje del cliente es porque no envio una ruta
                    System.out.println("Ruta no especificada...");
                }
            } catch (IOException e) {
                if ((e.toString()).equals("java.io.FileNotFoundException: " + archivo + ""
                        + " (El sistema no puede encontrar el archivo especificado)")) {
                    try {
                        dos = new DataOutputStream(socket.getOutputStream());
                        System.out.println("no entra");
                        dos.writeUTF("Archivo no existe");
                    } catch (IOException ex) {
                        System.out.println("No envia el msj");
                    }
                    System.out.println("!!!EL ARCHIVO NO EXSITE!!!!!");
                }
                if ((e.toString()).equals("java.io.FileNotFoundException: " + archivo + " (Acceso denegado)")) {
                    try {
                        dos = new DataOutputStream(socket.getOutputStream());
                        System.out.println("no entra");
                        dos.writeUTF("Especifique  bien la direccion del archivo con su extension, porfavor");
                    } catch (IOException ex) {
                        System.out.println("No envia el msj");
                    }
                    System.out.println("!!!EL ARCHIVO NO SE PUEDE EJECUTAR!!!!!");
                }
            }
            try {
                socket.close();
                ss.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar la conexión");
            }
        } while (true);

    }

}
