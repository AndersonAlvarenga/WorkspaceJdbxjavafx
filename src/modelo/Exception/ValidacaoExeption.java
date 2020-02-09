package modelo.Exception;

import java.util.HashMap;
import java.util.Map;

public class ValidacaoExeption extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Map<String, String> mapErros = new HashMap<>();

	public ValidacaoExeption(String msg) {
		super(msg);
	}

	public Map<String, String> getErro() {
		return mapErros;
	}

	public void addErro(String fildeName, String erroMsg) {
		mapErros.put(fildeName, erroMsg);
	}

}
