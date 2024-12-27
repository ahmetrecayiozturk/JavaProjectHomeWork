package org.project.frames.home.home.panels;

import org.project.data.JsonRepository;
import org.project.models.Cargo;
import org.project.services.CargoService;

import javax.swing.*;

public class Cargos extends JPanel {

    public Cargos() {
        CargoService.getAllCargos();

    }

}
