package cn.kmbeast.utils;

import java.math.BigDecimal;

public class DecimalUtils {

    /**
     * 金额计算
     *
     * @param buyNumber 购买数量
     * @param price     价格
     * @param discount  折扣
     * @return BigDecimal
     */
    public static BigDecimal calculateTotal(Integer buyNumber,
                                            BigDecimal price,
                                            Double discount) {
        // 将 Integer 转换为 BigDecimal
        BigDecimal buyNumberDecimal = BigDecimal.valueOf(buyNumber);
        // 计算总金额
        BigDecimal total = buyNumberDecimal.multiply(price);
        // 应用折扣
        BigDecimal discountFactor = BigDecimal.valueOf(discount); // 将折扣转换为 BigDecimal
        BigDecimal discountedTotal = total.multiply(discountFactor);
        return discountedTotal.setScale(2, BigDecimal.ROUND_CEILING); // 保留两位小数并向上取整
    }

}
