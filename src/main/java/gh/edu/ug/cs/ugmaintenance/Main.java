package gh.edu.ug.cs.ugmaintenance;

import gh.edu.ug.cs.ugmaintenance.api.ApiServer;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting UG Campus Maintenance Service Optimizer...");
        ApiServer.start(8080);
    }
}
