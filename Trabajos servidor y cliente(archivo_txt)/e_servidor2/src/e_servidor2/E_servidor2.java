package e_servidor2;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class E_servidor2 {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) {
        //Un do-while para que simepre se mantenga abierto el servidor
        do {
            //declaracion de variables a utilizar
            ServerSocket ss = null;
            Socket socket = null;
            BufferedReader lector = null;
            String entrada = "";
            char ruta=0;
            boolean sal = false;
            PrintWriter escritor = null;

            try {
                //Creamos el servidor y esperamos que un cliente se conecte
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
                //un lector bufferedReader para que podamos recibir mensajes del cliente
                lector = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
            } catch (IOException e) {
                System.out.println("Error al extraer informacion en el buffer");
                System.out.println("Por motivo: " + e.toString());
            }

            try {
                //un escritor printwriter para poder enviarle mensajes al cliente
                escritor = new PrintWriter(
                        socket.getOutputStream(), true
                );
            } catch (IOException e) {
                System.out.println("Error con la entrada de datos");
                System.out.println("Por motivo: " + e.toString());
            }
            do {
                try {
                    //recibimos el mensaje del cliente donde se supone que deberia enviarnos la ruta del archivo a descargar
                    entrada = lector.readLine();
                    System.out.println("Mensaje del cliente: ");
                    System.out.println(entrada);
                } catch (IOException e) {
                    System.out.println("Error al escribir el mensaje del cliente");
                    System.out.println("Por motivo: " + e.toString());
                    sal = true;
                }
                try {
                 //creamos una serie de variable para comprobar que el archivo enviado por el cliente exista y su extension sea txt
                    File archivo = new File(entrada);
                    String nombre_archivo=archivo.getName();
                    String tipo_archivo = nombre_archivo.substring(nombre_archivo.indexOf('.')+ 1, nombre_archivo.length());
                    //aqui compramos si existe y sea txt para enviarle un msj al cliente de que si esta bien
                    if (archivo.exists() && tipo_archivo.equals("txt")) {
                        System.out.println("!!!!siga todo normal!!!!!!");
                        escritor.println("Escribe1321");
                        //extraemos el primer caracter de la ruta para saber si es un disco C:
                        ruta = entrada.charAt(0);
                    } 
                    //en caso de que no existe o no sea txt, el cliente no 
                    //deberia enviar un listo donde significara que no escribio nada
                    // y le enviamos un mensaje de que el archivo no existe al servidor y cliente
                    else if(!entrada.equals("listo")){
                        escritor.println("El archivo no existe");
                        System.out.println("!!!!EL ARCHIVO NO EXISTE!!!!!!");
                        sal=true;
                    }
                } catch (Exception e) {
                    System.out.println("Error al enviar mensaje de respuesta");
                    System.out.println("Por motivo: " + e.toString());
                    sal = true;
                }
                //aqui comprobamos que el mensaje del cliente sea listo que signifca que termino de enviar el archivo
                if (entrada.equalsIgnoreCase("listo")) {
                    System.out.println("Me voy");
                    try {
                        socket.close();
                        ss.close();
                        sal = true;
                    } catch (IOException ex2) {
                        System.out.println("Error al cerrar el server");
                        sal = true;
                    }
                }

                //comprobamos que el primer caracter sea una c
                if (ruta == 'c' || ruta == 'C' && sal!=true) {
                    //un string cadena para ir guardando los msj que enviara que tiene el archivo
                    String cadena;
                    try {
                        //leemos el archivo por medio de la ruta ya validada
                        FileReader f = new FileReader(entrada);
                        //aqui pasamos los datos al buffer para enviarlos al cliente
                        BufferedReader b = new BufferedReader(f);
                        //un ciclo donde a la cadena le daremos lo que tiene escribo el txt y si no es null enviarlo al cliente
                        while ((cadena = b.readLine()) != null) {
                            //System.out.println("Mandando archivo ");
                            escritor.println(cadena);
                        }
                        //mostrar un mensaje de que termino de enviar y igual enviarle al cliente que ya termino
                        System.out.println("!!!! El mensaje a terminado !!!!!!");
                        escritor.println("!!!! El mensaje a terminado !!!!!!");
                        //cerramos la conexion del bufferReader
                        b.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("No se encuentra el archivo");
                        sal = true;
                    } catch (IOException ex) {
                        System.out.println("Error al recibir el archivo");
                        sal = true;
                    }
                }
                //intentara esto hasta que el cliente mande un mensaje de listo donde finaliza de recibir y que no haya ocurrido
                //un error
            } while (!entrada.equalsIgnoreCase("listo") && sal != true);
            try {
                socket.close();
                ss.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar la conexión");
            }

        } while (true);
    }

}
