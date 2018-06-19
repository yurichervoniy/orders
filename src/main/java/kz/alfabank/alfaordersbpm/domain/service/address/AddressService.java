package kz.alfabank.alfaordersbpm.domain.service.address;

import kz.alfabank.alfaordersbpm.domain.models.address.Address;
import kz.alfabank.alfaordersbpm.domain.models.address.AddressType;
import kz.alfabank.alfaordersbpm.domain.models.dto.AddressDTO;
import kz.alfabank.alfaordersbpm.domain.models.dto.PersonDetailsDTO;

import java.util.List;
import java.util.Optional;

public interface AddressService {

    List<Address> getByOrderId(Long orderId);

    Optional<Address> getByOrdeIdAndAddresType(Long orderId, AddressType addressType);

    Optional<Address> getOne(Long id);

    Address save(Address entity);

    Address update(Address entity);

    Address insert(Address entity);

    Address insertFromAddressDTO(AddressDTO addressDTO);

    Address fromPersonDetails(Long orderId,PersonDetailsDTO personDetailsDTO);

    boolean existsById(Long id);

    void delete(Long id);

}
