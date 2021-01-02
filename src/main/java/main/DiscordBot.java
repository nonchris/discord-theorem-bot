package main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DiscordBot {

    public static void main(String[] args) {
        new DiscordBot();
    }

    public static Properties config; //holds all properties
    public static String prefix; //bot prefix

    //loading config - holding parameters in config variable
    public void getConfig() throws IOException {
        final String propFileName = "./src/data/config.properties";
        InputStream inputStream = new FileInputStream(propFileName);
        System.out.println(inputStream);
        config = new Properties();
        config.load(inputStream);
    }

    public DiscordBot()  {
        //loading config
        try {
            getConfig();
        }catch(IOException e){
            System.err.println("Unable to load Config file");
            e.printStackTrace();
            System.exit(1);
        }
        prefix = (String)config.get("PREFIX");

        //log into bot account
        JDA jda = null;
        try {
            jda = JDABuilder
                    .createDefault((String)config.get("DISCORD_API_KEY"))
                    .build();
        }catch(LoginException e){
            System.err.println("Unable to login");
            e.printStackTrace();
            System.exit(1);
        }

        //adding VoiceChannelHandler for commands
        jda.addEventListener(new VoiceChannelHandler());
    }

}
