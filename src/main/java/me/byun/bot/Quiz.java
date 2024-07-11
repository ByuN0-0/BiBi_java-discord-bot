package me.byun.bot;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;

public class Quiz {
    private static final Map<String, Quiz> quizMap = new HashMap<>();
    private static final Map<String, Integer> member = new HashMap<>();
    private static final Queue<String> answerQueue = new LinkedList<>();
    private String answer;
    private boolean isQuizStarted;
    private MessageChannelUnion channel;
    private boolean nextQuiz;

    private Quiz(String serverId){
        this.isQuizStarted = false;
        String userAnswer = null;
        this.nextQuiz = false;
        loadQuiz();
    }
    public static synchronized Quiz getInstance(String serverId){
        if (!quizMap.containsKey(serverId)) {
            Quiz quiz = new Quiz(serverId);
            quizMap.put(serverId, quiz);
        }
        return quizMap.get(serverId);
    }
    private void loadQuiz(){
        String strQuiz = txtReader.readToken("/quiz.txt");
        String[] quiz = strQuiz.split("\n");
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
    public boolean checkQuiz(){
        return isQuizStarted;
    }
    public void setChannel(MessageChannelUnion channel){
        this.channel = channel;
    }
    public String getMember(){
        StringBuilder result = new StringBuilder();
        for(String key : member.keySet()){
            result.append(key).append(" : ").append(member.get(key)).append("\n");
        }
        return result.toString();
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
    public void addAnswerUser(String answerUser){
        answerQueue.add(answerUser);
    }
    public String getAnswerUser(){
        return answerQueue.poll();
    }
    public void answerQueueClear(){
        answerQueue.clear();
    }
    public void quizCycle() {
        String question = "한국의 수도는?";
        this.answer = "서울";
        AtomicInteger tIndex = new AtomicInteger(100); //10초
        AtomicInteger num = new AtomicInteger(1);
        Thread quizThread = new Thread(() -> {
            while (isQuizStarted) {
                int quizNum = num.get();
                int currentIndex = tIndex.getAndAdd(-2); //-0.2초
                try {
                    Thread.sleep(200); //0.2초딜레이
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (currentIndex == 100) { //10초면 문제출제
                    channel.sendMessage(quizNum + ". " + question + " 퀴즈 종료를 원하시면 \"퀴즈 종료\"를 입력해주세요.").queue();
                    answerQueueClear();
                    //answer = 새로운 답;
                    // 문제 출제 & answer 변경
                } else { //10초가 아니면 정답확인
                    if (nextQuiz || currentIndex<=0) {
                        if(nextQuiz) {
                            String user = getAnswerUser();
                            setMember(user);
                            channel.sendMessage(user + "님 정답입니다!\n" + quizNum + "번 정답은 \"" + answer + "\" 입니다.\n다음 문제를 출제합니다.").queue();
                            nextQuiz = false;
                        }else {
                            channel.sendMessage(quizNum +"번 정답은 \"" + answer + "\" 이었습니다.\n다음 문제를 출제합니다.").queue();
                        }
                        num.set(quizNum+1);
                        tIndex.set(100);
                        answerQueueClear();
                        continue;
                    }
                }
                //if (currentIndex % 10 == 0) {
                //    channel.sendMessage("남은 시간: " + currentIndex / 10).queue();
                //}
                if (currentIndex % 10 == 0 && currentIndex <= 30) {
                    channel.sendMessage("남은 시간: " + currentIndex / 10).queue();
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
