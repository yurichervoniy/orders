package kz.alfabank.alfaordersbpm.domain.repositories.blacklist;


import kz.alfabank.alfaordersbpm.domain.models.blacklist.BLCheckResults;
import kz.alfabank.alfaordersbpm.domain.models.blacklist.BLRequestResponse;

import java.sql.SQLException;
import java.util.List;

public interface BlackListDAO {
    List<BLRequestResponse> getBLRqRsInfo(long id) throws SQLException;
    List<BLCheckResults> getResultsChecks(long id) throws SQLException;
}
