package gamestore.service;

import gamestore.domain.dtos.GameAddDTO;

public interface GameService {

    String addGame(GameAddDTO gameAddDTO);

    String editGame(String[] tokens);

    String deleteGame(int id);

    String getAllGame();

    String getDetailGame(String title);

    String getMyGames(String loggedInUser);
}
