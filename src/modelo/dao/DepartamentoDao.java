package modelo.dao;

import java.util.List;

import modelo.entidades.Departamento;

public interface DepartamentoDao {

	void insert(Departamento dep);
	void update(Departamento dep);
	void deleteById(int id);
	Departamento findById(int id);
	List<Departamento> findAll();
}
