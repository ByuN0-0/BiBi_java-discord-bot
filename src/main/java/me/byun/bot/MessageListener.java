package me.byun.bot;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MessageListener extends ListenerAdapter
{
    private static final Queue<String> answerQueue = new LinkedList<>();
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        String serverId = event.getGuild().getId();
        MessageChannelUnion channel = event.getChannel();
        String channelId = channel.getId();
        String messageContent = event.getMessage().getContentDisplay();
        String userName = event.getAuthor().getName();
        Quiz quiz = Quiz.getInstance(serverId);

        if (event.getAuthor().isBot()) return;
        if (event.isFromType(ChannelType.PRIVATE)) {
            System.out.printf("개인채널 [PM] %s: %s\n", event.getAuthor().getName(),
                    event.getMessage().getContentDisplay());
        }
        else {
            System.out.printf("[Server: %s, Channel: %s] (User) %s: %s\n", event.getGuild().getName(),
                    channel.getName(), Objects.requireNonNull(event.getMember()).getEffectiveName(),
                    event.getMessage().getContentDisplay());
        }

        if (quiz.checkQuiz()){ //퀴즈가 진행중이면
            //Todo: 퀴즈 진행 여기서 퀴즈가 정답인지 체크
            //channel.sendMessage("퀴즈 중").queue();
            if(quiz.checkAnswer(messageContent)&&!quiz.getNextQuiz()){ //정답이고 퀴즈 진행중이면
                quiz.addAnswerUser(userName);
                quiz.setNextQuiz(true);
                //channel.sendMessage(event.getAuthor().getName()+"님 정답입니다!").queue();
                return;
            }
            if(messageContent.equals("퀴즈끝")){ //퀴즈 종료
                quiz.endQuiz(serverId);
                event.getChannel().sendMessage("퀴즈종료").queue();
                return;
            }
        }
        else {
            replyEventMessage(event, messageContent);
        }
    }
    public void addAnswerUser(String answerUser){
        answerQueue.add(answerUser);
    }
    public String getAnswerUser(){
        return answerQueue.poll();
    }
    public void answerQueueClear(){
        answerQueue.clear();
    }
    public void replyEventMessage(MessageReceivedEvent event, @NotNull String msg){
        if(msg.equals("조서연멍청이")) {
            event.getChannel().sendMessage("인졍!").queue();
        }
        else if(msg.equals("빠이")) {
            event.getChannel().sendMessage("바바잉!").queue();
        }
    }
}