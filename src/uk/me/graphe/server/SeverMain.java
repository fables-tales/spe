package uk.me.graphe.server;

import java.util.Scanner;

public class SeverMain {
    public static void main(String[] args) {
        GraphemeServer gs = GraphemeServer.getInstance();
        gs.start();
        Scanner scanner = new Scanner(System.in);
        
        while(true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line.equals("shutdown")) gs.shutDown();
        }
        
    }
    
}
