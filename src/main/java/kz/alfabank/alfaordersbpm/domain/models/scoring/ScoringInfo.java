package kz.alfabank.alfaordersbpm.domain.models.scoring;

import java.time.LocalDateTime;
import java.util.List;

public interface ScoringInfo {

    Long getId();

    Long getOrderId();

    String getScoringType();

    LocalDateTime getDateInsert();

    Boolean getActive();

    String getStatus();

    String getDecisionText();

    String getDecisionCategory();

    String getDecisionSetterId();

    String getDecisionSetterName();

    List<ScoringRule> getScoringRules();

}
