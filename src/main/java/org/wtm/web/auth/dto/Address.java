package org.wtm.web.auth.dto;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Address {

    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;

}