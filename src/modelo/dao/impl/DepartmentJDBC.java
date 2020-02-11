package modelo.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegritExeption;
import modelo.dao.DepartamentoDao;
import modelo.entidades.Departamento;

public class DepartmentJDBC implements DepartamentoDao {
	private Connection com;

	public DepartmentJDBC(Connection com) {
		this.com = com;
	}

	@Override
	public void insert(Departamento dep) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = com.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, dep.getNome());
			int linhaInsert = ps.executeUpdate();
			if (linhaInsert > 0) {
				rs = ps.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					dep.setId(id);
				}
			} else {
				throw new DbException("Erro inexperado nenhuma linha add");
			}
		} catch (SQLException e) {
			throw new DbException("Erro: " + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}

	}

	@Override
	public void update(Departamento dep) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			com.setAutoCommit(false);
			ps = com.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
			ps.setString(1, dep.getNome());
			ps.setInt(2, dep.getId());
			ps.executeUpdate();
			com.commit();
		} catch (SQLException e) {
			try {
				com.rollback();
				throw new DbException("Erro: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbIntegritExeption("Erro: " + e.getMessage());
			}

		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}

	}

	@Override
	public void deleteById(int id) {
		PreparedStatement ps = null;
		try {
			com.setAutoCommit(false);
			ps = com.prepareStatement("DELETE FROM department WHERE Id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
			System.out.println("Dado deletado");
			com.commit();
		} catch (SQLException e) {
			try {
				com.rollback();
			} catch (SQLException e1) {
				throw new DbIntegritExeption("Erro ao executar rollback: "+e.getMessage());
			}
			throw new DbIntegritExeption("Erro ao deletar: "+e.getMessage());
			
		}

	}

	@Override
	public Departamento findById(int id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = com.prepareStatement("SELECT *FROM department WHERE Id=?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Departamento dep = instacDepart(rs);
				return dep;
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new DbException("Erro: " + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}

	}

	@Override
	public List<Departamento> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Departamento> listDepar = new ArrayList<Departamento>();

		try {
			ps = com.prepareStatement("SELECT * FROM department");
			rs = ps.executeQuery();

			while (rs.next()) {
				Departamento depFindAll = instacDepart(rs);
				listDepar.add(depFindAll);
			}
		} catch (SQLException e) {
			throw new DbException("Erro: " + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		if (listDepar.isEmpty()) {
			return null;
		} else {
			return listDepar;
		}
	}

	private Departamento instacDepart(ResultSet rs) throws SQLException {
		return new Departamento(rs.getInt("Id"), rs.getString("Name"));
	}

}
