package application.controller;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import javafx.application.Platform;
import newsstand.Config;
import newsstand.NewsstandSimulation;
import newsstand.components.entity.Employee;

import java.util.List;

public class test {
    public NewsstandSimulation simulation = new NewsstandSimulation(new Config()) ;

    public test() {
        this.simulation = simulation;

    }

    public void refreshEmployees(List<Employee> employee){
        System.out.println(employee);
    }


}

