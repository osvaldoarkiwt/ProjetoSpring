package br.com.iwt.pizzaria.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.iwt.pizzaria.api.assembler.ClienteAssembler;
import br.com.iwt.pizzaria.api.model.ClienteModel;
import br.com.iwt.pizzaria.api.model.input.ClienteInput;
import br.com.iwt.pizzaria.domain.exception.EntidadeEmUsoException;
import br.com.iwt.pizzaria.domain.exception.EntidadeNaoEncontradaException;
import br.com.iwt.pizzaria.domain.model.Cliente;
import br.com.iwt.pizzaria.domain.repository.ClienteRepository;

@Service
public class CadastroClienteService {

	private final String RECURSO_NOT_FOUND_ERRO = "não existe cadastro de cliente com o id ";
	
	@Autowired
	ClienteRepository repositorio;
	
	@Autowired
	ClienteAssembler assembler;
	
	public Page<ClienteModel> retornarClientes(Pageable pageable){
		
		List<ClienteModel> clientes = assembler.toCollectionModel(repositorio.findAll(pageable).getContent());
		
		Page<ClienteModel> clientePageModel = new PageImpl<>(clientes,pageable, clientes.size());
		
		return clientePageModel;
	}
	
	public Optional<Cliente> retornarCliente(UUID id) {
		return repositorio.findById(id);
	}
	
	public ClienteModel salvar(ClienteInput clienteInput) {
		
		Cliente clienteResposta = assembler.toEntity(clienteInput);
		
		Cliente cliente = repositorio.save(clienteResposta);
		
		return assembler.toModel(cliente);		
	}

	public Cliente listarOrThrow(UUID clienteId) {
		return repositorio.findById(clienteId).orElseThrow(()-> new EntidadeNaoEncontradaException(String.format(RECURSO_NOT_FOUND_ERRO+" "+clienteId)));
	}
	
	public void deletar(UUID clienteId) {
		
		Cliente resposta = repositorio.findById(clienteId).orElseThrow(()-> new EntidadeNaoEncontradaException(String.format(RECURSO_NOT_FOUND_ERRO+" "+clienteId)));
		
		try {
			
			repositorio.deleteById(clienteId);
			
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format("Cozinha de código %d não pode ser removida, pois está em uso", clienteId));
		}
	}
	
}
