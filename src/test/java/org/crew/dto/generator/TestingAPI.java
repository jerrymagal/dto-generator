package org.crew.dto.generator;

import org.crew.dto.generator.dto.VeiculoDTO;
import org.crew.dto.generator.reflection.ReflectUtil;
import org.crew.dto.generator.service.Service;
import org.crew.dto.generator.service.ServiceFactory;
import org.crew.dto.generator.service.VeiculoService;

public class TestingAPI {
	
	public static void main(String[] args) {
		
		//VeiculoService service = new VeiculoService();
		
		//Service service = ServiceFactory.getService(new VeiculoService());
		Service service = ServiceFactory.getService(VeiculoService.class);
		VeiculoDTO veiculo = service.recuperar();
		
		System.out.println("========DTO INICIO=======");
		
		VeiculoDTO dto = new VeiculoDTO();

		System.out.println("Before " + dto.getCliente());
		System.out.println("Before " + dto.getMotor());
		
		System.out.println("=======DTO FINAL========");
		
		ReflectUtil.buildDTO(veiculo, dto);
		
		System.out.println("Result " + dto.getCliente());
		System.out.println("Result " + dto.getMotor());
	}

}
