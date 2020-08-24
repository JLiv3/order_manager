package org.jliv3.customer.order_manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadForm {
    private Integer id;
    private MultipartFile[] files;
}
