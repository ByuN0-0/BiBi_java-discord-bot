package me.byun.bot;

import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Quiz {
    private static Map<String, Quiz> quizMap = new HashMap<>();
    private static Map<String, Integer> member = new HashMap<>();
    private String serverId;
    private String userAnswer;
    private String answer;
    private String previousAnswer;
    private String lastUser;
    private boolean isQuizStarted;
    private MessageChannelUnion channel;
    private boolean nextQuiz;

    private Quiz(String serverId){
        this.serverId = serverId;
        this.isQuizStarted = false;
        this.userAnswer = null;
        this.previousAnswer = null;
        this.lastUser = null;
        this.nextQuiz = false;
    }
    public static synchronized Quiz getInstance(String serverId){
        if (!quizMap.containsKey(serverId)) {
            Quiz quiz = new Quiz(serverId);
            quizMap.put(serverId, quiz);
        }
        return quizMap.get(serverId);
    }
    public void startQuiz(String serverId, MessageChannelUnion channel){
        this.channel = channel;
        if (!quizMap.containsKey(serverId)) {
            Quiz serverQuiz = new Quiz(serverId);
            quizMap.put(serverId, serverQuiz);
        }
        this.isQuizStarted = true;
        quizCycle();
    }
    public void endQuiz(String serverId){
        quizMap.remove(serverId);
        this.isQuizStarted = false;
    }
    public boolean checkAnswer(String answer){
        return this.answer.equalsIgnoreCase(answer);
    }
    /*public CompletableFuture<Boolean> checkAnswer(String answer) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        boolean isCorrect = false;
        // 정답 확인 로직 수행
        if(answer.equalsIgnoreCase(userAnswer)) {
            isCorrect = true; // 정답 확인 결과
        }

        // 결과를 CompletableFuture에 설정
        future.complete(isCorrect);

        return future;
    }*/
    public boolean checkQuiz(){
        return isQuizStarted;
    }
    public void setChannel(MessageChannelUnion channel){
        this.channel = channel;
    }
    public String getMember(){
        String result = "";
        for(String key : member.keySet()){
            result += key + " : " + member.get(key) + "\n";
        }
        return result;
    }
    public void setNextQuiz(boolean bool){
        this.nextQuiz = bool;
    }
    public boolean getNextQuiz(){
        return this.nextQuiz;
    }
    public void setMember(String userName){
        if(!member.containsKey(userName)){
            member.put(userName, 1);
            return;
        }
        int score = member.get(userName);
        member.put(userName, score+1);
    }
    public void quizCycle() {
        String question = "한국의 수도는?";
        this.answer = "서울";
        AtomicInteger tIndex = new AtomicInteger(100); //10초

        Thread quizThread = new Thread(() -> {
            while (isQuizStarted) {
                int currentIndex = tIndex.getAndAdd(-2); //-0.1초
                if (currentIndex == 100) { //10초면 문제출제
                    channel.sendMessage(question).queue();
                    //answer = 새로운 답;
                    // 문제 출제 & answer 변경
                } else { //10초가 아니면 정답확인
                    if (nextQuiz || currentIndex<=0) {
                        channel.sendMessage("정답은 \"" + answer + "\" 였습니다.\n다음 문제를 출제합니다.").queue();
                        nextQuiz = false;
                        tIndex.set(200);
                        continue;
                    }
                }
                if (currentIndex % 10 == 0 && currentIndex <= 30) {
                    channel.sendMessage("남은 시간: " + currentIndex / 10).queue();
                }
                try {
                    Thread.sleep(10); //0.2초딜레이
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        quizThread.start();
    }
    /*public void quizCycle() {
        String question = "한국의 수도는?";
        String answer = "서울";
        AtomicInteger tIndex = new AtomicInteger(100);

        Thread quizThread = new Thread(() -> {
            while (isQuizStarted) {
                int currentIndex = tIndex.getAndAdd(-1);
                if (currentIndex <= 0) {
                    tIndex.set(100);
                }
                channel.sendMessage("남은 시간: " + currentIndex ).queue();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (currentIndex == 100) {
                    channel.sendMessage(question).queue();
                    userAnswer = null;
                    previousAnswer = null;
                    // 문제 출제 & answer 변경
                }
                if (!Objects.equals(userAnswer, previousAnswer)) {
                    if (userAnswer != null && userAnswer.equalsIgnoreCase(answer)) {
                        channel.sendMessage(lastUser + "님 정답").queue();
                        continue;
                    }
                    previousAnswer = userAnswer;
                }
            }
        });
        quizThread.start();
    }*/
    /*public void quizCycle() {
        String question = "한국의 수도는?";
        String answer = "서울";
        AtomicInteger tIndex = new AtomicInteger(100);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            int currentIndex = tIndex.getAndAdd(-5);
            if (currentIndex % 10 == 0) {
                channel.sendMessage("남은 시간: " + currentIndex / 10 + "초").queue();
            }
            if (currentIndex == 100) {
                channel.sendMessage(question).queue();
                userAnswer = null;
                previousAnswer = null;
                // 문제 출제 & answer 변경
            }
            if (!Objects.equals(userAnswer, previousAnswer)) {
                if (userAnswer != null && userAnswer.equalsIgnoreCase(answer)) {
                    channel.sendMessage(lastUser + "님 정답").queue();
                }
                previousAnswer = userAnswer;
            }
            if (!isQuizStarted) {
                executor.shutdown();
            }
            if (currentIndex <= 0) {
                tIndex.set(100);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);  // 0.5초마다 userAnswer를 확인하도록 설정
    }*/
    /*public void quizCycle(){
        String question = "한국의 수도는?";
        String answer = "서울";
        AtomicInteger tIndex = new AtomicInteger(100);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int currentIndex = tIndex.getAndAdd(-5);
                if(currentIndex%10==0){
                    channel.sendMessage("남은 시간: "+currentIndex/10+"초").queue();
                }
                if(currentIndex==100){
                    channel.sendMessage(question).queue();
                    userAnswer = null;
                    previousAnswer = null;
                    //answer = newAnswer;
                    //문제 출제 & answer 변경
                }
                if (!Objects.equals(userAnswer, previousAnswer)) {
                    if (userAnswer != null && userAnswer.equalsIgnoreCase(answer)) {
                        channel.sendMessage(lastUser+"님 정답").queue();
                    }
                    previousAnswer = userAnswer;
                }
                if(!isQuizStarted){ cancel(); }
                if(currentIndex<=0){
                    tIndex.set(100);
                }
            }
        }, 0, 500);  // 0.5초마다 userAnswer를 확인하도록 설정
    }*/
}
