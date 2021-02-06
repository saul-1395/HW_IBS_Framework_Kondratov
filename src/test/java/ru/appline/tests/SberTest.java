package ru.appline.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.appline.baseTests.BaseTests;

import static java.lang.Thread.sleep;

public class SberTest extends BaseTests {

    @Test
    public void runTest() throws InterruptedException {


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
        jse.executeScript("window.scrollBy(0,-2400)");
        sleep(2000);
        checkElementisVisible(driver.findElement(By.xpath("//h1/..//a/span[text()='" + buttonOnline + "']")), "Оформить онлайн");
        clickButton("//h1/..//a/span[text()='" + buttonOnline + "']/..");

        //заполняем поля
        sleep(3000);
        inputLabelForm("Фамилия", "Кодавр");
        inputLabelForm("Имя", "Саул");
        inputLabelForm("Отчество", "Вичевски");

        //заполняем поле с почтой
        WebElement inputMail = driver.findElement(By.xpath("//label[text()='E-mail']/../input"));
        inputMail.sendKeys("mail@mail.com");

        //заполняем Рiк Нарождення
        String birthDay = "Дата рождения";
        WebElement inputeBirthDay = driver.findElement(By.xpath("//label[text()='" + birthDay + "']/../div/div/input"));
        inputeBirthDay.sendKeys("01012000");
        sleep(1000);

        //заполняем поле телфон         !!! неудачно, так и не осилил пока, просто курсор моргает, ничего не вводится и ошибка не пдает, кликнуть нельзя
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//input[@id='odc-personal__phone']"))));
        driver.findElement(By.xpath("//input[@id='odc-personal__phone']")).sendKeys("+79197700610");
        sleep(3000);

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
        Assertions.assertAll("fields",  () -> Assertions.assertEquals(fieldDate, "Обязательное поле"),
                                                () -> Assertions.assertEquals(fieldNumber, "Обязательное поле"),
                                                () -> Assertions.assertEquals(fieldSerial, "Обязательное поле"));

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
    private void inputLabelForm(String value, String keys) {
        WebElement element = driver.findElement(By.xpath("//label[text()='" + value + "']/../input"));
        wait.until(ExpectedConditions.visibilityOf(element));
        element.click();
        element.sendKeys(keys);
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

}
