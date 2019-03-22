package gamestore.service.implementations;

import gamestore.domain.dtos.GameAddDTO;
import gamestore.domain.dtos.GameEditDTO;
import gamestore.domain.entities.Game;
import gamestore.domain.entities.User;
import gamestore.repository.GameRepository;
import gamestore.repository.UserRepository;
import gamestore.service.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public String addGame(GameAddDTO gameAddDTO) {
        Validator validator = provideValidator();

        StringBuilder sb = new StringBuilder();

        if (validator.validate(gameAddDTO).size() > 0) {
            for (ConstraintViolation<GameAddDTO> violation : validator.validate(gameAddDTO)) {
                sb.append(violation.getMessage()).append(System.lineSeparator());
            }
        } else {
            Game entity = gameRepository.findByTitle(gameAddDTO.getTitle())
                    .orElse(null);

            if (entity != null) {
                return sb.append("Game already exists").toString();
            }

            entity = modelMapper.map(gameAddDTO, Game.class);
            gameRepository.saveAndFlush(entity);
            sb.append(String.format("Added %s", entity.getTitle()));
        }

        return sb.toString().trim();
    }

    @Override
    public String editGame(String[] tokens) {
        Validator validator = provideValidator();

        StringBuilder sb = new StringBuilder();
        long id = Long.parseLong(tokens[1]);

        Game game = this.gameRepository.findById(id).orElse(null);
        if (game == null) {
            return "Game not found!";
        }

        GameEditDTO entity = modelMapper.map(game, GameEditDTO.class);
        entity.setId(id);

        for (int i = 2; i < tokens.length; i++) {
            String[] propertiesElements = tokens[i].split("=");
            String propertyName = propertiesElements[0];
            String propertyKey = propertiesElements[1];
            switch (propertyName) {
                case "title":
                    return "Title cannot be edited!";
                case "price":
                    entity.setPrice(new BigDecimal(propertyKey));
                    break;
                case "size":
                    entity.setSize(new BigDecimal(propertyKey));
                    break;
                case "trailer":
                    entity.setTrailer(propertyKey);
                    break;
                case "thumbnailUrl":
                    entity.setImageThumbnail(propertyKey);
                    break;
                case "description":
                    entity.setDescription(propertyKey);
                    break;
            }
        }

        Set<ConstraintViolation<GameEditDTO>> violations = validator.validate(entity);

        if (violations.size() > 0) {
            for (ConstraintViolation<GameEditDTO> violation : violations) {
                sb.append(violation.getMessage()).append(System.lineSeparator());

            }

            return sb.toString().trim();
        } else {
            Game editedGame = this.modelMapper.map(entity, Game.class);
            if (editedGame != null) {
                Game savedGame = this.gameRepository.saveAndFlush(editedGame);
                if (savedGame != null) {
                    return String.format("Edited %s", savedGame.getTitle());
                }
            }
        }

        return null;
    }

    @Override
    public String deleteGame(int id) {
        Game game = gameRepository.findById((long) id)
                .orElse(null);

        String title = game.getTitle();

        if (game != null) {
            gameRepository.deleteById(id);

            return String.format("Deleted %s", title);
        }

        return null;
    }

    @Override
    public String getAllGame() {
        List<Game> games = gameRepository.getAllGames()
                .orElse(null);


        return String.join(System.lineSeparator(),
                games.stream()
                        .map(game -> String.format("%s %.2f",
                                game.getTitle(),
                                game.getPrice())
                        )
                        .collect(Collectors.toList())
        );
    }

    @Override
    public String getDetailGame(String title) {

        Game game = gameRepository.getByTitle(title)
                .orElse(null);

        return String.join(System.lineSeparator(),
                String.format(
                        "Title: %s\n" +
                                "Price: %.2f\n" +
                                "Description: %s\n",
                        game.getTitle(),
                        game.getPrice(),
                        game.getDescription())
        ).trim();
    }

    @Override
    public String getMyGames(String loggedInUser) {
        User user = userRepository.findByEmail(loggedInUser)
                .orElse(null);

        String userName = user.getFullName();

        List<Game> games = gameRepository.findOwnedGames(userName);

        return String.join(System.lineSeparator(),
                games.stream()
                        .map(g -> String.format("%s",
                                g.getTitle())
                        )
                        .collect(Collectors.toList())
        );
    }


    private Validator provideValidator() {
        return Validation
                .byDefaultProvider()
                .configure()
                .buildValidatorFactory()
                .getValidator();
    }

}
