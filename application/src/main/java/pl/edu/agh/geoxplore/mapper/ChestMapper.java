package pl.edu.agh.geoxplore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.edu.agh.geoxplore.model.Chest;
import pl.edu.agh.geoxplore.rest.ChestResponse;

import java.sql.Timestamp;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChestMapper {
    @Mapping(source = "dateFound", target = "opened", qualifiedByName = "dateToBoolean")
    ChestResponse ChestToResponse(Chest chest);

    List<ChestResponse> ChestToResponse(List<Chest> chest);

    @Named("dateToBoolean")
    default Boolean timestampToBoolean(Timestamp obj) {
        return obj != null;
    }
}
