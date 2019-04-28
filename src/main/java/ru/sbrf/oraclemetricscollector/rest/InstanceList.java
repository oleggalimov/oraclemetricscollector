package ru.sbrf.oraclemetricscollector.rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sbrf.oraclemetricscollector.data.Instance;
import java.util.HashSet;

@RestController
public class InstanceList {
    @Autowired
    HashSet<Instance> instances;
    @ResponseBody
    @RequestMapping ("/instances" )
    public String getListInstances() {
        return instances.toString();
    }
}
