package kz.alfabank.alfaordersbpm.domain.service.pkb;


import kz.alfabank.alfaordersbpm.domain.models.pkb.PkbChecks;
import kz.alfabank.alfaordersbpm.domain.models.pkb.PkbData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PkbService {
    CompletableFuture<PkbData> getPkbData(Long orderId);

    CompletableFuture<List<PkbChecks>> getPkbChecks(Long orderId);

    String getPkbHtml(Long orderId);

    String getPkbResponse(Long orderId);
}
