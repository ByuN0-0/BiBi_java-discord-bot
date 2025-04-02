package me.byun.bot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;
import javax.annotation.Nonnull;

public class SlashBot extends ListenerAdapter {
  private static final int MAX_CLEAR_AMOUNT = 20;
  private static final int LOTTO_NUMBERS_COUNT = 6;
  private static final int LOTTO_MAX_NUMBER = 45;
  private static final String[] FOODS = { "피자", "햄버거", "스테이크", "돈까스", "파스타", "쌀국수", "샐러드", "마라탕", "스시", "타코", "카레",
      "라면", "떡볶이", "비빔밥", "김치찌개", "부대찌개", "삼계탕", "해장국", "볶음밥", "우동", "곱창", "닭갈비", "갈비찜", "냉면", "삼겹살", "굶어", "양꼬치",
      "곱도리탕", "치킨", "족발", "보쌈", "닭발", "오뎅", "순대", "김밥", "초밥", "회", "초계탕", "칼국수", "잔치국수", "비빔국수", "쫄면", "짜장면", "짬뽕",
      "탕수육", "마라샹궈", "마라탕", "샤브샤브", "훠궈", "양고기", "고기국수", "육개장", "설렁탕", "갈비탕", "도가니탕", "부대찌개", "김치찌개", "된장찌개", "순두부찌개",
      "부대찌개", "참치찌개", "고추장찌개", "제육볶음", "오징어볶음", "낙지볶음", "닭볶음탕", "고추잡채", "잡채", "볶음밥", "김치볶음밥", "새우볶음밥", "치킨볶음밥", "돈부리",
      "오코노미야키", "타코야키", "라멘", "우동", "소바", "오니기리", "김치찌개", "부대찌개", "김치찌개", "부대찌개" };

  @Override
  public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
    try {
      switch (event.getName()) {
      case "ping" -> ping(event);
      case "team" -> team(event);
      case "lotto" -> lotto(event);
      case "점메추", "저메추" -> recommendFood(event);
      case "clear" -> clear(event);
      default -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
      }
    } catch (Exception e) {
      event.reply("명령어 실행 중 오류가 발생했습니다: " + e.getMessage()).setEphemeral(true).queue();
    }
  }

  public void ping(SlashCommandInteractionEvent event) {
    long time = System.currentTimeMillis();
    event.reply("Pong!").setEphemeral(false)
        .flatMap(v -> event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)).queue();
  }

  public void team(SlashCommandInteractionEvent event) {
    String playersStr = Objects.requireNonNull(event.getOption("players")).getAsString();
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

    String result = String.format("입력: %s\nteam 1: %s\nteam 2: %s", playersStr, String.join(", ", team1),
        String.join(", ", team2));

    event.reply(result).setEphemeral(false).queue();
  }

  public void lotto(SlashCommandInteractionEvent event) {
    Set<Integer> numberSet = generateLottoNumbers();
    int[] numbers = numberSet.stream().mapToInt(Integer::intValue).toArray();
    insertionSort(numbers);

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

  public void recommendFood(SlashCommandInteractionEvent event) {
    String recommendedFood = FOODS[(int) (Math.random() * FOODS.length)];
    event.reply("추천 음식: " + recommendedFood).setEphemeral(false).queue();
  }

  public void clear(SlashCommandInteractionEvent event) {
    int amount = Objects.requireNonNull(event.getOption("amount")).getAsInt();

    if (amount > MAX_CLEAR_AMOUNT) {
      event.reply(MAX_CLEAR_AMOUNT + "개 이상의 채팅을 삭제할 수 없습니다.").setEphemeral(true).queue();
      return;
    }

    Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
    if (!selfMember.hasPermission((GuildChannel) event.getChannel(), Permission.MESSAGE_MANAGE)) {
      event.reply("채팅을 삭제할 권한이 없습니다.").setEphemeral(true).queue();
      return;
    }

    deleteMessages(event, amount);
  }

  private void deleteMessages(SlashCommandInteractionEvent event, int amount) {
    event.getChannel().getHistory().retrievePast(amount).queue(messages -> {
      messages.forEach(message -> message.delete().queue());
      event.reply("채팅을 " + amount + "개 삭제했습니다.").setEphemeral(false).queue();
    });
  }

  public static void insertionSort(int[] arr) {
    for (int i = 1; i < arr.length; i++) {
      int key = arr[i];
      int j = i - 1;

      while (j >= 0 && arr[j] > key) {
        arr[j + 1] = arr[j];
        j--;
      }
      arr[j + 1] = key;
    }
  }
}
