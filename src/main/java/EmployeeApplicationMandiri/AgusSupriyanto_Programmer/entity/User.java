package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role{
        USER,ADMIN
    };

    @PrePersist
    public void prePersist (){
        this.id = UUID.randomUUID().toString();
    }
}
