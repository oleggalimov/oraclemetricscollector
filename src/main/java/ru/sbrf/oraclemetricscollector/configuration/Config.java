package ru.sbrf.oraclemetricscollector.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.*;
import ru.sbrf.oraclemetricscollector.data.Instance;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;



@Configuration
@ComponentScan (basePackages="ru.sbrf.oraclemetricscollector")
@EnableScheduling
@EnableWebMvc
@PropertySources({
        @PropertySource("classpath:influx.properties"),
        @PropertySource("classpath:sqlqueries.properties")
}

)


public class Config  implements WebMvcConfigurer, WebApplicationInitializer  {
    //Конфигурация диспатчер - сервлета
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("ru.sbrf.oraclemetricscollector.configuration.Config");
        servletContext.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/pages/",".jsp");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/");

    }

    @Bean ("instances")
    public HashSet<Instance> parseConfig () throws IOException {

        File configFile = new ClassPathResource("instances.json").getFile();
        String config  = new String (Files.readAllBytes(Paths.get(configFile.getPath())));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HashSet <Instance> instanceArrayList = objectMapper.readValue(config,objectMapper.getTypeFactory().constructCollectionType(HashSet.class,Instance.class));
            instanceArrayList.forEach(System.out::println);
            return instanceArrayList;
        } catch (Exception ex) {
            System.out.println("Ошибка чтения списка инстансов, будет создан пустой конфиг файл!");
            ex.printStackTrace();
            return new HashSet <> ();
        }


    }
    @Bean(name="influxBD")
    InfluxDB getInfluxDB (
            @Value("${connection.url}") String connectURl,
            @Value("${connection.username}") String user,
            @Value("${connection.password}") String pass,
            @Value("${batching.byCount}") int batchByCount,
            @Value("${batching.byTimeInMs}") int batchByTime
            ) {

        InfluxDB influxBean = InfluxDBFactory.connect(connectURl, user, pass);
        influxBean.enableBatch(batchByCount, batchByTime, TimeUnit.MILLISECONDS);
        return influxBean;
    }

    @Bean ("SQLQuerries")
    public HashMap <String, String> getQuerries (
            @Value("${waitClass}") String waitClass,
            @Value("${waitEvent}") String waitEvent,
            @Value("${sysMetrics}") String sysMetrics,
            @Value("${tbsMetrics}") String tbsMetrics
    ) {
        HashMap<String, String> queries = new HashMap<>();
        queries.put("waitClass", waitClass);
        queries.put("waitEvent", waitEvent);
        queries.put("sysMetrics", sysMetrics);
        queries.put("tbsMetrics", tbsMetrics);
        return queries;
    }
    @Bean ("dbInfluxcreator")
    public String createInfluxDB (
            @Qualifier ("influxBD") InfluxDB influx,
            @Qualifier ("instances") HashSet<Instance> instances
            ) {
        //TODO
        return "Creation influxDb completed!";
    }


}
