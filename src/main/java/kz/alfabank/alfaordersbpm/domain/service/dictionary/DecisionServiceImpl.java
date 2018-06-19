package kz.alfabank.alfaordersbpm.domain.service.dictionary;

import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceDecision;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceDecisionType;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.mappers.OrderMapper;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWeb;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWebResponse;
import kz.alfabank.alfaordersbpm.domain.repositories.dictionaries.ServiceDecisionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(timeout = 30, isolation = Isolation.READ_COMMITTED)
public class DecisionServiceImpl implements DecisionService{

    private static final Logger LOG = LoggerFactory.getLogger(DecisionServiceImpl.class);

    private final ServiceDecisionRepository serviceDecisionRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public DecisionServiceImpl(ServiceDecisionRepository serviceDecisionRepository, OrderMapper orderMapper) {
        this.serviceDecisionRepository = serviceDecisionRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(readOnly = true,timeout = 20)
    public DictValWebResponse getServiceDecisionByType(ServiceDecisionType serviceDecisionType) {
        LOG.debug("DecisionService getServiceDecisionByType serviceDecisionType={}", serviceDecisionType);
        if (serviceDecisionType == null)
            throw new IllegalArgumentException("DecisionService->getServiceDecisionByType serviceDecisionType is null");
        List<ServiceDecision> decisions = serviceDecisionRepository.findByDecisionType(serviceDecisionType);
        List<DictValWeb> dictValWebs = orderMapper.getDictValsFromServiceDecision(decisions);
        DictValWebResponse result = new DictValWebResponse();
        result.setRetCode(1);
        result.setValWebs(dictValWebs);
        return result;
    }

    @Override
    @Transactional(readOnly = true,timeout = 10)
    public Optional<ServiceDecision> getOne(Long id) {
        LOG.debug("DecisionService getOne ID={}", id);
        return serviceDecisionRepository.findById(id);
    }

    @Override
    public ServiceDecision save(ServiceDecision entity) {
        LOG.debug("save ServiceDecision={}", entity);
        return serviceDecisionRepository.save(entity);
    }

    @Override
    public ServiceDecision update(ServiceDecision entity) {
        LOG.debug("update ServiceDecision={}", entity);
        Long id = entity.getId();
        if(id == null)
            throw new BadRequestException(String.format("To update, ServiceDecision must have an ID, ServiceDecision=%s", entity.toString()));
        if(!existsById(id))
            throw new EntityNotFoundException(String.format("ServiceDecision with ID=%s not found to perform update, ServiceDecision=%s", id, entity.toString()));
        return save(entity);
    }

    @Override
    public ServiceDecision insert(ServiceDecision entity) {
        LOG.debug("insert ServiceDecision={}", entity);
        Long id = entity.getId();
        if(id != null)
            throw new BadRequestException(String.format("To perform insert, ServiceDecision ID must be null, passed ServiceDecision=%s", entity.toString()));
        return save(entity);
    }

    @Override
    @Transactional(readOnly = true,timeout = 15)
    public boolean existsById(Long id) {
        LOG.debug("ServiceDecision existsById ID={}", id);
        return serviceDecisionRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("ServiceDecision delete by ID={}", id);
        serviceDecisionRepository.deleteById(id);
    }
}
