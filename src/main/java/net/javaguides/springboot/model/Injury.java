package net.javaguides.springboot.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Injury implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String injurySerialNo;
    private String typeOfInjury;
    private String siteOfInjury;
    private String sizeOfInjury;
    private String ageOfInjury;
    private String natureOfWeapon;
    private String remarks;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeOfInjury() {
        return typeOfInjury;
    }

    public void setTypeOfInjury(String typeOfInjury) {
        this.typeOfInjury = typeOfInjury;
    }

    public String getSiteOfInjury() {
        return siteOfInjury;
    }

    public void setSiteOfInjury(String siteOfInjury) {
        this.siteOfInjury = siteOfInjury;
    }

    public String getSizeOfInjury() {
        return sizeOfInjury;
    }

    public void setSizeOfInjury(String sizeOfInjury) {
        this.sizeOfInjury = sizeOfInjury;
    }

    public String getAgeOfInjury() {
        return ageOfInjury;
    }

    public void setAgeOfInjury(String ageOfInjury) {
        this.ageOfInjury = ageOfInjury;
    }

    public String getNatureOfWeapon() {
        return natureOfWeapon;
    }

    public void setNatureOfWeapon(String natureOfWeapon) {
        this.natureOfWeapon = natureOfWeapon;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
