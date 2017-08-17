package org.crew.dto.generator.service;

import org.crew.dto.generator.annotation.DTOInterceptor;
import org.crew.dto.generator.dao.VeiculoDAO;
import org.crew.dto.generator.dto.VeiculoDTO;

public class VeiculoService implements Service<VeiculoDTO> {

	@SuppressWarnings("unchecked")
	@DTOInterceptor
	public Veiculo recuperar() {
		return VeiculoDAO.recuperarVeiculo();
	}

}