package kz.alfabank.alfaordersbpm.domain.service.dictionary;

import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceCheck;
import kz.alfabank.alfaordersbpm.domain.models.dictionary.ServiceCheckType;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.models.mappers.OrderMapper;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWeb;
import kz.alfabank.alfaordersbpm.domain.models.ocrmrefs.DictValWebResponse;
import kz.alfabank.alfaordersbpm.domain.repositories.dictionaries.ServiceCheckRepository;
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
public class CheckServiceImpl implements CheckService {

    private static final Logger LOG = LoggerFactory.getLogger(CheckServiceImpl.class);

    private final ServiceCheckRepository serviceCheckRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public CheckServiceImpl(ServiceCheckRepository serviceCheckRepository, OrderMapper orderMapper) {
        this.serviceCheckRepository = serviceCheckRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(readOnly = true,timeout = 20)
    public DictValWebResponse getServiceCheckByType(ServiceCheckType serviceCheckType) {
        LOG.debug("CheckService getServiceCheckByType serviceCheckType={}", serviceCheckType);
        if (serviceCheckType == null)
            throw new IllegalArgumentException("CheckService->getServiceCheckByType serviceCheckType is null");
        List<ServiceCheck> checks = serviceCheckRepository.findByCheckType(serviceCheckType);
        List<DictValWeb> dictValWebs = orderMapper.getDictValsFromServiceChecks(checks);
        DictValWebResponse result = new DictValWebResponse();
        result.setRetCode(1);
        result.setValWebs(dictValWebs);
        return result;
    }

    @Override
    @Transactional(readOnly = true,timeout = 10)
    public Optional<ServiceCheck> getOne(Long id) {
        LOG.debug("CheckService getOne ID={}", id);
        return serviceCheckRepository.findById(id);
    }

    @Override
    public ServiceCheck save(ServiceCheck entity) {
        LOG.debug("save ServiceCheck={}", entity);
        return serviceCheckRepository.save(entity);
    }

    @Override
    public ServiceCheck update(ServiceCheck entity) {
        LOG.debug("update ServiceCheck={}", entity);
        Long id = entity.getId();
        if(id == null)
            throw new BadRequestException(String.format("To update, ServiceCheck must have an ID, ServiceCheck=%s", entity.toString()));
        if(!existsById(id))
            throw new EntityNotFoundException(String.format("ServiceCheck with ID=%s not found to perform update, ServiceCheck=%s", id, entity.toString()));
        return save(entity);
    }

    @Override
    public ServiceCheck insert(ServiceCheck entity) {
        LOG.debug("insert ServiceCheck={}", entity);
        Long id = entity.getId();
        if(id != null)
            throw new BadRequestException(String.format("To perform insert, ServiceCheck ID must be null, passed ServiceCheck=%s", entity.toString()));
        return save(entity);
    }

    @Override
    @Transactional(readOnly = true,timeout = 15)
    public boolean existsById(Long id) {
        LOG.debug("ServiceCheck existsById ID={}", id);
        return serviceCheckRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("ServiceCheck delete by ID={}", id);
        serviceCheckRepository.deleteById(id);
    }
}
