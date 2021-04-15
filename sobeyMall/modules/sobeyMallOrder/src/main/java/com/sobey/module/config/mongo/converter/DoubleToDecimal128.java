package com.sobey.module.config.mongo.converter;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.math.BigDecimal;

/**
 * @author WCY
 * @createTime 2020/7/9 18:21
 */
@WritingConverter
public class DoubleToDecimal128 implements Converter<Double, Decimal128> {
    @Override
    public Decimal128 convert(Double source) {
        return new Decimal128(BigDecimal.valueOf(source));
    }
}
