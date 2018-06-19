package kz.alfabank.alfaordersbpm.domain.service.contactperson;

import kz.alfabank.alfaordersbpm.domain.models.contactperson.ContactPerson;
import kz.alfabank.alfaordersbpm.domain.repositories.ContactPersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(timeout = 30, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class ContactPersonServiceImpl implements ContactPersonService {

    private static final Logger LOG = LoggerFactory.getLogger(ContactPersonServiceImpl.class);

    private final ContactPersonRepository contactPersonRepository;

    @Autowired
    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository) {
        this.contactPersonRepository = contactPersonRepository;
    }

    @Override
    @Transactional(timeout = 15, readOnly = true)
    public Optional<ContactPerson> getOne(Long id) {
        LOG.debug("ContactPersonService getOne id={}", id);
        return contactPersonRepository.findById(id);
    }

    @Override
    public ContactPerson save(ContactPerson entity) {
        LOG.debug("ContactPersonService save Entity={}", entity);
        return contactPersonRepository.saveAndFlush(entity);
    }

    @Override
    public List<ContactPerson> saveAll(Iterable<ContactPerson> iterable) {
        LOG.debug("ContactPersonService saveAll Iterable={}", iterable);
        List<ContactPerson> result = contactPersonRepository.saveAll(iterable);
        contactPersonRepository.flush();
        return result;
    }

    @Override
    @Transactional(timeout = 15, readOnly = true)
    public boolean existsById(Long id) {
        LOG.debug("ContactPersonService existsById id={}", id);
        return contactPersonRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("ContactPersonService delete id={}", id);
        contactPersonRepository.deleteById(id);
    }

    @Override
    @Transactional(timeout = 15, readOnly = true)
    public List<ContactPerson> getByOrderId(Long orderId) {
        LOG.debug("ContactPersonService getByOrderId orderid={}", orderId);
        return contactPersonRepository.findByOrderId(orderId);
    }
}
