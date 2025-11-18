package com.example.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {
	// 데이터는 레디스에 잘 들어가지만 SecurityContextImpl가 생성자가 없어서 서버오류반환
	// 시큐리티 필터를 구성하여 해결(시큐리티컨텍스트)
    // @Bean
    // public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
    //     return RedisSerializer.json();
    // }
}
