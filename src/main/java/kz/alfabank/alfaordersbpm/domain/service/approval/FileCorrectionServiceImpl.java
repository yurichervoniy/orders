package kz.alfabank.alfaordersbpm.domain.service.approval;

import kz.alfabank.alfaordersbpm.domain.models.approval.FileCorrection;
import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.domain.models.exception.EntityNotFoundException;
import kz.alfabank.alfaordersbpm.domain.repositories.FileCorrectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(timeout = 30, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class FileCorrectionServiceImpl implements FileCorrectionService {
    private static final Logger LOG = LoggerFactory.getLogger(FileCorrectionServiceImpl.class);

    private FileCorrectionRepository fileCorrectionRepository;

    @Autowired
    public FileCorrectionServiceImpl(FileCorrectionRepository fileCorrectionRepository) {
        this.fileCorrectionRepository = fileCorrectionRepository;
    }

    @Override
    public Optional<FileCorrection> getOne(Long id) {
        LOG.debug("FileCorrectionService getOne ID={}", id);
        return fileCorrectionRepository.findById(id);
    }

    @Override
    public FileCorrection save(FileCorrection entity) {
        LOG.debug("save FileCorrection={}", entity);
        return fileCorrectionRepository.saveAndFlush(entity);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("delete FileCorrection by ID {}", id);
        fileCorrectionRepository.deleteById(id);
    }
}
