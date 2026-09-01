package ifrn.pi.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ifrn.pi.eventos.models.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
}