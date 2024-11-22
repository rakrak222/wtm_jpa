package org.wtm.web.auth.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;


    public String fullAddress() {
        return String.format("%s",
                address != null ? address : "No Address");
    }

}