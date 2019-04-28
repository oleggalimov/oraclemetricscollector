package ru.sbrf.oraclemetricscollector.utility;

import org.influxdb.InfluxDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sbrf.oraclemetricscollector.data.Instance;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class Scheduler {

    @Autowired
    InfluxDB influxDB;
    @Autowired
    @Qualifier("SQLQuerries")
    HashMap<String, String> queries;
    @Autowired
    HashSet<Instance> instances; //список инстансов для мониторинга

    @Scheduled (fixedRate = 60000)
    public void collectMetrics () {
        ExecutorService service = Executors.newFixedThreadPool(instances.size());
        instances.forEach( currentInstance -> service.submit(new Collector(currentInstance, influxDB,queries))
        );
        try {
            service.shutdown();
            boolean success = service.awaitTermination(30, TimeUnit.SECONDS);
            if (!success) {
                System.out.println("Ошибка конкурентного выполнения: превышено время ожидания");
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("Ошибка запуска по шедулеру");
            e.printStackTrace();
            service.shutdownNow();
        }
    }

    @Scheduled (fixedRate = 1000*60*30)
    public void backupInstances () throws FileNotFoundException {
        try (FileOutputStream fout = new FileOutputStream("oraclemetricscollector_instances.dat")) {
            instances.forEach( currentInstance -> {
                        try {
                            fout.write(currentInstance.toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
