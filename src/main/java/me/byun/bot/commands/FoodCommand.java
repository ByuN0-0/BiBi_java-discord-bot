package me.byun.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import me.byun.bot.services.FoodRecommendationService;

public class FoodCommand {
  private final FoodRecommendationService foodService;

  public FoodCommand() {
    this.foodService = new FoodRecommendationService();
  }

  public void execute(SlashCommandInteractionEvent event) {
    String command = event.getName();
    String timeOfDay;

    switch (command) {
    case "아메추" -> timeOfDay = "아침";
    case "점메추" -> timeOfDay = "점심";
    case "저메추" -> timeOfDay = "저녁";
    default -> timeOfDay = "점심";
    }

    String currentSeason = foodService.getCurrentSeason();
    String recommendedFood = foodService
        .generateFoodRecommendation(timeOfDay, currentSeason);

    String response = String.format("추천 음식: %s\n(시간대: %s, 계절: %s)",
        recommendedFood, timeOfDay, currentSeason);

    event.reply(response).setEphemeral(false).queue();
  }
}