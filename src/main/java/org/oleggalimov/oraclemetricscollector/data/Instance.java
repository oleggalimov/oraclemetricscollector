package org.oleggalimov.oraclemetricscollector.data;


import java.util.Objects;

public class Instance {
    private String host;
    private String port;
    private String sid;
    private String user;
    private String pass;

    public Instance() {
    }

    public Instance(String HOST, String PORT, String SID, String USER, String PASS) {
        host = HOST;
        port = PORT;
        sid = SID;
        user = USER;
        pass = PASS;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "{\"host\":\"" + host + "\", \"port\":\""+port+"\",\"sid\":\""+sid+"\",\"user\":\""+user+"\"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instance instance = (Instance) o;
        return Objects.equals(host, instance.host) &&
                Objects.equals(port, instance.port) &&
                Objects.equals(sid, instance.sid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(host, port, sid);
    }

    public String getConnectionString () {
        return String.format("jdbc:oracle:thin:%s/%s@%s:%s:%s", this.user, this.pass,this.host, this.port,this.sid);
    }

    public String getInfluxAlias () {
        return String.format("%s_%s_%s", this.host, this.port,this.sid);
    }
}
