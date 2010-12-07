package gui;

import core.controller.ProtocolController;
import core.model.Game;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("What do you want to do?");
		System.out.println("1- Become Server 2- Become Client 3-Quit");
		ProtocolController controller = new ProtocolController();
		GUIController selfcontroller = new GUIController(controller);
		int ans;
		do {
			ans = scan.nextInt();
		} while(ans < 1 || ans > 3);
		switch (ans) {
		case 1:
			System.out.println("Port to use?");
			int port = scan.nextInt();
			controller.createGame(new Game(2, 10, 100, 10, .5f), port);
			System.out.println("What is your name?");
			String name = scan.next();
			controller.login(selfcontroller, name, "localhost", port, port);
			System.out.println("Asking to become a player...");
			controller.becomePlayer();
			break;
		case 2:
			System.out.println("Please enter: your name, the server's host, the server's port and your port.");
			controller.login(selfcontroller, scan.next(), scan.next(), scan.nextInt(), scan.nextInt());
			System.out.println("Asking to become a player...");
			controller.becomePlayer();
			break;
		case 3:
			System.exit(0);
		}
	}
}
