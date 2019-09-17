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

    public static void main(String[] args) {
        //Declaracion de variables
        Socket sock = null;
        PrintWriter escritor = null;
        Scanner scanner = new Scanner(System.in);
        // try-catch para comprobar la conexion con el servidor
        try {
            //Realiza la conexcion con el servidor
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
            //se establece un escritor para que pueda mandar mensaje por la conexion que se establezca
            escritor = new PrintWriter(
                    sock.getOutputStream(), true
            );
        } catch (IOException e) {
            System.out.println("Error con la salida de datos");
            System.out.println("Por motivo de: " + e.toString());
            System.exit(1);
        }
        try {
            //le pedimos a al cliente que escriba un msj y lo enviamos con el escritor para luego cerrar la conexion
            System.out.println("Escriba un mensaje");
            String msj = scanner.nextLine();
            escritor.println(msj);
            sock.close();
        } catch (IOException e) {
            System.out.println("!!!Error al Enviar el mensaje al cliente!!!");
        }
    }

}
