package me.byun.bot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import me.byun.bot.commands.*;

public class SlashBot extends ListenerAdapter {
  private final PingCommand pingCommand;
  private final LottoCommand lottoCommand;
  private final TeamCommand teamCommand;
  private final ClearCommand clearCommand;
  private final FoodCommand foodCommand;
  private final FoodCategoryCommand foodCategoryCommand;

  public SlashBot() {
    this.pingCommand = new PingCommand();
    this.lottoCommand = new LottoCommand();
    this.teamCommand = new TeamCommand();
    this.clearCommand = new ClearCommand();
    this.foodCommand = new FoodCommand();
    this.foodCategoryCommand = new FoodCategoryCommand();
  }

  @Override
  public void onSlashCommandInteraction(
      SlashCommandInteractionEvent event) {
    try {
      switch (event.getName()) {
      case "ping" -> pingCommand.execute(event);
      case "team" -> teamCommand.execute(event);
      case "lotto" -> lottoCommand.execute(event);
      case "점메추", "저메추", "아메추" -> foodCommand.execute(event);
      case "한식", "중식", "일식", "양식" -> foodCategoryCommand
          .execute(event);
      case "clear" -> clearCommand.execute(event);
      default -> event
          .reply("I can't handle that command right now :(")
          .setEphemeral(true).queue();
      }
    } catch (Exception e) {
      event.reply("명령어 실행 중 오류가 발생했습니다: " + e.getMessage())
          .setEphemeral(true).queue();
    }
  }
}
