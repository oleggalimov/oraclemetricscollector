package ru.sbrf.oraclemetricscollector.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.oraclemetricscollector.data.Instance;

import java.io.IOException;
import java.util.HashSet;


@RestController
public class DeleteInstance {
    @Autowired
    HashSet<Instance> instances;
    @ResponseBody
    @RequestMapping (value = "/delinstance", method = RequestMethod.POST)

    public Boolean addInstance(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        Instance instance;
        Boolean response=false;
        try {
            instance = mapper.readValue(body, mapper.getTypeFactory().constructType(Instance.class));
            response = instances.remove(instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
