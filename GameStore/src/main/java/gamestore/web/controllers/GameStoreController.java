package gamestore.web.controllers;

import gamestore.domain.dtos.GameAddDTO;
import gamestore.domain.dtos.UserLoginDTO;
import gamestore.domain.dtos.UserLogoutDTO;
import gamestore.domain.dtos.UserRegisterDTO;
import gamestore.service.GameService;
import gamestore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Scanner;

@Controller
public class GameStoreController implements CommandLineRunner {

    private final UserService userService;
    private final GameService gameService;
    private String loggedInUser;

    @Autowired
    public GameStoreController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String inputLine = scanner.nextLine();

            String[] params = inputLine.split("\\|");


            switch (params[0]) {
                case "RegisterUser":
                    UserRegisterDTO userRegisterDTO =
                            new UserRegisterDTO(params[1], params[2], params[3], params[4]);

                    System.out.println(userService.registerUser(userRegisterDTO));

                    break;
                case "LoginUser":
                    if (loggedInUser == null) {
                        UserLoginDTO userLoginDTO =
                                new UserLoginDTO(params[1], params[2]);

                        String loginResult = userService.loginUser(userLoginDTO);

                        if (loginResult.contains("Successfully")) {
                            loggedInUser = userLoginDTO.getEmail();
                        }

                        System.out.println(loginResult);
                    } else {
                        System.out.println("Session is taken");
                    }

                    break;
                case "Logout":
                    if (loggedInUser == null) {
                        System.out.println("Cannot log out. No user was logged in.");
                    } else {
                        String logoutResult = userService
                                .logoutUser(new UserLogoutDTO(loggedInUser));

                        System.out.println(logoutResult);

                        loggedInUser = null;
                    }

                    break;
                case "AddGame":
                    if (loggedInUser != null && userService.isAdmin(loggedInUser)) {
                        GameAddDTO gameAddDTO =
                                new GameAddDTO(
                                        params[1],
                                        BigDecimal.valueOf(Double.parseDouble(params[2])),
                                        BigDecimal.valueOf(Double.parseDouble(params[3])),
                                        params[4],
                                        params[5],
                                        params[6]
                                );

                        System.out.println(gameService.addGame(gameAddDTO));
                    } else {
                        System.out.println("Cannot add game. No user was logged in.");
                    }

                    break;
                case "EditGame":
                    if (loggedInUser != null && userService.isAdmin(loggedInUser)) {
                        System.out.println(gameService.editGame(params));
                    } else {
                        System.out.println("Cannot add game. No user was logged in.");
                    }

                    break;
                case "DeleteGame":
                    if (loggedInUser != null && userService.isAdmin(loggedInUser)) {
                        System.out.println(gameService.deleteGame(Integer.parseInt(params[1])));
                    } else {
                        System.out.println("Cannot delete game. No user was logged in.");
                    }

                    break;
                case "AllGame":
                    System.out.println(gameService.getAllGame());

                    break;
                case "DetailGame":
                    System.out.println(gameService.getDetailGame(params[1]));

                    break;
                case "OwnedGame":
                    if (loggedInUser != null) {
                        System.out.println(gameService.getMyGames(loggedInUser));
                    } else {
                        System.out.println("Cannot delete game. No user was logged in.");
                    }
            }
        }
    }
}
