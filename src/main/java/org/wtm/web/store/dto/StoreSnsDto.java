package org.wtm.web.store.dto;


import lombok.Data;
import org.wtm.web.store.model.StoreSns;

@Data
public class StoreSnsDto {

    private String type;
    private String url;

    public StoreSnsDto(StoreSns storeSns) {
        this.type = storeSns.getType();
        this.url = storeSns.getUrl();
    }
}
