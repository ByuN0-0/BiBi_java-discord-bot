package me.byun.bot;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class MessageListener extends ListenerAdapter
{
    private static Map<String, Boolean> quizChannelStatus = new HashMap<>();
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        MessageChannel channel = event.getChannel();
        String channelId = channel.getId();
        String messageContent = event.getMessage().getContentDisplay();
        if (event.isFromType(ChannelType.PRIVATE)) {
            System.out.printf("개인채널 [PM] %s: %s\n", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay());
        }
        else {
            System.out.printf("[Server: %s, Channel: %s] (User) %s: %s\n", event.getGuild().getName(),
                    channel.getName(), event.getMember().getEffectiveName(),
                    event.getMessage().getContentDisplay());
        }
        if (event.getAuthor().isBot()) return;
        if (quizChannelStatus.containsKey(channelId) && quizChannelStatus.get(channelId)) {
            if (messageContent.equals("퀴즈 끝")) {
                quizChannelStatus.put(channelId, false);
                channel.sendMessage("퀴즈가 종료되었습니다!").queue();
                return;
            }
            channel.sendMessage("퀴즈 진행중!").queue();
            return;
        }
        replyEventMessage(event, messageContent);
    }
    public void replyEventMessage(MessageReceivedEvent event, String msg){
        if(msg.equals("조서연멍청이")) {
            event.getChannel().sendMessage("인졍!").queue();
            return;
        }
        else if(msg.equals("빠이")) {
            event.getChannel().sendMessage("바바잉!").queue();
            return;
        }
    }
    public static void activateQuizCommand(SlashCommandInteractionEvent event) {
        MessageChannel channel = event.getChannel();
        String channelId = event.getChannel().getId();
        quizChannelStatus.put(channelId, true);
        channel.sendMessage("-----퀴즈 진행 중-----").queue();
    }
}