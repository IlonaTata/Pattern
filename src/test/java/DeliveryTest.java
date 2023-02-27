import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;


import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

class DeliveryTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());}
    @BeforeEach
     void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

        @Test
        @DisplayName("Should successful plan and replan meeting")
        void shouldSuccessfulPlanAndReplanMeeting() {
            DataGenerator.Registration.generateUser("ru");
            int daysToAddForFirstMeeting = 4;
            String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
            int daysToAddForSecondMeeting = 7;
            var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] input").setValue(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date = DataGenerator.generateDate(4);
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id='phone'] input").setValue(DataGenerator.generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $x("//*[@class='button__content']").click();
        $("[data-test-id='success-notification'] button").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + date), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date1 = DataGenerator.generateDate(7);
        $("[data-test-id='date'] input").setValue(date1);
        $$("button").find(Condition.exactText("Запланировать")).click();
        $("[data-test-id='replan-notification']")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(15))
                .shouldBe(visible);
        $x("//*[@class='button__text']").click();
        $("[data-test-id='success-notification'] button").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + date1), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }
}



