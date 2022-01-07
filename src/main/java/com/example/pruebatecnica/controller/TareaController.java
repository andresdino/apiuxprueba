package com.example.pruebatecnica.controller;

import com.example.pruebatecnica.entities.Tarea;
import com.example.pruebatecnica.service.TareaService;
import com.example.pruebatecnica.service.exceptions.ObjetoNoExisteException;
import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/v1/tarea", produces = MediaType.APPLICATION_JSON_VALUE)
public class TareaController {

  private TareaService tareaService;

  @Autowired
  public TareaController(TareaService tareaService) {
    this.tareaService = tareaService;
  }

  @GetMapping
  public ResponseEntity<List<Tarea>> listar() {
    List<Tarea> tareas = this.tareaService.listar();
    return new ResponseEntity<>(tareas, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Tarea> crear(@Valid @RequestBody Tarea tarea) {
    var tareas = this.tareaService.crear(tarea);
    return new ResponseEntity<>(tareas, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Tarea> actualizar(@Valid @RequestBody Tarea tarea,
      @PathVariable("id") int id) {
    if (tarea == null || tarea.getId() != id) {
      throw new ObjetoNoExisteException("El id de la tarea a actualizar no corresponde al path");
    }
    var data = this.tareaService.actualizar(tarea);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }
}
