package CodeIt.Ytrip.place.repository;

import CodeIt.Ytrip.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByPxAndPy(double posX, double posY);
    List<Place> findByIdIn(List<Long> ids);
}
