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
        //Declaracion de variable
        Socket sock = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;
        String datos;
        String msjEntrada;
        Scanner scanner = new Scanner(System.in);
        try {
            //Intentamos realizar la conexion con el servidor
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
            //un escritor printwriter que se encargara de enviarle los msj al servidor
            escritor = new PrintWriter(
                    sock.getOutputStream(), true
            );
        } catch (IOException e) {
            System.out.println("Error con la salida de datos");
            System.out.println("Por motivo de: " + e.toString());
            System.exit(1);
        }
        try {
            //un lector para que bufferReader que ayude a imprimir los mensaje que nos mande el servidor
            lector = new BufferedReader(
                    new InputStreamReader(sock.getInputStream())
            );
        } catch (Exception e) {
            System.out.println("Error: al sacar informacion de la conexion");
            System.out.println("Por motivo: " + e.toString());
            System.exit(1);
        }
        //usamos un ciclo infinito para que siempre mantenga el chat al menos que el cliente o servidor se desconecten
        while (true) {
            try {
                //capturamos un msj del cliente por medio de consola y lo mandamos al servidor
                System.out.println("Escriba un mensaje:");
                datos = scanner.nextLine();
                escritor.println(datos);
            } catch (Exception e) {
                System.out.println("Error: al capturar datos");
                System.out.println("Por motivo: " + e.toString());
                System.exit(1);
            }
            try {
                //guardamos el mensaje recibido y lo mostamos en pantalla
                msjEntrada = lector.readLine();
                System.out.println("El servidor dice:");
                System.out.println(msjEntrada);
            } catch (IOException e) {
                System.out.println("Error: al imprimir datos");
                System.out.println("Por motivo: " + e.toString());
                System.exit(1);
            }

        }
    }

}
