package org.jliv3.customer.order_manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Integer id;
    private String code;
    private String name;
    private String note;
    private MultipartFile[] files;

    public OrderDTO() {
        this.code = "";
        this.name = "";
        this.note = "";
        this.files = new MultipartFile[]{};
    }
}
