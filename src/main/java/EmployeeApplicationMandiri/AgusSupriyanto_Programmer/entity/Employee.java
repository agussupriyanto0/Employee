package EmployeeApplicationMandiri.AgusSupriyanto_Programmer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Employee {

    @Id
    private String id;

    private String name;

    private String email;

    private String position;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist(){

        if (this.id == null){
            this.id = UUID.randomUUID().toString();
        }

        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedDate =LocalDateTime.now();
    }

}
