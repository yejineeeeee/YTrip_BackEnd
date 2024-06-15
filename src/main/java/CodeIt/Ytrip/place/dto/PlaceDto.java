package CodeIt.Ytrip.place.dto;

import CodeIt.Ytrip.place.domain.Place;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceDto {
    private int index;
    private String name;
    private String description;
    private String img;
    private float posX;
    private float posY;

    public static PlaceDto of(int index, Place place) {
        return PlaceDto.builder()
                .index(index)
                .name(place.getName())
                .description(place.getDescription())
                .img(place.getImg())
                .posX(place.getPosX())
                .posY(place.getPosY())
                .build();
    }
}
