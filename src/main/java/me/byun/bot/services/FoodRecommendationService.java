package me.byun.bot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.io.IOException;
import java.util.*;

public class FoodRecommendationService {
  private static final Map<String, List<String>> FOOD_CATEGORIES = new HashMap<>();
  private static final Map<String, List<String>> SEASONAL_FOODS = new HashMap<>();
  private static final Map<String, List<String>> TIME_FOODS = new HashMap<>();
  private static final List<String> DELIVERY_FOODS = new ArrayList<>();
  private static final List<String> SOLO_FOODS = new ArrayList<>();
  private static final List<String> ALCOHOL_FOODS = new ArrayList<>();
  private static final Random random = new Random();

  static {
    try {
      ObjectMapper mapper = new ObjectMapper();
      InputStream is = FoodRecommendationService.class
          .getResourceAsStream("/foods.json");

      // 직접 전체 구조에 맞는 TypeReference 사용
      TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};
      HashMap<String, Object> foodData = mapper.readValue(is,
          typeRef);

      // 세부 데이터 변환을 위한 TypeReference 생성
      TypeReference<HashMap<String, List<String>>> mapTypeRef = new TypeReference<>() {};

      // 카테고리별 음식
      if (foodData.containsKey("categories")) {
        HashMap<String, List<String>> categories = mapper
            .convertValue(foodData.get("categories"), mapTypeRef);
        FOOD_CATEGORIES.putAll(categories);
      }

      // 계절별 음식
      if (foodData.containsKey("seasons")) {
        HashMap<String, List<String>> seasons = mapper
            .convertValue(foodData.get("seasons"), mapTypeRef);
        SEASONAL_FOODS.putAll(seasons);
      }

      // 시간대별 음식
      if (foodData.containsKey("timeOfDay")) {
        HashMap<String, List<String>> timeOfDay = mapper
            .convertValue(foodData.get("timeOfDay"), mapTypeRef);
        TIME_FOODS.putAll(timeOfDay);
      }

      // 추가 카테고리
      TypeReference<List<String>> listTypeRef = new TypeReference<>() {};

      if (foodData.containsKey("배달맛집")) {
        List<String> delivery = mapper
            .convertValue(foodData.get("배달맛집"), listTypeRef);
        DELIVERY_FOODS.addAll(delivery);
      }

      if (foodData.containsKey("혼밥")) {
        List<String> solo = mapper.convertValue(foodData.get("혼밥"),
            listTypeRef);
        SOLO_FOODS.addAll(solo);
      }

      if (foodData.containsKey("술안주")) {
        List<String> alcohol = mapper
            .convertValue(foodData.get("술안주"), listTypeRef);
        ALCOHOL_FOODS.addAll(alcohol);
      }
    } catch (IOException e) {
      System.err
          .println("음식 데이터를 로드하는 중 오류가 발생했습니다: " + e.getMessage());
    }
  }

  public String getCurrentSeason() {
    Calendar cal = Calendar.getInstance();
    int month = cal.get(Calendar.MONTH) + 1;

    if (month >= 3 && month <= 5)
      return "봄";
    if (month >= 6 && month <= 8)
      return "여름";
    if (month >= 9 && month <= 11)
      return "가을";
    return "겨울";
  }

  public String generateFoodRecommendation(String timeOfDay,
      String season) {
    List<String> allFoods = new ArrayList<>();

    // 시간대별 음식 추가
    if (TIME_FOODS.containsKey(timeOfDay)) {
      allFoods.addAll(TIME_FOODS.get(timeOfDay));
    }

    // 계절별 음식 추가
    if (SEASONAL_FOODS.containsKey(season)) {
      allFoods.addAll(SEASONAL_FOODS.get(season));
    }

    // 랜덤으로 카테고리 선택
    String randomCategory = new ArrayList<>(FOOD_CATEGORIES.keySet())
        .get(random.nextInt(FOOD_CATEGORIES.size()));
    allFoods.addAll(FOOD_CATEGORIES.get(randomCategory));

    // 야식 시간에는 배달 음식과 술안주도 추가 (저녁 9시 이후)
    Calendar now = Calendar.getInstance();
    int hour = now.get(Calendar.HOUR_OF_DAY);
    if (hour >= 21 || hour < 5) {
      allFoods.addAll(DELIVERY_FOODS);
      allFoods.addAll(ALCOHOL_FOODS);
    }

    // 중복 제거
    Set<String> uniqueFoods = new HashSet<>(allFoods);
    if (uniqueFoods.isEmpty()) {
      return "추천 가능한 음식이 없습니다.";
    }

    // 랜덤으로 음식 선택
    return new ArrayList<>(uniqueFoods)
        .get(random.nextInt(uniqueFoods.size()));
  }
}