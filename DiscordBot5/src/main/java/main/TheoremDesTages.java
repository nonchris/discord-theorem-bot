package main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class TheoremDesTages extends ListenerAdapter {

    private JDA jda;
    private Timer timer;
    private TimerTask timerTask;
    private MessageChannel channel;
    private boolean started;

    public TheoremDesTages(JDA jda) {
        this.jda = jda;
        started = false;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getAuthor().isBot()) return;

        String content = event.getMessage().getContentRaw();
        if(content.equalsIgnoreCase("!TheoremDesTages") && !started){
            setupTimer();
            channel = event.getChannel();
            timer.scheduleAtFixedRate(timerTask, 500, 1000);
            jda.addEventListener(new TheoremDesTages(jda));
            started = true;
        }else if(content.equalsIgnoreCase("!stop") && event.getChannel() == channel){
            timer.cancel();
            jda.removeEventListener(this);
        }
    }

    private void setupTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                sendTheorem();
            }
        };

        timer = new Timer();
    }

    private void sendTheorem() {
        channel.sendMessage("Test").queue();
    }
}
