package com.senai.jonatas.comprebem.service;

import com.senai.jonatas.comprebem.dto.ProdutoRequestDTO;
import com.senai.jonatas.comprebem.dto.ProdutoResponseDTO;
import com.senai.jonatas.comprebem.entity.Produto;
import com.senai.jonatas.comprebem.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 50;

    private final ProdutoRepository produtoRepository;

    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> findAll(String busca, Pageable pageable) {
        return findByFiltro(busca, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> findByFiltro(String filtro, Pageable pageable) {
        Pageable pageableSeguro = limitarPageable(pageable);

        Page<Produto> paginaProdutos;
        if (filtro == null || filtro.isBlank()) {
            paginaProdutos = produtoRepository.findAll(pageableSeguro);
        } else {
            paginaProdutos = produtoRepository.findByNomeContainingIgnoreCase(filtro.trim(), pageableSeguro);
        }

        return paginaProdutos.map(ProdutoResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO findById(UUID id) {
        Produto produto = buscarEntidadePorIdOu404(id);
        return ProdutoResponseDTO.fromEntity(produto);
    }

    @Transactional
    public ProdutoResponseDTO save(ProdutoRequestDTO dto) {
        Produto salvo = produtoRepository.save(dto.toEntity());
        return ProdutoResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public ProdutoResponseDTO update(UUID produtoId, ProdutoRequestDTO dto) {
        Produto existente = buscarEntidadePorIdOu404(produtoId);
        existente.setNome(dto.nome().trim());
        existente.setPreco(dto.preco());
        existente.setDescricao(dto.descricao().trim());
        if (dto.estoque() != null) {
            existente.setEstoque(dto.estoque());
        }
        Produto atualizado = produtoRepository.save(existente);
        return ProdutoResponseDTO.fromEntity(atualizado);
    }

    @Transactional
    public void delete(UUID produtoId) {
        Produto existente = buscarEntidadePorIdOu404(produtoId);
        produtoRepository.delete(existente);
    }

    private Pageable limitarPageable(Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            return PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
        }
        int paginaSegura = Math.max(DEFAULT_PAGE, pageable.getPageNumber());
        int tamanhoSolicitado = pageable.getPageSize() <= 0 ? DEFAULT_SIZE : pageable.getPageSize();
        int tamanhoSeguro = Math.min(tamanhoSolicitado, MAX_SIZE);

        return PageRequest.of(paginaSegura, tamanhoSeguro, pageable.getSort());
    }

    private Produto buscarEntidadePorIdOu404(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado para o ID: " + id
                ));
    }
}

