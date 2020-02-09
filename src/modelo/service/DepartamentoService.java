package modelo.service;

import java.util.ArrayList;
import java.util.List;

import modelo.dao.DaoFactory;
import modelo.entidades.Departamento;

public class DepartamentoService {
	
	public List<Departamento> findAll(){
		List<Departamento> listDepartamento = new ArrayList<Departamento>();
		listDepartamento=DaoFactory.createDepartDao().findAll();
		return listDepartamento;
	}

}
