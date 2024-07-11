package me.byun.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        //String token = txtReader.readFile("src\\main\\java\\me\\byun\\bot\\botToken.txt");
        String token = txtReader.readToken("/botToken.txt");
        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MessageListener())
                .addEventListeners(new SlashBot())
                .setActivity(Activity.playing("현재 초기 개발"))
                .build();// enables explicit access to message.getContentDisplay()
        logger.info("Bot is ready");

        loadCommands(jda.updateCommands());
        logger.info("Commands are loaded");
    }

    // 슬래시 명령어를 추가하기 위해서는 commands.addCommands()를 사용해야 한다.
    // SlashBot 클래스에서 스위치문에 추가해야 한다.
    // SlashBot 클래스에서 함수를 추가해야한다.
    public static void loadCommands(CommandListUpdateAction commands) {
        commands.addCommands(
                Commands.slash("ping", "Calculate ping of the bot"),
                Commands.slash("team", "팀을 나눠줍니다.")
                        .addOptions(
                                new OptionData(OptionType.STRING, "players", "player들을 공백으로 구분해주세요\n ex: person1 person2 person3 ... ")
                                        .setRequired(true)
                        ),
                Commands.slash("lotto", "recommend lotto number"),
                Commands.slash("점메추", "점심 메뉴 추천"),
                Commands.slash("저메추", "저녁 메뉴 추천"),
                Commands.slash("quiz", "퀴즈를 내줍니다."),
                Commands.slash("clear", "채팅을 지워줍니다.")
                        .addOptions(
                                new OptionData(OptionType.INTEGER, "amount", "지울 채팅의 개수를 입력해주세요")
                                        .setRequired(true)
                        )
        ).queue();
    }
}
