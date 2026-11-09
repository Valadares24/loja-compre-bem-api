package com.senai.jonatas.comprebem.config;

import com.senai.jonatas.comprebem.entity.Produto;
import com.senai.jonatas.comprebem.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final int TOTAL_PRODUTOS_INICIAIS = 60;

    private final ProdutoRepository produtoRepository;

    @Override
    public void run(String... args) {
        if (produtoRepository.count() >= TOTAL_PRODUTOS_INICIAIS) {
            return;
        }

        String[][] categorias = {
                {"Cadeira", "Cadeira ergonômica para escritório com ajuste de altura"},
                {"Mesa", "Mesa reforçada para escritório e home office"},
                {"Lâmpada", "Lâmpada de mesa com iluminação LED econômica"},
                {"Monitor", "Monitor Full HD IPS com bordas ultrafinas"},
                {"Teclado", "Teclado mecânico silencioso ABNT2"},
                {"Mouse", "Mouse óptico sem fio de alta precisão"},
                {"Suporte", "Suporte articulado ergonômico para notebook"},
                {"Estante", "Estante organizadora multiuso em aço e MDF"},
                {"Gaveteiro", "Gaveteiro volante com rodízios e chave"},
                {"Headset", "Headset estéreo com microfone e cancelamento de ruído"}
        };

        List<Produto> iniciais = new ArrayList<>();

        iniciais.add(Produto.builder()
                .nome("Cadeira Maneira")
                .preco(new BigDecimal("10.00"))
                .descricao("Cadeira ergonômica para escritório")
                .estoque(15)
                .build());

        iniciais.add(Produto.builder()
                .nome("Mesa bacana 2")
                .preco(new BigDecimal("20.00"))
                .descricao("Mesa para escritório")
                .estoque(10)
                .build());

        iniciais.add(Produto.builder()
                .nome("Lampada show 3")
                .preco(new BigDecimal("30.00"))
                .descricao("Lâmpada de mesa com iluminação LED")
                .estoque(25)
                .build());

        for (int i = 4; i <= TOTAL_PRODUTOS_INICIAIS; i++) {
            String[] cat = categorias[(i - 1) % categorias.length];
            BigDecimal preco = BigDecimal.valueOf(25 + ((i * 17) % 450) + 0.90)
                    .setScale(2, RoundingMode.HALF_UP);

            iniciais.add(Produto.builder()
                    .nome(cat[0] + " Compre Bem #" + i)
                    .preco(preco)
                    .descricao(cat[1] + " - Modelo " + i)
                    .estoque(5 + (i % 30))
                    .build());
        }

        produtoRepository.saveAll(iniciais);
    }
}

