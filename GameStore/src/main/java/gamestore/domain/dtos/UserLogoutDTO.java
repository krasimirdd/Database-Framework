package gamestore.domain.dtos;

public class UserLogoutDTO {
    private String email;

    public UserLogoutDTO() {
    }

    public UserLogoutDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
