package ru.sbrf.oraclemetricscollector.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.oraclemetricscollector.data.Instance;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;


@RestController
public class AddInstance {
    @Autowired
    HashSet<Instance> instances;
    String CHECK_USER_SQL_Q="SELECT ACCOUNT_STATUS FROM dba_users WHERE username=?";
    @ResponseBody
    @RequestMapping (value = "/addinstance", method = RequestMethod.POST)

    public String addInstance(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        Instance instance=null;
        String response="false";
        try {
            instance = mapper.readValue(body, mapper.getTypeFactory().constructType(Instance.class));
            try (Connection conn = DriverManager.getConnection(instance.getConnectionString())) {
                PreparedStatement querry = conn.prepareStatement(CHECK_USER_SQL_Q);
                querry.setString(1,instance.getUser());
                ResultSet resultSet = querry.executeQuery();
                while (resultSet.next()) {
                    String status = resultSet.getString(1);
                    if (!status.equals("OPEN")) {
                        throw new Exception("Некорректный статус пользователя");
                    } else {
                        response="true";
                        instances.add(instance);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
