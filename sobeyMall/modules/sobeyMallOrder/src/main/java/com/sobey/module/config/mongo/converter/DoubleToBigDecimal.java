package com.sobey.module.config.mongo.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.math.BigDecimal;

/**
 * @author WCY
 * @createTime 2020/7/7 18:42
 */
@ReadingConverter
public class DoubleToBigDecimal implements Converter<Double, BigDecimal> {
    @Override
    public BigDecimal convert(Double source) {
        if (Double.isNaN(source)){
            return null;
        }
        return BigDecimal.valueOf(source);
    }
}
