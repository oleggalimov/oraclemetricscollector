package ru.sbrf.oraclemetricscollector.utility;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import ru.sbrf.oraclemetricscollector.data.Instance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class Collector implements Runnable {
    private final Instance instance;
    private final InfluxDB influxInstance;
    private final HashMap<String, String> queries;
    private final String DBNAME;

    public Collector (Instance dbInstance, InfluxDB influxInstance, HashMap<String, String> queryList) {
        this.instance=dbInstance;
        this.influxInstance=influxInstance;
        this.queries=queryList;
        this.DBNAME=dbInstance.getInfluxAlias();
    }
    @Override
    public void run() {
        try (Connection conn = DriverManager.getConnection(instance.getConnectionString())) {
            ResultSet waitClassResultSet = conn.createStatement().executeQuery(queries.get("waitClass"));
            while (waitClassResultSet.next()) {
                String WAIT_CLASS = waitClassResultSet.getString("WAIT_CLASS").replace(" ","_");
                float VALUE = waitClassResultSet.getFloat("AAS");

                Point.Builder point = Point.measurement("waitClass");
                point.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                point.tag("className", WAIT_CLASS);
                point.addField("value", VALUE);
                Point builded = point.build();
                influxInstance.write(DBNAME, "autogen", builded);
            }
            ResultSet waitEventResultSet = conn.createStatement().executeQuery(queries.get("waitEvent"));
            while (waitEventResultSet.next()) {
                String WAIT_CLASS = waitEventResultSet.getString("WAIT_CLASS").replace(" ","_");
                String WAIT_NAME = waitEventResultSet.getString("WAIT_NAME").replace(" ","_");
                float CNT = waitEventResultSet.getFloat("CNT");
                float AVGMS = waitEventResultSet.getFloat("AVGMS");

                Point.Builder point = Point.measurement("waitEvent");
                point.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                point.tag("className", WAIT_CLASS);
                point.tag("wait_event", WAIT_NAME);
                point.addField("count", CNT);
                point.addField("latency", AVGMS);
                Point builded = point.build();
                influxInstance.write(DBNAME, "autogen", builded);
            }
            ResultSet sysMetricsResultSet = conn.createStatement().executeQuery(queries.get("sysMetrics"));
            while (sysMetricsResultSet.next()) {
                String METRIC_NAME = sysMetricsResultSet.getString("METRIC_NAME").replace(" ","_");
                float VALUE = sysMetricsResultSet.getFloat("VALUE");

                Point.Builder point = Point.measurement("sysMetrics");
                point.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                point.tag("metric_name", METRIC_NAME);
                point.addField("value", VALUE);
                Point builded = point.build();
                influxInstance.write(DBNAME, "autogen", builded);
            }

            ResultSet tbsMetricsResultSet = conn.createStatement().executeQuery(queries.get("tbsMetrics"));
            while (tbsMetricsResultSet.next()) {
                String TABLESPACE_NAME = tbsMetricsResultSet.getString("TABLESPACE_NAME").replace(" ","_");
                Float PERC_USED = tbsMetricsResultSet.getFloat("PERC_USED");

                Point.Builder point = Point.measurement("tbsMetrics");
                point.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                point.tag("tablespace_name", TABLESPACE_NAME);
                point.addField("perc_used", PERC_USED);
                Point builded = point.build();
                influxInstance.write(DBNAME, "autogen", builded);
            }
            System.out.println("Обработаны метрики для "+instance.getConnectionString());
        } catch (Exception e) {
            System.out.println("Поток выполнения прерван! Инстанс:"+instance.getInfluxAlias());
            e.printStackTrace();

        }

    }

}
