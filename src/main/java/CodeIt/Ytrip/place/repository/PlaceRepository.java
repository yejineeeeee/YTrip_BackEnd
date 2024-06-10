package CodeIt.Ytrip.place.repository;

import CodeIt.Ytrip.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByPosXAndPosY(float posX, float posY);
}
