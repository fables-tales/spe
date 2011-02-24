package uk.me.graphe.server;

import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        GraphemeServer gs = GraphemeServer.getInstance();
        gs.start();
        Scanner scanner = new Scanner(System.in);
        
        while(true) {
            
        }
        
    }
    
}
