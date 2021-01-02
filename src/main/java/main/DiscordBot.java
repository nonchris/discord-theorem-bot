package main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DiscordBot {

    private final String propFileName = "./src/data/config.properties";
    public static void main(String[] args) {
        new DiscordBot();
    }

    private JDA jda;

    public DiscordBot() throws LoginException, IOException {
        InputStream inputStream = new FileInputStream(propFileName);
        System.out.println(inputStream);
        Properties prop = new Properties();
        prop.load(inputStream);



        jda = JDABuilder
                .createDefault((String)prop.get("DISCORD_API_KEY"))
                .build();

        jda.addEventListener(new PingCmd());
        jda.addEventListener(new TheoremDesTages(jda));
    }

}
