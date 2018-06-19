package kz.alfabank.alfaordersbpm.domain.service.identitydocument;

import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.Task;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.TaskResult;
import kz.alfabank.alfaordersbpm.domain.models.identitydocument.IdentityDocument;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.repositories.IdentityDocumentRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter.BpmAdapterProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(timeout = 45, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class IdentityIdentityDocumentServiceImp implements IdentityDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityIdentityDocumentServiceImp.class);

    private final IdentityDocumentRepository identityDocumentRepository;
    private final RetailOrderRepository orderRepository;
    private final BpmAdapterProxy bpmAdapterProxy;

    @Autowired
    public IdentityIdentityDocumentServiceImp(IdentityDocumentRepository identityDocumentRepository, RetailOrderRepository orderRepository, BpmAdapterProxy bpmAdapterProxy) {
        this.identityDocumentRepository = identityDocumentRepository;
        this.orderRepository = orderRepository;
        this.bpmAdapterProxy = bpmAdapterProxy;
    }

    @Override
    @Transactional(readOnly = true,timeout = 20)
    public List<IdentityDocument> getByOrderId(Long orderId) {
        LOG.debug("IdentityDocumentService getByOrderId={}", orderId);
        if (orderId == null)
            throw new IllegalArgumentException("IdentityDocumentService getByOrderId orderId is null");
        return identityDocumentRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional(readOnly = true,timeout = 10)
    public Optional<IdentityDocument> getOne(Long id) {
        LOG.debug("IdentityDocumentService getOne ID={}", id);
        return identityDocumentRepository.findById(id);
    }

    @Override
    public IdentityDocument save(IdentityDocument entity) {
        LOG.debug("save IdentityDocument={}", entity);
        return identityDocumentRepository.saveAndFlush(entity);
    }

    @Override
    public IdentityDocument update(IdentityDocument entity) {
        LOG.debug("update IdentityDocument={}", entity);
        Long id = entity.getId();
        if(id == null)
            throw new BadRequestException(String.format("To update, IdentityDocument must have an ID, IdentityDocument=%s", entity.toString()));
        if(!existsById(id))
            throw new EntityNotFoundException(String.format("IdentityDocument with ID=%s not found to perform update, IdentityDocument=%s", id, entity.toString()));
        return save(entity);
    }

    @Override
    public IdentityDocument insert(IdentityDocument entity) {
        LOG.debug("insert IdentityDocument={}", entity);
        Long id = entity.getId();
        if(id != null)
            throw new BadRequestException(String.format("To perform insert, IdentityDocument ID must be null, passed IdentityDocument=%s", entity.toString()));
        String processId = orderRepository.getOrderProcessId(entity.getOrderId());
        Task task = bpmAdapterProxy.getTaskOrElseThrow(processId, RetailOrderUiStep.PASSPORT.getStepName());
        IdentityDocument result = save(entity);
        orderRepository.setProcessingStepUI(entity.getOrderId(), SecurityContextHolder.getContext().getAuthentication().getName());
        bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task));
        return result;
    }

    @Override
    @Transactional(readOnly = true,timeout = 15)
    public boolean existsById(Long id) {
        LOG.debug("IdentityDocument existsById ID={}", id);
        return identityDocumentRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("IdentityDocument delete by ID={}", id);
        identityDocumentRepository.deleteById(id);
    }

}
