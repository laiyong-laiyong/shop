package com.sobey.module.config.mongo;

import com.sobey.module.config.mongo.converter.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WCY
 * @createTime 2020/7/7 18:49
 */
@SpringBootConfiguration
public class MongoConf {

    @Bean
    public MongoCustomConversions customConversions(){
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new BigDecimalToDecimal128());
        converters.add(new Decimal128ToBigDecimal());
        converters.add(new DoubleToBigDecimal());
        converters.add(new BigDecimalToDouble());
        converters.add(new DoubleToDecimal128());
        return new MongoCustomConversions(converters);
    }

}
