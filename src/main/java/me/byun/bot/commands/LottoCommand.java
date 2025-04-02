package me.byun.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.util.*;

public class LottoCommand {
  private static final int LOTTO_NUMBERS_COUNT = 6;
  private static final int LOTTO_MAX_NUMBER = 45;

  public void execute(SlashCommandInteractionEvent event) {
    Set<Integer> numberSet = generateLottoNumbers();
    int[] numbers = numberSet.stream().mapToInt(Integer::intValue)
        .toArray();
    Arrays.sort(numbers);

    String result = formatLottoResult(numbers);
    event.reply(result).setEphemeral(false).queue();
  }

  private Set<Integer> generateLottoNumbers() {
    Set<Integer> numberSet = new HashSet<>();
    Random random = new Random();

    while (numberSet.size() < LOTTO_NUMBERS_COUNT) {
      numberSet.add(random.nextInt(LOTTO_MAX_NUMBER) + 1);
    }

    return numberSet;
  }

  private String formatLottoResult(int[] numbers) {
    StringBuilder str = new StringBuilder("추천해줄 6개의 숫자는 ");
    for (int i = 0; i < numbers.length; i++) {
      str.append(numbers[i]);
      if (i < numbers.length - 1) {
        str.append(", ");
      }
    }
    str.append("야");
    return str.toString();
  }
}