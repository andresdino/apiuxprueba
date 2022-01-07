package com.example.pruebatecnica.service;

import com.example.pruebatecnica.entities.Tarea;
import java.util.List;

public interface TareaService {

  Tarea crear(Tarea tarea);

  Tarea actualizar(Tarea tarea);

  List<Tarea> listar();

  Tarea buscarPorId(int id);


  void borrar(int id);
}
