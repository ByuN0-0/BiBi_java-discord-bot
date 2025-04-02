package me.byun.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.*;

public class TeamCommand {
  public void execute(SlashCommandInteractionEvent event) {
    String playersStr = Objects
        .requireNonNull(event.getOption("players")).getAsString();
    String[] players = playersStr.split(" ");

    if (players.length % 2 != 0) {
      event.reply(playersStr + "\n짝수로 고쳐주십사와요~\n").queue();
      return;
    }

    List<String> playerList = new ArrayList<>(Arrays.asList(players));
    Collections.shuffle(playerList);

    List<String> team1 = new ArrayList<>();
    List<String> team2 = new ArrayList<>();

    for (int i = 0; i < playerList.size(); i++) {
      if (i % 2 == 0) {
        team1.add(playerList.get(i));
      } else {
        team2.add(playerList.get(i));
      }
    }

    String result = String.format("입력: %s\nteam 1: %s\nteam 2: %s",
        playersStr, String.join(", ", team1),
        String.join(", ", team2));

    event.reply(result).setEphemeral(false).queue();
  }
}