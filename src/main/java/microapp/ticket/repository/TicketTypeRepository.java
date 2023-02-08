package microapp.ticket.repository;

import microapp.ticket.domain.TicketType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TicketType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketTypeRepository extends ReactiveCrudRepository<TicketType, Long>, TicketTypeRepositoryInternal {
    @Override
    <S extends TicketType> Mono<S> save(S entity);

    @Override
    Flux<TicketType> findAll();

    Flux<TicketType> findAllByOrderByWeightAsc();

    @Override
    Mono<TicketType> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TicketTypeRepositoryInternal {
    <S extends TicketType> Mono<S> save(S entity);

    Flux<TicketType> findAllBy(Pageable pageable);

    Flux<TicketType> findAll();

    Mono<TicketType> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TicketType> findAllBy(Pageable pageable, Criteria criteria);

}
