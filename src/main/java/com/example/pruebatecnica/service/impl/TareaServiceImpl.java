package com.example.pruebatecnica.service.impl;

import com.example.pruebatecnica.entities.Tarea;
import com.example.pruebatecnica.repository.TareaRepository;
import com.example.pruebatecnica.service.TareaService;
import com.example.pruebatecnica.service.exceptions.ObjetoNoExisteException;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TareaServiceImpl implements TareaService {

  private Validator validator;
  private TareaRepository tareaRepository;

  @Autowired
  public TareaServiceImpl(Validator validator,
      TareaRepository tareaRepository) {
    this.validator = validator;
    this.tareaRepository = tareaRepository;
  }

  @Override
  public Tarea crear(Tarea tarea) {
    this.validar(tarea);
    this.tareaRepository.save(tarea);
    return tarea;
  }

  @Override
  public Tarea actualizar(Tarea tarea) {
    this.validar(tarea);
    this.buscarPorId(tarea.getId());
    this.tareaRepository.save(tarea);
    return tarea;
  }

  @Override
  public List<Tarea> listar() {
    return this.tareaRepository.findAll(Sort.by("identificador"));
  }

  @Override
  public Tarea buscarPorId(int id) {
    return this.tareaRepository.findById(id)
        .orElseThrow(() -> new ObjetoNoExisteException("No existe la tarea con el ID: " + id));
  }

  @Override
  public void borrar(int id) {
    var tarea = this.buscarPorId(id);
    this.tareaRepository.deleteById(tarea.getId());
  }

  private void validar(Tarea tarea) {
    Set<ConstraintViolation<Tarea>> violations = validator.validate(tarea);
    if (!violations.isEmpty()) {
      var sb = new StringBuilder();
      for (ConstraintViolation<Tarea> constraintViolation : violations) {
        sb.append(constraintViolation.getMessage());
      }
      throw new ConstraintViolationException("Error occurred: " + sb, violations);
    }
  }
}
