package net.javaguides.springboot.model;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Patient implements Serializable {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    private String dispatchId;
    private String generationDate;
    private String docket;
    private String docketDate;
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String age;
    private String sex;
    private String relation;
    private String gurdian;
    private String phoneNo;
    private String nid;
    private String village;
    private String post;
    private String thana;
    private String district;
    private String regNo;
    private String dateOfAdmission;
    private String timeOfAdmission;
    private String ward;


    @OneToMany(cascade= CascadeType.ALL)
    private List<Injury> injuries;

    private String note;

}