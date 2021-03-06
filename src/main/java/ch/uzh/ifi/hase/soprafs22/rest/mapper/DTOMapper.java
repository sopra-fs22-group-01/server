package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.game.Lobby;
import ch.uzh.ifi.hase.soprafs22.game.Match;
import ch.uzh.ifi.hase.soprafs22.game.card.WhiteCard;
import ch.uzh.ifi.hase.soprafs22.game.helpers.Ranking;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {//TEST

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);


    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password") //how can the password still be saved in the database if it doesn't get mapped here??
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO); //takes userPostDTO and converts it to entity

    @Mapping(source = "score", target = "score")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "creation_date", target = "date")
    @Mapping(source = "userStatus", target = "userStatus")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "isReady", target = "isReady")
    @Mapping(source = "customWhiteText", target = "customWhiteText")
    @Mapping(source = "superVote", target = "superVote")
    @Mapping(source = "overallWins", target = "overallWins")
    @Mapping(source = "playedGames", target = "playedGames")
    UserGetDTO convertEntityToUserGetDTO(User user);

    // takes User as input and converts it to entity of UserGetDTO
    //does this method map all the @mapping stuff from above?


    @Mapping(source = "password", target = "password")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "userStatus", target = "userStatus")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "isReady", target = "isReady")
    @Mapping(source = "customWhiteText", target = "customWhiteText")
    @Mapping(source = "superVote", target = "superVote")
    UserPutDTO convertEntityToUserPutDTO(User user);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "owner", target = "owner")
    @Mapping(source = "score", target = "score")
    @Mapping(source = "text", target = "text")
    WhiteCardGetDTO convertEntityToWhiteCardGetDTO(WhiteCard whiteCard);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "owner", target = "owner")
    @Mapping(source = "score", target = "score")
    @Mapping(source = "text", target = "text")
    WhiteCard convertWhiteCardPutDTOToEntity(WhiteCardPutDTO whiteCardPutDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "currentPlayerCount", target = "currentPlayerCount")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

    @Mapping(source = "rank", target = "rank")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "score", target = "score")
    RankingGetDTO convertEntityToRankingGetDTO(Ranking ranking);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "matchStatus", target = "matchStatus")
    @Mapping(source = "matchPlayers", target = "matchPlayers")
    @Mapping(source = "laughStatus", target = "laughStatus")
    @Mapping(source = "available_Supervotes", target = "available_Supervotes")
    MatchGetDTO convertEntityToMatchGetDTO(Match match);
}