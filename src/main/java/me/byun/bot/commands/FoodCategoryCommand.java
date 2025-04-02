package me.byun.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import me.byun.bot.services.FoodRecommendationService;
import java.util.List;

public class FoodCategoryCommand {
  private final FoodRecommendationService foodService;

  public FoodCategoryCommand() {
    this.foodService = new FoodRecommendationService();
  }

  public void execute(SlashCommandInteractionEvent event) {
    String command = event.getName();
    String category;

    switch (command) {
    case "한식" -> category = "한식";
    case "중식" -> category = "중식";
    case "일식" -> category = "일식";
    case "양식" -> category = "양식";
    default -> {
      event.reply("지원하지 않는 음식 카테고리입니다.").setEphemeral(true).queue();
      return;
    }
    }

    List<String> foodList = foodService.getFoodsByCategory(category);

    if (foodList.isEmpty()) {
      event.reply(category + " 메뉴가 존재하지 않습니다.").setEphemeral(true)
          .queue();
      return;
    }

    StringBuilder response = new StringBuilder();
    response.append("**").append(category).append(" 메뉴 목록**\n\n");

    for (int i = 0; i < foodList.size(); i++) {
      response.append(i + 1).append(". ").append(foodList.get(i));
      if (i < foodList.size() - 1) {
        response.append("\n");
      }
    }

    String randomFood = foodList
        .get((int) (Math.random() * foodList.size()));
    response.append("\n\n오늘은 **").append(randomFood)
        .append("** 어떠세요? 😋");

    event.reply(response.toString()).queue();
  }
}