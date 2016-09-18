package smultarchivos;

import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;
import static java.lang.Thread.sleep;

public class ClienteArchivo {

    public static void main(String[] args) {
        // TODO code application logic here
        try {
            //Se establece el host y puerto de la conexion
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Dirección del servidor: ");
            String host = "127.0.0.1";
            System.out.println(host);

            System.out.println("Puerto: ");
            int pto = 7001;
            System.out.println(pto);

            //Se crea un arreglo para almacenar lso archivos
            File[] f = null;
            //Almacena el resultado de abrir el file chooser exito | fracaso
            int resultado;
            //Se crea un file chooser
            JFileChooser fileChooser;
        
            //Se iniciliza el file chooser    
            fileChooser = new JFileChooser();
            //Se establece que el filechooser pueda seleccionar varios archivos
            fileChooser.setMultiSelectionEnabled(true);
            //Se abre el file chooser y se guara el estado
            resultado = fileChooser.showOpenDialog(null);

            //Si el file chooser se abri correctamente
            if(resultado==JFileChooser.APPROVE_OPTION){
               //Se guarda en el arrelo de archivos los documento seleccionados 
               f = fileChooser.getSelectedFiles();
               //Se recorre el arreglo, es un foreach
               for(File file:f){
                    System.out.println("\nEnviando archivo: "+file.getAbsolutePath());
                    //Se crea e socket de conexion
                    Socket cl = new Socket(host,pto);
                
                    //Se asocia la salida al socket
                    DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                    //Se asocia la entrada desde el doxumetno actual
                    DataInputStream dis = new DataInputStream(new FileInputStream(file.getAbsolutePath()));
                   
                    //Se escribe en el socket el nombre del archivo
                    dos.writeUTF(file.getName());
                    //Se escribe la longitud del archivo
                    dos.writeLong(file.length());
                    //Se estable buffer de lectura
                    byte[] buf = new byte[1024];
                    int leidos=0;
                    int b_leidos;
                    //Se establece el tamaño del bloque a enviar, si los datos a enviar son mayor a 1024 solo se envian 1024 bytes si es menor se envia el tamaño dispinible de bytes
                    int tam_bloque=(dis.available()>=1024)? 1024 :dis.available();
                    //Se establece el numero de bytes del archivo
                    int tam_arch = dis.available();
                    
                    //Se recorre los bytes del archivo
                    while((b_leidos=dis.read(buf,0,buf.length))!=-1){
                            //Se escriben los bytes
                            dos.write(buf,0,b_leidos);
                            //Se limpia el flujo
                            dos.flush();
                            //Se lleva un conteo de bytes envdos
                            leidos += tam_bloque;
                            tam_bloque=(dis.available()>=1024)? 1024 :dis.available();
                    }//while
                    dis.close();
                    dos.close();                
                    }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

