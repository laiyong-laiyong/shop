package com.sobey.module.config.mongo.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.math.BigDecimal;

/**
 * @author WCY
 * @createTime 2020/7/7 18:43
 */
@WritingConverter
public class BigDecimalToDouble implements Converter<BigDecimal,Double> {
    @Override
    public Double convert(BigDecimal source) {
        return source.doubleValue();
    }
}
