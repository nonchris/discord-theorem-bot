package main.utils;

import main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.awt.*;


/**
 * A wrapper of JDAs EmbedBuilder contains some 'default' embed-styles
 */
@SuppressWarnings("unused")
public class EmbedFactory {

    public static EmbedFactory ERROR(){
        return new EmbedFactory("Error",
                "An unknown error occurred, please inform the maintainers!", new Color(164, 0, 0));
        }

    public static EmbedFactory PERMISSION_BOT(){
        return new EmbedFactory("I can't do that", "I might need some extra permissions for this...", new Color(231, 1, 1));
    }

    public static EmbedFactory PERMISSION_USER(){
        return new EmbedFactory("Nope.", "You're not authorized to do that, I'm sorry", new Color(230, 146, 0));
    }

    public static EmbedFactory WRONG_COMMAND(){
        return new EmbedFactory("That's strange...",
                "I don't recognize that command. \nDid you enter a valid command? \nMore information: " + DiscordBot.prefix + "help`",
                new Color(230, 146, 0));
    }

    public static EmbedFactory WRONG_PARAMETER(){
        return new EmbedFactory("Hmmmmm...",
                "I don't understand that parameter - Are you sure it's right? \nMore information: `" + DiscordBot.prefix + "help`",
                new Color(230, 146, 0));
    }

    public static EmbedFactory SUCCESS(){
        return new EmbedFactory("Success", "Executed command", new Color(31, 188, 0));
    }

    public static EmbedFactory MESSAGE(){
        return new EmbedFactory("\u200B", "\u200B", new Color(3, 192, 202));
    }

    private final EmbedBuilder eb;

    public EmbedFactory(String title, String message, Color color) {
        eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setDescription(message);
        eb.setColor(color);
    }

    //functions for overwriting default values
    public EmbedFactory setTitle(String title){
        eb.setTitle(title);
        return this;
    }

    public EmbedFactory setMessage(String message){
        eb.setDescription(message);
        return this;
    }

    public EmbedFactory setColor(Color color){
        eb. setColor(color);
        return this;
    }

    public EmbedFactory setFooter(String text){
        eb.setFooter(text);
        return this;
    }

    //function to send final embed to channel
    public void dispatch(TextChannel channel) {
        try {
            channel.sendMessage(eb.build()).queue();
        }catch (PermissionException e){
            //TODO: Logging
            System.out.println("Failed to embed message to: " + channel.getName()
                    + " on server: " + channel.getGuild().getName());
            channel.sendMessage("Something went wrong, maybe I can't send embeds?").queue();
        }
    }

}
