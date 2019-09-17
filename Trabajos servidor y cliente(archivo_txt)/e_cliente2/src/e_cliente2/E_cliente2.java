/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e_cliente2;

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 *
 * @author ITLM
 */
public class E_cliente2 {

    public static void main(String[] args) throws Exception {
        //Declaracion de variables
        Socket sock = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;
        int contador = 0;
        String datos, descarga;
        String msjEntrada = "";
        Scanner scanner = new Scanner(System.in);
        File file = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        boolean terminar = false;
        try {
            //Intentamos establecer conexion con el servidor 
            sock = new Socket(args[0], Integer.valueOf(args[1]));
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
            //un escritor printwriter para mandarle mensajes al servidor
            escritor = new PrintWriter(
                    sock.getOutputStream(), true
            );
        } catch (IOException e) {
            System.out.println("Error con la salida de datos");
            System.out.println("Por motivo de: " + e.toString());
            System.exit(1);
        }
        try {
            //un lector bufferedReader para poder recibir los msj del servidor
            lector = new BufferedReader(
                    new InputStreamReader(sock.getInputStream())
            );
        } catch (IOException e) {
            System.out.println("Error: al sacar informacion de la conexion");
            System.out.println("Por motivo: " + e.toString());
            System.exit(1);
        }

        // String ruta = "C:/Users/FERNANDO/Desktop/nuevo123.txt";
        //C:/Users/ITLM/Desktop/asd.txt \\d
        while (true && terminar != true) {
            contador++;
            try {
                //pedimos la ruta del archivo por consola y se lo mandamos al servidor
                System.out.println("Ruta del archivo .txt: ");
                datos = scanner.nextLine();
                escritor.println(datos);
            } catch (Exception e) {
                System.out.println("Error: al capturar datos");
                System.out.println("Por motivo: " + e.toString());
                System.exit(1);
            }
            try {
                //lee el msj de entrada del servidor clave para saber si encontro el archivo o no
                msjEntrada = lector.readLine();
                //System.out.println(msjEntrada);
            } catch (IOException e) {
                System.out.println("Error: al imprimir datos del servidor");
                System.out.println("Por motivo: " + e.toString());
                System.exit(1);
            }
            //comprobamos si recibe Escribe1321 el menseja que confirma que si existe el archivo y sigue
            if (msjEntrada.equalsIgnoreCase("Escribe1321")) {
                //Pedimos al cliente donde desea guadar el archivo
                System.out.println("Direccion donde guardar: ");
                descarga = scanner.nextLine();
                //hacemos la instancia con la ruta donde descargar, y luego lo pasamos a un bufferedwrite por medio de filewrite
                //para haci volver a escribir el archivo de texto
                String tipo_archivo = descarga.substring(descarga.indexOf('.')+ 1, descarga.length());
                if(!tipo_archivo.equals("txt")){
                    System.out.println("!!!LA DIRECCION ESTA MAL, O EL ARCHIVO NO ESPECIFICA QUE ES TXT!!!");
                    System.exit(0);
                }
                file = new File(descarga);
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
            } 
            //en caso no haber llegar el mensaje clave 
            //comparamos que el servidor nos mando un mensaje de que el archivo no existe para verificar
            else if (msjEntrada.equalsIgnoreCase("El archivo no existe")) {
                System.out.println(msjEntrada);
                System.exit(0);
            }
           
            //En este ciclo sacamos toda la informacion guardada en el bufferwrite con la informacion del archivo
            //para transcribir el archivo
            while (!(msjEntrada = lector.readLine()).equals("Escribe1321") && contador > 0) {
                if (!msjEntrada.equals("!!!! El mensaje a terminado !!!!!!")) {
                    try {
                        //comprobamos que con el nombre del archivo que quiere guardar exista si no lo crea
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        //aqui escribirmos las linea del texto y hacemos un salto por si se encuenta un salto de linea
                        bw.write(msjEntrada);
                        bw.newLine();
                    } catch (Exception e) {
                        System.out.println("Error: Al crear el archivo en el cliente");
                        System.out.println("Por motivo: " + e.toString());
                    }
                }
                //comprobamos que el servidor nos mande un mensaje de que termino de enviar toda la informacion
                if (msjEntrada.equals("!!!! El mensaje a terminado !!!!!!")) {
                    bw.close();
                    System.out.println("Archivo recibido.........");
                    escritor.println("listo");
                    System.exit(0);
                }
            }
            contador++;
        }
    }

}
