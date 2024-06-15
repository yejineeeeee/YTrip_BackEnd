package CodeIt.Ytrip.place.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Place {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;
    private String name;
    private String description;
    private String img;
    private float posX;
    private float posY;
}
