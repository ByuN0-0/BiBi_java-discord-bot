package me.byun.bot;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MessageListener extends ListenerAdapter {
  private static final Logger logger = LoggerFactory
      .getLogger(Main.class);
  private static final Map<String, String> RESPONSE_MESSAGES = new HashMap<>();

  static {
    RESPONSE_MESSAGES.put("빠이", "바바잉!");
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getAuthor().isBot())
      return;

    logMessage(event);
    handleMessage(event);
  }

  private void logMessage(MessageReceivedEvent event) {
    String messageContent = event.getMessage().getContentDisplay();
    String authorName = event.getAuthor().getName();

    if (event.isFromType(ChannelType.PRIVATE)) {
      logger.info("[PM] {} : {}", authorName, messageContent);
    } else {
      logger.info("[Server: {}, Channel: {}] (User) {} : {}",
          event.getGuild().getName(), event.getChannel().getName(),
          Objects.requireNonNull(event.getMember())
              .getEffectiveName(),
          messageContent);
    }
  }

  private void handleMessage(MessageReceivedEvent event) {
    String messageContent = event.getMessage().getContentDisplay();
    String response = RESPONSE_MESSAGES.get(messageContent);

    if (response != null) {
      event.getChannel().sendMessage(response).queue();
    }
  }
}