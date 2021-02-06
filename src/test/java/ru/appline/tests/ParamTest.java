package ru.appline.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.appline.baseTests.BaseTests;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class ParamTest extends BaseTests {

    LinkedHashMap<String, String> expectedlValue = new LinkedHashMap<>();

    @ParameterizedTest
    @MethodSource("positiveExpected")
    public void method2(String firstname, String lastname, String patronymic) throws InterruptedException {

        //нажимаем карты
        clickButton(getElementContainsText("Карты"));

        //нажимаем в списке слева "Дебетовые карты"
        clickButton("//li/*[contains(text(), 'Дебетовые карты')]");

        //проверка заголовка "Дебетовые карты"
        String headerDebCard = "Дебетовые карты";
        WebElement headerDebetCard = driver.findElement(By.xpath("//h1[text()='" + headerDebCard + "']"));
        checkElementisVisible(headerDebetCard, "Дебетовые карты");

        //выбираем "молодёжная карта" и нажимаем заказть онлайн
        String cardType = "Молодёжная карта";
        String cardButton = "Заказать онлайн";
        clickButton("//*[text()='" + cardType + "']/../..//span[text()='" + cardButton + "']/..");
        //проверка заголовка "Молодёжная карта"
        String headerJunCard = "Молодёжная карта";
        WebElement headerJuniorCard = driver.findElement(By.xpath("//h1[text()='" + headerJunCard + "']"));
        checkElementisVisible(headerJuniorCard, "Молодёжная карта");

        //делаем скрол ввверх и нажимаем "Оформить онлайн"
        String buttonOnline = "Оформить онлайн";
        sleep(1000);
        jse.executeScript("window.scrollBy(0,-2400)");
        sleep(2000);
        checkElementisVisible(driver.findElement(By.xpath("//h1/..//a/span[text()='" + buttonOnline + "']")), "Оформить онлайн");
        clickButton("//h1/..//a/span[text()='" + buttonOnline + "']/..");
        sleep(2000);
        
        //заполняем поля
        String lastnameLabel = "Фамилия";
        String firstnameLabel = "Имя";
        String patronymicLabel = "Отчество";
        inputLabelForm(firstnameLabel, firstname);
        inputLabelForm(lastnameLabel, lastname);
        inputLabelForm(patronymicLabel, patronymic);

        //проверка введенных фио
        Assertions.assertAll("fields", () -> Assertions.assertEquals(expectedlValue.get(lastnameLabel), getValueFromInputLabelForm(lastnameLabel)),
                () -> Assertions.assertEquals(expectedlValue.get(firstnameLabel), getValueFromInputLabelForm(firstnameLabel)),
                () -> Assertions.assertEquals(expectedlValue.get(patronymicLabel), getValueFromInputLabelForm(patronymicLabel)));

        //заполняем поле с почтой
        String mailLable = "E-mail";
        inputMailForm(mailLable, "mail@mail.com");

        //заполняем Рiк Нарождення
        String birthDayLabel = "Дата рождения";
        inputDataForm(birthDayLabel, "01012000");

        //заполняем поле телфон
        inputPhoneForm("9197700610");

        sleep(1000);
        jse.executeScript("window.scrollBy(0,800)");
        sleep(1000);
        //нажимаем кнопку Далее
        WebElement buttonContinious = driver.findElement(By.xpath("//span[text()='Далее']/.."));
        wait.until(ExpectedConditions.elementToBeClickable(buttonContinious));

        buttonContinious.click();

        sleep(1000);

        //берем из незаполненых полей алерты
        String fieldSerial = getAllertFromInputForm("Серия");
        String fieldNumber = getAllertFromInputForm("Номер");

        String fieldType = "Дата выдачи";
        WebElement fieldTypeDate = driver.findElement(By.xpath("//label[text()='" + fieldType + "']/../div[2]"));
        String fieldDate = fieldTypeDate.getText();


        //проверяем, что алерты соответствуют ожиданиям
        Assertions.assertAll("fields", () -> Assertions.assertEquals(fieldDate, "Обязательное поле"),
                () -> Assertions.assertEquals(fieldNumber, "Обязательное поле"),
                () -> Assertions.assertEquals(fieldSerial, "Обязательное поле"));

        expectedlValue.clear();
    }

    //метод заполнения формы Телефон
    private void inputPhoneForm(String phone) throws InterruptedException {
        WebElement element = driver.findElement(By.xpath("//Input[@data-name='phone']"));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(phone);
    }

    private void scrollToElementJs(WebElement element) {
//        Point location = element.getLocation();
//        String comand = "window.scrollBy(" + location.x + "," + location.y + ")";
//        jse.executeScript(comand);
    }

    //заполняем поле mail
    private void inputMailForm(String mailLable, String mail) {
        WebElement inputMail = driver.findElement(By.xpath("//label[text()='" + mailLable + "']/../input"));
        inputMail.sendKeys(Keys.CONTROL + "a");
        inputMail.sendKeys(Keys.DELETE);
        inputMail.sendKeys(mail);
    }

    static Stream<Arguments> positiveExpected() {
        return Stream.of(
                arguments("Иванов", "Иван", "Иванович"),
                arguments("Петров", "Петр", "Петрович"),
                arguments("Федоров", "Федор", "Федорович")
        );
    }

    //метод для получения текста из алертов у форм
    private String getAllertFromInputForm(String formName) {
        WebElement element = driver.findElement(By.xpath("//label[text()='" + formName + "']/../div"));
        String alert = element.getText();
        System.out.println(alert + " alert");
        return alert;
    }

    //метод для заполнения формы с предварительным кликом
    private void inputLabelForm(WebElement element, String keys) {
        element.click();
        element.sendKeys(keys);
    }

    //метод для заполнения формы с предварительным кликом
    private void inputLabelForm(String labelname, String value) {
        WebElement element = driver.findElement(By.xpath("//label[text()='" + labelname + "']/../input"));
        wait.until(ExpectedConditions.visibilityOf(element));

        element.clear();
        element.sendKeys(value);
        inputExpectedList(labelname, value);
    }

    //метод возвращает значение, которым заполнили поле.

    private String getValueFromInputLabelForm(String labelname) {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String path = "//label[text()='" + labelname + "']/../input";
        //   System.out.println("path  " + path);
        WebElement element = driver.findElement(By.xpath(path));
        wait.until(ExpectedConditions.visibilityOf(element));
        String value = element.getAttribute("value");
        //    System.out.println("value " + value);
        return value;


    }

    //проверка, что элемент кликабельный
    private void elementClickable(String locate) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locate)));
    }

    //проверка, что элемент кликабельный
    private void elementClickable(WebElement element) {
        // System.out.println(element.getText());
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    //клик с проверкой на кликабельность
    private void clickButton(String locate) {
        WebElement button = driver.findElement(By.xpath(locate));
        elementClickable(button);

        button.click();
    }

    //клик с проверкой на кликабельность
    private void clickButton(WebElement button) {
        elementClickable(button);
        button.click();
    }

    //возвращает вебэлемент по содержанию текста
    private WebElement getElementContainsText(String text) {
        return driver.findElement(By.xpath("//*[contains(text(), '" + text + "')]"));
    }

    //проверка на содержание текста в элементе
    private void checkElementisVisible(WebElement element, String expected) {
        wait.until(ExpectedConditions.visibilityOf(element));
        System.out.println(element.getText() + " assert");
        Assertions.assertEquals(expected, expected, element.getText());
    }

    //заполняем лист с актуальными значениями
    private void inputExpectedList(String key, String value) {
        expectedlValue.put(key, value);
    }

    //рiк нарождення
    private void inputDataForm(String locate, String date) {
        WebElement inputeBirthDay = driver.findElement(By.xpath("//label[text()='" + locate + "']/../div/div/input"));
        inputeBirthDay.clear();
        inputeBirthDay.sendKeys(date);
    }


}
