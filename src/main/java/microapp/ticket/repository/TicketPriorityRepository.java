package microapp.ticket.repository;

import microapp.ticket.domain.TicketPriority;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TicketPriority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketPriorityRepository extends ReactiveCrudRepository<TicketPriority, Long>, TicketPriorityRepositoryInternal {
    @Override
    <S extends TicketPriority> Mono<S> save(S entity);

    @Override
    Flux<TicketPriority> findAll();

    @Override
    Mono<TicketPriority> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TicketPriorityRepositoryInternal {
    <S extends TicketPriority> Mono<S> save(S entity);

    Flux<TicketPriority> findAllBy(Pageable pageable);

    Flux<TicketPriority> findAll();

    Mono<TicketPriority> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TicketPriority> findAllBy(Pageable pageable, Criteria criteria);

}
