package me.byun.bot;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Quiz {
    private static Map<String, Quiz> quizMap = new HashMap<>();
    private String serverId;
    private boolean isQuizStarted;
    private Quiz(String serverId){
        this.serverId = serverId;
        this.isQuizStarted = false;
    }
    public static synchronized Quiz getInstance(String serverId){
        if (!quizMap.containsKey(serverId)) {
            Quiz quiz = new Quiz(serverId);
            quizMap.put(serverId, quiz);
        }
        return quizMap.get(serverId);
    }
    public void startQuiz(String serverId){
        if (!quizMap.containsKey(serverId)) {
            Quiz serverQuiz = new Quiz(serverId);
            quizMap.put(serverId, serverQuiz);
        }
        this.isQuizStarted = true;
    }
    public void endQuiz(String serverId){
        quizMap.remove(serverId);
        this.isQuizStarted = false;
    }
    public boolean checkQuiz(MessageReceivedEvent event){
        MessageChannel channel = event.getChannel();
        if (isQuizStarted) {
            channel.sendMessage("퀴즈 진행중!").queue();
            return true;
        }
        return false;
    }
    public boolean checkQuiz(SlashCommandInteractionEvent event){
        if (isQuizStarted) {
            return true;
        }
        return false;
    }
}
