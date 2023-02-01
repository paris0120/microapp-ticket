package microapp.ticket.repository;

import microapp.ticket.domain.TicketAssignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TicketAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketAssignmentRepository extends ReactiveCrudRepository<TicketAssignment, Long>, TicketAssignmentRepositoryInternal {
    @Override
    <S extends TicketAssignment> Mono<S> save(S entity);

    @Override
    Flux<TicketAssignment> findAll();

    @Override
    Mono<TicketAssignment> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TicketAssignmentRepositoryInternal {
    <S extends TicketAssignment> Mono<S> save(S entity);

    Flux<TicketAssignment> findAllBy(Pageable pageable);

    Flux<TicketAssignment> findAll();

    Mono<TicketAssignment> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TicketAssignment> findAllBy(Pageable pageable, Criteria criteria);

}
