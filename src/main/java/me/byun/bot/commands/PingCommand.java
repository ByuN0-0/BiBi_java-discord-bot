package me.byun.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand {
  public void execute(SlashCommandInteractionEvent event) {
    long time = System.currentTimeMillis();
    event.reply("Pong!").setEphemeral(false)
        .flatMap(
            v -> event.getHook().editOriginalFormat("Pong: %d ms",
                System.currentTimeMillis() - time))
        .queue();
  }
}