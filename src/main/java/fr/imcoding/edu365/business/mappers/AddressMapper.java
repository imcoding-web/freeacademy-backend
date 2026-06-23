package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.AddressDto;
import fr.imcoding.edu365.persistence.entities.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {

  private final CityMapper cityMapper;

  public Address toAddress(AddressDto addressRequest) {
    return addressRequest != null ? new Address(addressRequest.getAddressStreet(), addressRequest.getAddressStreetNumber(),
        addressRequest.getAddressPostalCode(),
        cityMapper.toCity(addressRequest.getAddressCity())) : null;
  }

  public AddressDto toAddressDto(Address address) {
    return  new AddressDto(address.getAddressStreet(), address.getAddressStreetNumber(),
        address.getAddressPostalCode(),
        cityMapper.toCityDto(address.getAddressCity()));
  }


}
