package modelo.service;

import java.util.ArrayList;
import java.util.List;

import modelo.entidade.Departamento;

public class DepartamentoService {
	
	public List<Departamento> findAll(){
		List<Departamento> listDepartamento = new ArrayList<Departamento>();
		listDepartamento.add(new Departamento(1, "Livros"));
		listDepartamento.add(new Departamento(2, "Eletronicos"));
		listDepartamento.add(new Departamento(3, "Materiais"));
		return listDepartamento;
	}

}
