# Lab2: Tests + Coverage (Gradle + Jacoco + GitHub Actions)

Цей архів перетворює твою Lab1 на формат, який вимагає Lab2:
- ✅ Підключено **Gradle** з розширеннями `test` та `jacoco` (див. `gradle/` + `build.gradle`)
- ✅ Додано **pipeline** GitHub Actions: `.github/workflows/pull_request.yml`
- ✅ Написані **юнiт-тести** для сервісів та **MVC-тести** валідації для контролера
- ✅ Ввімкнено **перевірку покриття ≥ 50%** (налаштовується в `build.gradle`)

### Локальний запуск
```bash
gradle clean test jacocoTestReport jacocoTestCoverageVerification
# Звіт: build/reports/jacoco/test/html/index.html
```

> Якщо тест `create_ok()` у контролері повертає 201 (Created) у твоєму коді — заміни в тесті `.andExpect(status().isOk())` на `.andExpect(status().isCreated())`.

### Що зроблено
- `build.gradle`, `settings.gradle` — перехід на Gradle (Java 21, Spring Boot 3.3.3)
- `gradle/jacoco.gradle`, `gradle/test.gradle` — Jacoco/тести як у лекції
- `.github/workflows/pull_request.yml` — pipeline для PR
- `src/test/java/...` — готові тести:
  - `ProductServiceImplTest` — позитивні/негативні сценарії, валідація, not-found
  - `ProductControllerValidationTest` — позитивні та негативні кейси валідації + delete 204

### Налаштування покриття
У `build.gradle`:
```gradle
ext {
    minimumCoveragePerFile = 0.50 // змінюй за потреби
    filesExcludedFromCoverage = ["**/*Application.*","**/config/**","**/dto/**","**/exception/**"]
}
```

Після пуша у гілку і створення PR — у вкладці **Actions** з’явиться джоб, який прикріпить HTML-звіт як артефакт.
