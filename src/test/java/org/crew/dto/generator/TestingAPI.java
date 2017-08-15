package org.crew.dto.generator;

import org.crew.dto.generator.dto.VeiculoDTO;
import org.crew.dto.generator.model.Cliente;
import org.crew.dto.generator.model.Veiculo;
import org.crew.dto.generator.reflection.ReflectUtil;

public class TestingAPI {
	
	public static void main(String[] args) {
		
		Cliente cliente = new Cliente();
		cliente.setId(1L);
		cliente.setCelular("9999-9999");
		cliente.setEmail("teste@teste.com");
		cliente.setNome("Danyllo");
		cliente.setTelefoneFixo("2222-9999");
		
		Veiculo veiculo = new Veiculo();
		veiculo.setCliente(cliente);
		veiculo.setCor("Black");
		veiculo.setFabricante("CREW");
		veiculo.setModelo("M1CREW");
		veiculo.setPlaca("CREW1234");
		
		System.out.println(veiculo.getCliente().getNome());
		
		VeiculoDTO dto = new VeiculoDTO();
		ReflectUtil.buildDTO(veiculo, dto);
		
		System.out.println("Result " + dto.getCliente());
	}

}
