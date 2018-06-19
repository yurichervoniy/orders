package kz.alfabank.alfaordersbpm.domain.repositories;

import kz.alfabank.alfaordersbpm.domain.models.order.OrderPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(timeout = 30, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public interface OrderPermissionRepository extends JpaRepository<OrderPermission, Long> {

    @Procedure(name = "addOrderPermission")
    @Transactional(timeout = 15)
    void addOrderPermission(@Param("p_order_id") Long orderId, @Param("p_permission") String permission, @Param("p_grant_to") String grantTo);


}
