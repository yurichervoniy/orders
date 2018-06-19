package kz.alfabank.alfaordersbpm.domain.service.signfinal;

import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.Task;
import kz.alfabank.alfaordersbpm.domain.models.bpm.adapter.TaskResult;
import kz.alfabank.alfaordersbpm.domain.models.dto.SignFinalDTO;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.mappers.OrderMapper;
import kz.alfabank.alfaordersbpm.domain.models.order.RetailOrderUiStep;
import kz.alfabank.alfaordersbpm.domain.models.signfinal.LoanAccount;
import kz.alfabank.alfaordersbpm.domain.models.signfinal.LoanAgreement;
import kz.alfabank.alfaordersbpm.domain.models.signfinal.SignFinal;
import kz.alfabank.alfaordersbpm.domain.repositories.LoanAccountRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.LoanAgreementRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.RetailOrderRepository;
import kz.alfabank.alfaordersbpm.domain.repositories.SignFinalRepository;
import kz.alfabank.alfaordersbpm.domain.service.bpmocrm.bpmadapter.BpmAdapterProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Transactional(timeout = 30, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class SignFinalServiceImpl implements SignFinalService{
    private static final Logger LOG = LoggerFactory.getLogger(SignFinalServiceImpl.class);

    private final SignFinalRepository signFinalRepository;
    private final BpmAdapterProxy bpmAdapterProxy;
    private final RetailOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final LoanAccountRepository loanAccountRepository;
    private final LoanAgreementRepository loanAgreementRepository;

    @Autowired
    private RetailOrderRepository retailOrderRepository;

    @Autowired
    public SignFinalServiceImpl(SignFinalRepository signFinalRepository, BpmAdapterProxy bpmAdapterProxy, RetailOrderRepository orderRepository, OrderMapper orderMapper, LoanAccountRepository loanAccountRepository, LoanAgreementRepository loanAgreementRepository) {
        this.signFinalRepository = signFinalRepository;
        this.bpmAdapterProxy = bpmAdapterProxy;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.loanAccountRepository = loanAccountRepository;
        this.loanAgreementRepository = loanAgreementRepository;
    }

    @Override
    public SignFinal getByOrderId(Long orderId) {
        LOG.debug("SignFinalService getByOrderId orderId={}", orderId);
        if (orderId == null)
            throw new IllegalArgumentException("SignFinalService->getByOrderId orderId is null");

        LoanAccount loanAccount = loanAccountRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Entity account not found with ID = %s",orderId.toString())));

        LoanAgreement loanAgreement = loanAgreementRepository.findByOrderId(orderId)
                .orElse(new LoanAgreement());

        SignFinal signFinal = signFinalRepository.findByOrderId(orderId)
                .orElse(new SignFinal());

        signFinal.setAccount(loanAccount.getAccount());
        signFinal.setCardFl(Long.parseLong(loanAccount.getCardFl()));
        signFinal.setStatus(loanAgreement.getStatus());

        return signFinal;
    }

    @Override
    public Optional<SignFinal> getOne(Long id) {
        LOG.debug("SignFinalService getOne ID={}", id);
        return signFinalRepository.findById(id);
    }

    @Override
    public SignFinal save(SignFinal entity) {
        LOG.debug("save SignFinal={}", entity);
        return signFinalRepository.saveAndFlush(entity);
    }

    @Override
    public SignFinal insert(SignFinal entity, RetailOrderUiStep step) {
        LOG.debug("insert SignFinal={}", entity);
        Long id = entity.getId();
        if(id != null)
            throw new BadRequestException(String.format("To perform insert, SignFinal ID must be null, passed SignFinal=%s", entity.toString()));
        String processId = orderRepository.getOrderProcessId(entity.getOrderId());
        Task task = bpmAdapterProxy.getTaskOrElseThrow(processId, step.getStepName());
        SignFinal result = save(entity);
        retailOrderRepository.setProcessingStepUI(entity.getOrderId(), SecurityContextHolder.getContext().getAuthentication().getName());

        if (StringUtils.isEmpty(entity.getCardIDN())) {
            bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task));
        } else {
            bpmAdapterProxy.completeTask(task, TaskResult.ofSuccess(task.getTkiid(), task.getName(), "APPROVE_CHK"));
        }
        return result;
    }

    @Override
    public boolean existsById(Long id) {
        LOG.debug("SignFinal existsById ID={}", id);
        return signFinalRepository.existsById(id);
    }

    @Override
    public SignFinal update(SignFinal entity) {
        LOG.debug("update SignFinal={}", entity);
        Long id = entity.getId();
        if(id == null)
            throw new BadRequestException(String.format("To update, SignFinal must have an ID, SignFinal=%s", entity.toString()));
        if(!existsById(id))
            throw new EntityNotFoundException(String.format("SignFinal with ID=%s not found to perform update, SignFinal=%s", id, entity.toString()));
        return save(entity);
    }

    @Override
    public SignFinal createLoanAgreement(SignFinalDTO signFinalDTO) {
        LOG.debug("Address insertFromSignFinalDTO {}", signFinalDTO);
        if (signFinalDTO == null)
            throw new IllegalArgumentException("SignFinalService->insertFromSignFinalDTO SignFinalDTO is null");

        SignFinal signFinal = orderMapper.fromSignFinalDTO(signFinalDTO);
        return insert(signFinal, RetailOrderUiStep.SIGN_FINAL_PRINT);
    }

    @Override
    public void saveSignFinal(Long orderId) {
        if(orderId == null)
            throw new BadRequestException("To complete the task orderId must be not null");

        String processId = orderRepository.getOrderProcessId(orderId);
        Task task = bpmAdapterProxy.getTaskOrElseThrow(processId, RetailOrderUiStep.SIGN_FINAL_DOCS.getStepName());
        retailOrderRepository.setProcessingStepUI(orderId, SecurityContextHolder.getContext().getAuthentication().getName());
        bpmAdapterProxy.completeTask(task, TaskResult.ofApprove(task));
    }
}
