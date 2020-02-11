package modelo.service;

import java.util.List;

import modelo.dao.DaoFactory;
import modelo.dao.DepartamentoDao;
import modelo.entidades.Departamento;

public class DepartamentoService {
	private DepartamentoDao departDao = DaoFactory.createDepartDao();
	public List<Departamento> findAll(){	
		return departDao.findAll();
	}
	public void insertDepartamento(Departamento dep) {
		DaoFactory.createDepartDao().insert(dep);
	}
	public void saveNewDepartmento(Departamento obj) {
		if(obj.getId()==null) {
		departDao.insert(obj);
		}else {
			departDao.update(obj);
		}
	}
	public void removeDepart(Departamento dep) {
		departDao.deleteById(dep.getId());
	}

}
