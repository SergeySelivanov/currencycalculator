package services;


import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Класс для работы с курсами валют
 */
public class ExchangeRatesService {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String URL_FORMAT =
            "http://data.fixer.io/api/%s?access_key=9473cf370874937716a8d58f5d2c9987&symbols=RUB&format=1";

    /**
     * Возращает курс на дату
     *
     * @param date дата, на которую определяется курс
     * @return курс валюты
     * @throws IOException    ошибка при получении курса через GET-запро
     * @throws ParseException ошибка парсинга ответа
     */
    public static BigDecimal getExchangeRate(Date date) throws IOException, ParseException {
        String jsonString = sendGetRequest(date);
        JSONParser parser = new JSONParser();
        return new BigDecimal(((HashMap) ((HashMap) parser.parse(jsonString)).get("rates")).get("RUB").toString());
    }

    /**
     * Отправляет GET-запрос для получения информации о курсе доллара
     *
     * @param date дата, на которую необходимо узнать курс
     * @return полученный ответ
     * @throws IOException ошибка при отправлении запроса
     */
    private static String sendGetRequest(Date date) throws IOException {
        URL obj = new URL(String.format(URL_FORMAT, DATE_FORMAT.format(date)));
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();

        return response.toString();
    }
}
