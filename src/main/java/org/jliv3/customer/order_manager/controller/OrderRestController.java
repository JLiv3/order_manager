package org.jliv3.customer.order_manager.controller;

import org.apache.commons.io.IOUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.jliv3.customer.order_manager.entity.FileImg;
import org.jliv3.customer.order_manager.entity.Order;
import org.jliv3.customer.order_manager.entity.OrderDTO;
import org.jliv3.customer.order_manager.service.OrderService;
import org.jliv3.customer.order_manager.utils.MySimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {
    public static final String ORDER_ID_NOT_FOUND_MESSAGE = "Không tìm thấy đơn hàng: ";
    public static final String IMG_NOT_FOUND_MESSAGE = "Không tìm thấy hình ảnh đơn hàng.";
    public static final String NO_ACCESS_MODIFIED_IMG_MESSAGE = "Bạn không có quyền chỉnh sửa đơn hàng.";
    public static final String CURRENT_IMG_DIR = System.getProperty("user.dir") + "/storeImage";
    @Autowired
    private OrderService orderService;

    public static FileImg getImgInSet(Set<FileImg> source, String imgName) {
        for (FileImg f : source) {
            if (imgName.equals(f.getShortName())) {
                return f;
            }
        }
        return null;
    }

    private static void saveImg(MultipartFile[] sourceImg, Order saveIn, Path orderPath) throws IOException {
        for (MultipartFile f : sourceImg) {
            byte[] bytes = f.getBytes();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(orderPath + "/" + f.getOriginalFilename()));
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.close();
            saveIn.getListImg().add(FileImg.builder().fullName(orderPath + "/" + f.getOriginalFilename()).shortName(f.getOriginalFilename()).build());
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getALl(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
            return new ResponseEntity<>(orderService.findByCreateBy(authentication.getName()), HttpStatus.OK);
        }
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@ModelAttribute OrderDTO orderDTO) throws IOException, ConstraintViolationException {
        if (!Files.exists(Paths.get(CURRENT_IMG_DIR))) {
            Files.createDirectory(Paths.get(CURRENT_IMG_DIR));
        }
        if (orderDTO.getCode().isEmpty()) {
            orderDTO.setCode("unknow" + MySimpleDateFormat.get().format(new Date()));
        }
        Path orderPath = Paths.get(CURRENT_IMG_DIR + "/" + orderDTO.getCode());
        if (!Files.exists(orderPath)) {
            Files.createDirectory(orderPath);
        }
        Order order = new Order();
        order.setCode(orderDTO.getCode());
        order.setName(orderDTO.getName());
        order.setNote(orderDTO.getNote());
        saveImg(orderDTO.getFiles(), order, orderPath);
        return new ResponseEntity<>(orderService.save(order), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Order> updateOrder(@ModelAttribute OrderDTO orderDTO, Authentication authentication) throws IOException, ConstraintViolationException {
        if (orderDTO.getCode().isEmpty()) {
            orderDTO.setCode("unknow" + MySimpleDateFormat.get().format(new Date()));
        }
        Order order = orderService.findById(orderDTO.getId()).orElseThrow(() -> new ApiException(ORDER_ID_NOT_FOUND_MESSAGE + orderDTO.getId()));

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER")) && !order.getCreateBy().equals(authentication.getName())) {
            throw new ApiException(NO_ACCESS_MODIFIED_IMG_MESSAGE);
        }
        String newOrderPath = CURRENT_IMG_DIR + "/" + orderDTO.getCode();
        if (!Files.exists(Paths.get(newOrderPath))) {
            Files.createDirectory(Paths.get(newOrderPath));
        }
        if (!order.getCode().equals(orderDTO.getCode())) {
            order.setCode(orderDTO.getCode());
            Set<FileImg> fileImgs = order.getListImg();
            for (FileImg f : fileImgs) {
                Files.move(Paths.get(f.getFullName()), Paths.get(newOrderPath + "/" + f.getShortName()), REPLACE_EXISTING);
                Files.deleteIfExists(Paths.get(f.getFullName()));
                f.setFullName(newOrderPath + "/" + f.getShortName());
            }
        }
        order.setName(orderDTO.getName());
        order.setNote(orderDTO.getNote());
        saveImg(orderDTO.getFiles(), order, Paths.get(newOrderPath));
        return new ResponseEntity<>(orderService.save(order), HttpStatus.OK);
    }

    @GetMapping(value = "/image/{id}/{imgName}", produces = MediaType.IMAGE_JPEG_VALUE)
    @Cacheable("images")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id, @PathVariable String imgName) throws IOException {
        Order order = orderService.findById(id).orElseThrow(() -> new ApiException(ORDER_ID_NOT_FOUND_MESSAGE + id));
        FileImg img = getImgInSet(order.getListImg(), imgName);
        if (img != null) {
            InputStream in = new FileInputStream(img.getFullName());
            byte[] byteStream = IOUtils.toByteArray(in);
            in.close();
            return ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)).body(byteStream);
        } else throw new ApiException(IMG_NOT_FOUND_MESSAGE);
    }

    @PutMapping("/toggleChecked")
    public ResponseEntity<Order> toggleChecked(@RequestBody Order order) {
        Order orderOnDB = orderService.findById(order.getId()).orElseThrow(() -> new ApiException(ORDER_ID_NOT_FOUND_MESSAGE + order.getId()));
        orderOnDB.setChecked(!orderOnDB.isChecked());
        return new ResponseEntity<>(orderService.save(orderOnDB), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Integer orderId) {
        Order order = orderService.findById(orderId).orElseThrow(() -> new ApiException(ORDER_ID_NOT_FOUND_MESSAGE + orderId));
        orderService.delete(order);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
