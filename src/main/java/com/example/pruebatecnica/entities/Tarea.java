package com.example.pruebatecnica.entities;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@Table(name = "tareas", schema = "public")
public class Tarea {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ColumnDefault("default")
  @Column(name = "tar_id")
  private int id;

  @Column(name = "tar_identificador")
  @ColumnDefault("1")
  @NotNull(message = "El numero identificador no puede ser vacío")
  @Min(value = 1, message = "El identificador debe tener mínimo un carácter")
  private Integer identificador;

  @Column(name = "tar_descripcion")
  @NotNull(message = "La descripcion no puede ser vacío")
  @Size(min = 1, max = 255, message = "La descripcion debe tener mínimo un carácter y máximo 255")
  private String descripcion;

  @Column(name = "tar_fecha_creacion")
  @NotNull(message = "Fecha de creacion no puede ser null")
  private LocalDate fechaCreacion;

  @Column(name = "tar_vigente")
  @NotNull(message = "La vigencia no puede ser null")
  private boolean vigente;
}
