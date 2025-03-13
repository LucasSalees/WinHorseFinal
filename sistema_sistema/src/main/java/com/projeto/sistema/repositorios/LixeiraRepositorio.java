package com.projeto.sistema.repositorios;
import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistema.modelos.Lixeira;

public interface LixeiraRepositorio extends JpaRepository<Lixeira, Long> {
    // Aqui você pode adicionar métodos específicos, se necessário
}
