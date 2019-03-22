package gamestore.service.implementations;

import gamestore.domain.dtos.UserLoginDTO;
import gamestore.domain.dtos.UserLogoutDTO;
import gamestore.domain.dtos.UserRegisterDTO;
import gamestore.domain.entities.Role;
import gamestore.domain.entities.User;
import gamestore.repository.UserRepository;
import gamestore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public String registerUser(UserRegisterDTO userRegisterDTO) {
        Validator validator = provideValidator();

        StringBuilder sb = new StringBuilder();

        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            sb.append("Passwords doesn't match.");
        } else if (validator.validate(userRegisterDTO).size() > 0) {

            for (ConstraintViolation<UserRegisterDTO> violation : validator.validate(userRegisterDTO)) {
                sb.append(violation.getMessage()).append(System.lineSeparator());
            }
        } else {
            User entity = userRepository.findByEmail(userRegisterDTO.getEmail())
                    .orElse(null);

            if (entity != null) {
                return sb.append("User already exists").toString();
            }

            entity = modelMapper.map(userRegisterDTO, User.class);

            if (userRepository.count() == 0) {
                entity.setRole(Role.ADMIN);
            } else {
                entity.setRole(Role.USER);
            }

            userRepository.saveAndFlush(entity);
            sb.append(String.format("%s was registered", entity.getFullName()));
        }

        return sb.toString().trim();
    }

    @Override
    public String loginUser(UserLoginDTO userLoginDTO) {
        Validator validator = provideValidator();
        StringBuilder sb = new StringBuilder();

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(userLoginDTO);

        if (violations.size() > 0) {
            for (ConstraintViolation<UserLoginDTO> violation : violations) {
                sb.append(violation.getMessage()).append(System.lineSeparator());
            }
        } else {
            User entity = userRepository
                    .findByEmail(userLoginDTO.getEmail())
                    .orElse(null);

            if (entity == null) {
                return sb.append("User does not exists").append(System.lineSeparator()).toString();
            } else if (!entity.getPassword().equals(userLoginDTO.getPassword())) {
                return sb.append("Wrong password").toString();
            }

            sb.append(String
                    .format("Successfully logged in %s", entity.getFullName()))
                    .append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    @Override
    public String logoutUser(UserLogoutDTO userLogoutDTO) {

        User entity = userRepository.findByEmail(userLogoutDTO.getEmail())
                .orElse(null);
        StringBuilder sb = new StringBuilder();

        if (entity == null) {
            return sb.append("User does not exists.").append(System.lineSeparator()).toString();
        }

        sb.append(String
                .format("User %s successfully logged out",
                        entity.getFullName())
        );

        return sb.toString();
    }

    private Validator provideValidator() {
        return Validation
                .byDefaultProvider()
                .configure()
                .buildValidatorFactory()
                .getValidator();
    }

    @Override
    public boolean isAdmin(String email) {
        User entity = this.userRepository.findByEmail(email).orElse(null);

        if (entity != null) {
            return entity.getRole().equals(Role.ADMIN);
        }

        return false;
    }
}
