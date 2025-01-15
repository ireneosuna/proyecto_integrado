package servidor;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	static final int PUERTO = 7000;
	private ServerSocket serverSocketServidor;

	public Main() {
		try {
			serverSocketServidor = new ServerSocket(PUERTO);
			while (true) {
				Socket socket = serverSocketServidor.accept();
				new Conexion(socket).start();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al lanzar el servidor.");
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
