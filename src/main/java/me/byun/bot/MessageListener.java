package me.byun.bot;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.isFromType(ChannelType.PRIVATE))
        {
            System.out.printf("개인채널 [PM] %s: %s\n", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay());
        }
        else
        {
            System.out.printf("[Server: %s, Channel: %s] (User) %s: %s\n", event.getGuild().getName(),
                    event.getChannel().getName(), event.getMember().getEffectiveName(),
                    event.getMessage().getContentDisplay());
        }
        if (event.getAuthor().isBot()) return;


        String userMessage = event.getMessage().getContentDisplay();
        if(userMessage.equals("조서연멍청이")) {
            event.getChannel().sendMessage("인졍!").queue();
        }
        if(userMessage.equals("빠이")) {
            event.getChannel().sendMessage("바바잉!").queue();
        }

    }
}