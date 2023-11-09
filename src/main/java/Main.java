import java.io.*;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Leer los comandos por consola
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el primer comando:");
        String comando1 = sc.nextLine();
        System.out.println("Introduce el segundo comando:");
        String comando2 = sc.nextLine();

        // Crear los procesos
        ProcessBuilder pb1 = new ProcessBuilder("/bin/bash", "-c", comando1);
        ProcessBuilder pb2 = new ProcessBuilder("/bin/bash", "-c", comando2);
        Process p1 = pb1.start();
        Process p2 = pb2.start();

        // Crear los canales de comunicación
        InputStream is1 = p1.getInputStream();
        OutputStream os2 = p2.getOutputStream();

        // Copiar la salida del primer proceso a la entrada del segundo
        byte[] buffer = new byte[1024];
        int bytesLeidos;
        while ((bytesLeidos = is1.read(buffer)) != -1) {
            os2.write(buffer, 0, bytesLeidos);
        }

        // Cerrar la entrada del segundo proceso para indicar que se ha terminado de escribir
        os2.close();

        // Capturar la salida del segundo proceso
        InputStream is2 = p2.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is2));
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }

        // Esperar a que terminen los procesos
        p1.waitFor();
        p2.waitFor();

        // Comprobar si el primer proceso se ha cerrado correctamente
        if (p1.exitValue() != 0) {
            System.out.println("El primer proceso ha terminado con un error.");
        }

        // Comprobar si el segundo proceso se ha cerrado correctamente
        if (p2.exitValue() != 0) {
            System.out.println("El segundo proceso ha terminado con un error.");
        }
    }
}
