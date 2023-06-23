package me.byun.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class Main {
    public static void main(String[] args){
        //String token = txtReader.readFile("src\\main\\java\\me\\byun\\bot\\botToken.txt");
        String token = txtReader.readToken("/botToken.txt");
        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MessageListener())
                .addEventListeners(new SlashBot())
                .setActivity(Activity.playing("현재 초기 개발 중"))
                .build();// enables explicit access to message.getContentDisplay()
        System.out.println("Execute normaly");
        CommandListUpdateAction commands = jda.updateCommands();
        loadCommands(commands);
        System.out.println("set Commands complete");
    }
    public static void loadCommands(CommandListUpdateAction commands){
        commands.addCommands(
                Commands.slash("ping","Calculate ping of the bot")
        );
        commands.addCommands(
                Commands.slash("team", "팀을 나눠줍니다.")
                        .addOptions(
                                new OptionData(OptionType.STRING,"players","player들을 공백으로 구분해주세요\n ex: person1 person2 person3 ... ")
                                        .setRequired(true))
        );
        commands.addCommands(
                Commands.slash("lotto","recommend lotto number")
        );
        commands.addCommands(
                Commands.slash("점메추", "점심 메뉴 추천")
        );
        commands.addCommands(
                Commands.slash("저메추", "저녁 메뉴 추천")
        );
        commands.queue();
    }
}
