package kz.alfabank.alfaordersbpm.domain.service.additionalinfo;

import kz.alfabank.alfaordersbpm.domain.models.additionalinfo.AdditionalInfo;
import kz.alfabank.alfaordersbpm.domain.models.dto.AdditionalInfoDTO;

import java.util.Optional;

public interface AdditionalInfoService {

    Optional<AdditionalInfo> getByOrderId(Long orderId);

    Optional<AdditionalInfo> getOne(Long id);

    AdditionalInfo save(AdditionalInfo entity);

    AdditionalInfo fromAddInfoDTO(Long orderId, AdditionalInfoDTO infoDTO);

}
