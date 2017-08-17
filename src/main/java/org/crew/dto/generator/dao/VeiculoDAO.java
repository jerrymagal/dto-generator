package org.crew.dto.generator.dao;

import org.crew.dto.generator.model.Cliente;
import org.crew.dto.generator.model.Motor;
import org.crew.dto.generator.model.Veiculo;

public class VeiculoDAO {

	public static Veiculo recuperarVeiculo(){
		Cliente cliente = new Cliente();
		cliente.setId(1L);
		cliente.setCelular("9999-9999");
		cliente.setEmail("teste@teste.com");
		cliente.setNome("Danyllo");
		cliente.setTelefoneFixo("2222-9999");
		
		Motor motor = new Motor();
		motor.setId(1L);
		motor.setNome("V6");
		
		Veiculo veiculo = new Veiculo();
		veiculo.setCor("Black");
		veiculo.setFabricante("CREW");
		veiculo.setModelo("M1CREW");
		veiculo.setPlaca("CREW1234");

		veiculo.setCliente(cliente);
		veiculo.setMotor(motor);
		
		System.out.println("=======MODEL========");
		
		System.out.println(veiculo.getCliente().getNome());
		System.out.println(veiculo.getMotor().getNome());
		
		return veiculo;
	}
}
