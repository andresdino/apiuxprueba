package com.example.pruebatecnica.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.pruebatecnica.entities.Tarea;
import com.example.pruebatecnica.repository.TareaRepository;
import com.example.pruebatecnica.service.exceptions.ObjetoNoExisteException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class TareaServiceImplTest {

  @Mock
  private static TareaRepository tareaRepository;

  @InjectMocks
  private static TareaServiceImpl tareaService;

  @Mock
  private static LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

  final static String NOMBRE_NO_EXISTENTE = "No existe nombre";
  final static String NOMBRE_YA_EXISTENTE = "Ya existe nombre";
  final static Integer IDENTIFICADOR = 123444;
  final static String DESCRIPCION = "xoxoxo";
  private static Integer CANTIDAD = 18;
  private final static LocalDate FECHA_HORA_ACTUAL = LocalDate.now();

  @Test
  @DisplayName("Cuando crea un nueva tarea retornar el elemento")
  void test_crear_tarea() {

    var tarea = this.construirTarea(1, IDENTIFICADOR, DESCRIPCION, FECHA_HORA_ACTUAL, true);
    tareaService.crear(tarea);
    verify(tareaRepository, times(1))
        .save(tarea);
  }

  @Test
  @DisplayName("Cuando actualiza y tarea no existe debe mostrar mensaje")
  void cuando_actualiza_y_tarea_no_existe_deberia_lanzar_exception() {
    var tarea = this.construirTarea(1, IDENTIFICADOR, DESCRIPCION, FECHA_HORA_ACTUAL, true);
    when(tareaRepository.findById(tarea.getId())).thenReturn(Optional.ofNullable(null));

    assertThrows(ObjetoNoExisteException.class, () -> tareaService.actualizar(tarea));

    verify(tareaRepository, times(1))
        .findById(tarea.getId());
    verify(tareaRepository, times(0))
        .save(tarea);
  }

  @Test
  @DisplayName("Cuando borra tarea y esta existe")
  void cuando_borrar_tarea_existe() {
    var tarea = this.construirTarea(2, IDENTIFICADOR, DESCRIPCION, FECHA_HORA_ACTUAL, true);
    Optional<Tarea> tareaExistente = Optional.of(tarea);

    when(tareaRepository.findById(2)).thenReturn(tareaExistente);

    tareaService.borrar(2);
    verify(tareaRepository, times(1)).deleteById(2);
  }

  @Test
  @DisplayName("Cuando borra tarea y no existe")
  void cuando_borrar_tarea_no_existe() {
    var tarea = this.construirTarea(10, IDENTIFICADOR, DESCRIPCION, FECHA_HORA_ACTUAL, true);
    Optional<Tarea> tareaNoExistente = Optional.ofNullable(null);

    when(tareaRepository.findById(10)).thenReturn(tareaNoExistente);

    assertThrows(ObjetoNoExisteException.class, () -> tareaService.borrar(10));

    verify(tareaRepository, times(0)).deleteById(10);
    verify(tareaRepository, times(1)).findById(10);
  }

  @ParameterizedTest(name = "Buscar tarea por id {0}")
  @ValueSource(ints = {5, 8, 9, 10, 50, 500, 1000})
  @DisplayName("Buscar por id y tarea no exite arrojar mensaje")
  void test_buscar_por_id_y_tarea_y_esta_no_existe(int id) {
    Optional<Tarea> tareaPorIdNoExiste = Optional.empty();
    when(tareaRepository.findById(id)).thenReturn(tareaPorIdNoExiste);

    assertThrows(ObjetoNoExisteException.class, () -> tareaService.buscarPorId(id));

    verify(tareaRepository, times(1)).findById(id);
  }

  @Test
  void listar() {
    Sort sort = Sort.by("identificador");
    when(tareaRepository.findAll(sort)).thenReturn(this.obtenerListaTarea());

    List<Tarea> tarea = tareaService.listar();

    assertThat(tarea).isNotNull().isNotEmpty();
    verify(tareaRepository, times(1)).findAll(sort);
  }

  private Tarea construirTarea(int id, int indentificador, String descripcion,
      LocalDate fechaCreacion, boolean vigente) {
    var tarea = new Tarea();
    tarea.setId(id);
    tarea.setIdentificador(indentificador);
    tarea.setDescripcion(descripcion);
    tarea.setFechaCreacion(fechaCreacion);
    tarea.setVigente(vigente);

    return tarea;
  }

  private List<Tarea> obtenerListaTarea() {
    Tarea tarea1 = this.construirTarea(1, 12,"sdfghbj",FECHA_HORA_ACTUAL,false);
    Tarea tarea2 = this.construirTarea(2,12,"sdfghbj",FECHA_HORA_ACTUAL,true);
    return Arrays.asList(tarea1, tarea2);
  }

}