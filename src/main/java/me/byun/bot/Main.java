package me.byun.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
  private static final Logger logger = LoggerFactory
      .getLogger(Main.class);

  private static final CommandData[] COMMANDS = {
      Commands.slash("ping", "Calculate ping of the bot"),
      Commands.slash("team", "팀을 나눠줍니다.").addOptions(new OptionData(
          OptionType.STRING, "players",
          "player들을 공백으로 구분해주세요\n ex: person1 person2 person3 ... ")
              .setRequired(true)),
      Commands.slash("lotto", "recommend lotto number"),
      Commands.slash("아메추", "아침 메뉴 추천"),
      Commands.slash("점메추", "점심 메뉴 추천"),
      Commands.slash("저메추", "저녁 메뉴 추천"),
      Commands.slash("clear", "채팅을 지워줍니다.")
          .addOptions(new OptionData(OptionType.INTEGER, "amount",
              "지울 채팅의 개수를 입력해주세요").setRequired(true)) };

  public static void main(String[] args) {
    try {
      JDA jda = initializeBot();
      loadCommands(jda.updateCommands());
      logger.info("Bot is ready and commands are loaded");
    } catch (Exception e) {
      logger.error("Bot initialization failed", e);
      System.exit(1);
    }
  }

  private static JDA initializeBot() {
    // 환경 변수 로드
    Dotenv dotenv = Dotenv.load();

    // 환경 변수 사용
    String botToken = dotenv.get("DISCORD_BOT_TOKEN");
    String botStatus = dotenv.get("BOT_STATUS");

    JDA jda = JDABuilder.createDefault(botToken)
        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
        .addEventListeners(new MessageListener())
        .addEventListeners(new SlashBot())
        .setActivity(Activity.playing(botStatus)).build();
    logger.info("Bot is ready");
    return jda;
  }

  public static void loadCommands(CommandListUpdateAction commands) {
    commands.addCommands(COMMANDS).queue();
  }
}
