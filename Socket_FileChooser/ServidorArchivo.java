package smultarchivos;

import java.net.*;
import java.io.*;
import static java.lang.Thread.sleep;

public class ServidorArchivo {

    public static void main(String args[]) throws IOException {
        //Se crea el socket del servidor en el puerto 7001
        ServerSocket ss = new ServerSocket(7001);
        
        //El server se queda escuchando
        while(true){
            Socket cl; //Se crea un socket para cada una de las conexiones
                
            try {
                cl = ss.accept(); //Se acepta la conexion del cliente
                
                //Se imprime la ingormacion del cliente
                System.out.println("Cliente conectado desde: "+cl.getInetAddress()+", "+cl.getPort());
                //Se asocia el glujo de datos del cliente a undis
                DataInputStream dis = new DataInputStream(cl.getInputStream());               
                //Se ele el nombre del archivo
                String fileName = dis.readUTF();
                //Se obtiene la longitud en bytes del archivo
                long dataLong = dis.readLong();
                //Se crea un buffer de lectura
                byte[] buf = new byte[1024];
                //Contador de bytes leidos por iteracion
                int leidos = 0;
                //Contador total de bytes leidos
                int recibidos = 0;
                
                //Se asocia un salida para el archivo
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName));
               
                //Se le buffer de entrada y se guaran los byte en buf mientras que en leidos se almacena el numero de bytes leidos
                while((leidos=dis.read(buf))!=-1){
                    //Se escriben los bytes del buf desde el byte 0 al ultimo byte leido
                    dos.write(buf, 0, leidos);
                    //Se limpia el buffer de salida
                    dos.flush();
                    //Se cuenta el total de bytes leidos
                    recibidos += leidos;
                }
                //Se cierran los recursos
               dis.close();
               dos.close();    
               cl.close();
               } catch (IOException ex) {
               }
            
       }
        
    }
}
