/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorchat_asin;

/**
 *
 * @author FERNANDO
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

public class Servidorchat_Asin {

    private static Set<String> usuarios = new HashSet<>();
    private static Set<PrintWriter> writers = new HashSet<>();
    private static HashMap<String, PrintWriter> mapa = new HashMap<String, PrintWriter>();

    public static void main(String[] args) throws Exception {
        System.out.println("El chat esta corriendo... ");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));

            }
        }

    }

    private static class Handler implements Runnable {

        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITNAME");
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (usuarios) {
                        if (!usuarios.contains(name)) {
                            usuarios.add(name);
                            break;

                        }
                    }
                }
                out.println("NAMEACCEPTED " + name);
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + name + " has joined");
                }
                writers.add(out);

                synchronized (mapa) {
                    if (!mapa.containsKey(name)) {
                        mapa.put(name, out);
                    }
                }
                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }
                    if (input.toLowerCase().startsWith("/m")) {
                        System.out.println("manda: " + input + "Lo mando" + name);
                        String mando = input.substring(3);
                        String usuario,msj;
                        int i_m =0;
                        /*for (int x = 0; x < mando.length(); x++) {
                            if (!Character.toString(mando.charAt(x)).equals(" ")) {
                                usuario = usuario + Character.toString(mando.charAt(x));
                                System.out.println("Nombre del usuario: " + usuario);
                                i_m=x;
                            } else {
                                for(int y = i_m; y < mando.length(); y++){
                                    System.out.println("si entro: "+y);
                                    msj= msj+Character.toString(mando.charAt(x));
                                }
                                System.out.println("si hay espacio");
                                break;
                            }
                        }*/
                        int espacio= input.substring(3).indexOf(" ");
                        usuario =  input.substring(3, espacio + 3);
                        msj =  input.substring(3).substring(espacio + 1);
                        
                        System.out.println("Nombre: " + mando);
                        System.out.println("Nombre del usuario: " + usuario);
                         System.out.println("Mensaje: " + msj);
                        //mapa.containsKey(null);
                        System.out.println(mapa.containsKey(null));
                        if(mapa.containsKey(usuario)){
                          mapa.get(usuario).println("MESSAGE " + name + "(mensaje privado): " + msj);
                          mapa.get(name).println("MESSAGE " + name + "(mensaje privado): " + msj);
                        }
                        /*for (Map.Entry<String,PrintWriter> e : mapa.entrySet()) {
                            System.out.println("Usuario: " + e.getKey() + "- Y su escritor: " + e.getValue());
                            if (e.getKey().equals(usuario)) {
                                e.getValue().println("MESSAGE " + name + ": " + input);
                               
                            }
                        }*/

                    }
                    else{
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                        //System.out.println("Usuarios: " + usuarios);
                        //System.out.println("Escritories: " + writers);
                    
                            }
                }    
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    writers.remove(out);
                }
                if (name != null) {
                    System.out.println("El usuario: "+name+" se a ido");
                    usuarios.remove(name);
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + " se a ido");
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

    }

}
