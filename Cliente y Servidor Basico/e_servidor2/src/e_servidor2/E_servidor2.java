package e_servidor2;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class E_servidor2 {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) {
        //un do while que siempre mantenga el servidor en servicio
        do {
            //Declaracion de variables
            ServerSocket socketServidor = null;
            Socket socket = null;
            BufferedReader entrada = null;
            String inf = null;
            try {
                //Creamos un servidor y luego lo ponemos a la espera de que se conecte un cliente
                socketServidor = new ServerSocket(Integer.valueOf(args[0]));
                System.out.println("Servidor a la espera");
                socket = socketServidor.accept();
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
            }

            try {
                //Aqui hacemos uso del bufferReader para poder extraer la informacion del buffer
                entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
            } catch (IOException e) {
                System.out.println("Error con la entrada de datos");
                System.out.println("Por motivo: " + e.toString());
            }
            try {
                //Realizamos un ciclo que siempre mostara el mensaje del cliente,cuando reciba un valor diferente de null
                while ((inf = entrada.readLine()) != null) {
                    System.out.println("me dijieron : " + inf);
                }
            } catch (IOException ex) {
                System.out.println("Error al imprimir el msj");
                System.out.println(ex.toString());
            }
            try {
                //aqui cerramos la conexion para que cuando se vuelva ejecutar el server no este ocupado la direccion ip y el puerto
                socketServidor.close();
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar la conexión");
            }
        } while (true);
    }

}
