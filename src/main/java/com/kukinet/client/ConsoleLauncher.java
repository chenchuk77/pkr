package com.kukinet.client;


import java.lang.reflect.Constructor;
import java.net.URI;

public class ConsoleLauncher{

    // global. used by client to recognize whoami
//    public static String USERNAME;

    public static void main( String[] args ) throws Exception {
        String command  = args[0].split(",")[0];
//        String username = args[0].split(",")[1];
        String USERNAME = args[0].split(",")[1];
        String password = args[0].split(",")[2];
        String gameName = args[0].split(",")[3];

        // default to HumanPlayer if no PLAYER_CLASS passed by the environment
        String playerClass = System.getenv("PLAYER_CLASS");
        if (playerClass == null) playerClass = "com.kukinet.client.profiles.HumanPlayer";
        Class<?> clazz = Class.forName(playerClass);
        Constructor<?> constructor = clazz.getConstructor(URI.class, String.class);
        //System.out.println("USERNAMEEEE=: " + USERNAME);

        Object instance = constructor.newInstance(new URI( "ws://52.17.43.16:4444" ), USERNAME);
        ConsoleClient c = (ConsoleClient) instance;
        System.out.println("starting a new player from class: " + c.getClass().getName());


//        Object c = playeClazz( new URI( "ws://localhost:4444" )).; // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
//        ConsoleClient c = new ConsoleClient( new URI( "ws://localhost:4444" )); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
        c.setConnectionLostTimeout(0);
        c.connect();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c.send("login" + "," + USERNAME + "," + password);

        // register to testgame if 'testGame' keyword specified
        if (gameName.equals("testGame")){
            c.send("register" + "," + USERNAME + ",testGame");

        }
        if (gameName.equals("noop")){
            c.send("statusrequest");
        }

    }
}
