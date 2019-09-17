package e_servidor2;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class E_servidor2 {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        //un do while que siempre mantenga el servidor en servicio
        do {
            //Declaramo e inicializamos las variables a utilizar
            ServerSocket socketServidor = null;
            Socket socket = null;
            BufferedReader lector = null;
            PrintWriter escritor = null;
            String entrada = null;
            Scanner scanner = new Scanner(System.in);
            String salida;
            try {
                //Creamos el servidor y esperamos a que se conecte un cliente
                socketServidor = new ServerSocket(Integer.valueOf(args[0]));
                System.out.println("Servidor a la espera");
                socket = socketServidor.accept();
            } catch (ArrayIndexOutOfBoundsException e) {
                if ((e.toString()).equals("java.lang.ArrayIndexOutOfBoundsException: 0")) {
                    System.out.println("Faltan parametros; no se especifica el puerto");
                }
                System.out.println("Se cerrera el server");
                System.exit(1);
            } catch (NumberFormatException ex) {
                System.out.println("El puerto no es numerico");
                System.out.println("Se cerrera el server por: " + ex.getMessage());
                System.exit(1);
            }

            try {
                //Aqui hacemos uso del bufferReader para poder extraer la informacion del buffer
                lector = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
            } catch (IOException e) {
                System.out.println("Error al extraer informacion en el buffer");
                System.out.println("Por motivo: " + e.toString());
            }
            try {
                //un escritor printwriter que se encargara de enviarle los msj al servidor
                escritor = new PrintWriter(
                        socket.getOutputStream(), true
                );
            } catch (IOException e) {
                System.out.println("Error con la entrada de datos");
                System.out.println("Por motivo: " + e.toString());
            }
            do {
                try {
                    //Sacamos la informacion enviada, con el lector la guardamos en entrada mostramos el mensaje
                    //y comparamos el mensajer clave del cliente 'listo' para cerrar el servidor
                    entrada = lector.readLine();
                    System.out.println("El cliente dice:");
                    System.out.println(entrada);
                    if (entrada.equalsIgnoreCase("listo")) {
                    System.out.println("Me voy");
                    socket.close();
                    socketServidor.close();
                    System.exit(0);
                }
                } catch (SocketException e) {
                    System.out.println("Error al contactar al cliente");
                    System.out.println("Por motivo: " + e.toString());
                    break;
                }
                try {
                    //Mandamos un mensaje por consola para que el servidor escriba y mande el mensaje
                    System.out.println("Escriba un mensaje:");
                    salida = scanner.nextLine();
                    escritor.println(salida);
                } catch (Exception e) {
                    System.out.println("Error al contactar al cliente, en enviar mensaje del servidor");
                    System.out.println("Por motivo: " + e.toString());
                    break;
                }
            } while (!entrada.equalsIgnoreCase("listo"));
            try {
                //Mensaje para notificar al servidor que el cliente se fue
                System.out.println("!!!CLIENTE DESCONECTADO!!!");
                //aqui cerramos la conexion para que cuando se vuelva ejecutar el server no este ocupado la direccion ip y el puerto
                socketServidor.close();
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar la conexión");
            }
        } while (true);
    }

}
