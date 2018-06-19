package kz.alfabank.alfaordersbpm.domain.service.pkb;

import kz.alfabank.alfaordersbpm.domain.models.pkb.PkbChecks;
import kz.alfabank.alfaordersbpm.domain.models.pkb.PkbData;
import kz.alfabank.alfaordersbpm.domain.repositories.pkb.PkbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PkbServiceImpl implements PkbService {
    private final PkbRepository pkbRepository;

    @Autowired
    public PkbServiceImpl(PkbRepository pkbRepository) {
        this.pkbRepository = pkbRepository;
    }

    @Override
    @Async
    public CompletableFuture<PkbData> getPkbData(Long orderId) {
        return CompletableFuture.completedFuture(pkbRepository.getPkbMainData(orderId));
    }

    @Override
    @Async
    public CompletableFuture<List<PkbChecks>> getPkbChecks(Long orderId) {
        return CompletableFuture.completedFuture(pkbRepository.getPkbChecksData(orderId));
    }

    @Override
    public String getPkbHtml(Long orderId) {
        return pkbRepository.getPkbHtml(orderId);
    }

    @Override
    public String getPkbResponse(Long orderId) {
        return pkbRepository.getPkbResponse(orderId);
    }
}
