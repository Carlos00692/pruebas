package com.bibliouni.modelo;

public class Material {

    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private int ejemplaresDisponibles;

    public Material() {
    }

    public Material(Long id, String titulo, String autor, String isbn, int ejemplaresDisponibles) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    public boolean estaDisponible() {
        return ejemplaresDisponibles > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getEjemplaresDisponibles() {
        return ejemplaresDisponibles;
    }

    public void setEjemplaresDisponibles(int ejemplaresDisponibles) {
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }
}
