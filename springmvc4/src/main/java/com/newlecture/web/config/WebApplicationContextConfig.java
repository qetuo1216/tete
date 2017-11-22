package com.newlecture.web.config;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

//mvc관련 설정을 넣는다.
@Configuration //원래 web.xml에  쓴걸 java 에서 구현하겠다는 것이다!
@EnableWebMvc
@Component("com.newlecture.web.controller")
public class WebApplicationContextConfig extends WebMvcConfigurerAdapter{//필수요소들을 구현해준다. 없어도 되지만 합시다!!

	@Bean
	public InternalResourceViewResolver getInternalViewResolver() {
		
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		
		return resolver;
	}
	
	//default값 설정 
	@Override
		public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
			configurer.enable();
		}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry
		.addResourceHandler("/resource/**")
		.addResourceLocations("/resource/");
	}
	
	@Override
		public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
			converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
			super.configureMessageConverters(converters);
		}
}
