package me.byun.bot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.Objects;

public class ClearCommand {
  private static final int MAX_CLEAR_AMOUNT = 20;

  public void execute(SlashCommandInteractionEvent event) {
    int amount = Objects.requireNonNull(event.getOption("amount"))
        .getAsInt();

    if (amount > MAX_CLEAR_AMOUNT) {
      event.reply(MAX_CLEAR_AMOUNT + "개 이상의 채팅을 삭제할 수 없습니다.")
          .setEphemeral(true).queue();
      return;
    }

    Member selfMember = Objects.requireNonNull(event.getGuild())
        .getSelfMember();
    if (!selfMember.hasPermission((GuildChannel) event.getChannel(),
        Permission.MESSAGE_MANAGE)) {
      event.reply("채팅을 삭제할 권한이 없습니다.").setEphemeral(true).queue();
      return;
    }

    deleteMessages(event, amount);
  }

  private void deleteMessages(SlashCommandInteractionEvent event,
      int amount) {
    event.getChannel().getHistory().retrievePast(amount)
        .queue(messages -> {
          messages.forEach(message -> message.delete().queue());
          event.reply("채팅을 " + amount + "개 삭제했습니다.")
              .setEphemeral(false).queue();
        });
  }
}