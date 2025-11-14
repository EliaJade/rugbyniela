package rugbyniela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rugbyniela.entity.pojo.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
	public Address getByStreet();
}
