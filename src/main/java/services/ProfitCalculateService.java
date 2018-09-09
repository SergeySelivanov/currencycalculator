package services;

import java.math.BigDecimal;

/**
 * Сервис для расчета прибыли/убытков при продаже валюты
 */
public class ProfitCalculateService {
    private static final BigDecimal SPREAD_COF_FOR_START_PRICE = new BigDecimal("1.0025");
    private static final BigDecimal SPREAD_COF_FOR_END_PRICE = new BigDecimal("0.9975");

    /**
     * Расчитывает прибыль/убыток получаемый при продаже валюты.
     * Значение формируется исходя из разницы курсов на момент покупки и момент продажи, а также вычитается спред,
     * равный 0.5%
     *
     * @param startPrice курс валюты на момент покупки
     * @param endPrice   курс валюты на момент продажи
     * @param amount     сумма купленной валюты
     * @return прибыль/убыток
     */
    public static BigDecimal calculateProfit(BigDecimal startPrice, BigDecimal endPrice, BigDecimal amount) {
        BigDecimal startPriceWithSpread = startPrice.multiply(SPREAD_COF_FOR_START_PRICE);
        BigDecimal endPriceWithSpread = endPrice.multiply(SPREAD_COF_FOR_END_PRICE);
        return amount.multiply(endPriceWithSpread.subtract(startPriceWithSpread)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
