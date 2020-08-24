package org.jliv3.customer.order_manager.controller;

import org.apache.commons.io.IOUtils;
import org.jliv3.customer.order_manager.entity.FileImg;
import org.jliv3.customer.order_manager.entity.Order;
import org.jliv3.customer.order_manager.entity.OrderDTO;
import org.jliv3.customer.order_manager.repository.OrderRepository;
import org.jliv3.customer.order_manager.utils.MySimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {
    public static final String ORDER_ID_NOT_FOUND_MESSAGE = "Order not found: ";
    public static final String IMG_NOT_FOUND_MESSAGE = "Image not found.";
    public static final String CURRENT_IMG_DIR = System.getProperty("user.dir") + "\\storeImage";
    @Autowired
    private OrderRepository orderRepository;

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
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(orderPath + "\\" + f.getOriginalFilename()));
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.close();
            saveIn.getListImg().add(FileImg.builder().fullName(orderPath + "\\" + f.getOriginalFilename()).shortName(f.getOriginalFilename()).build());
        }
    }

    public static void main(String[] args) {
    }

    @GetMapping
    public ResponseEntity<List<Order>> getALl() {
        return new ResponseEntity<>(orderRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@ModelAttribute OrderDTO orderDTO) throws IOException {
        if (!Files.exists(Paths.get(CURRENT_IMG_DIR))) {
            Files.createDirectory(Paths.get(CURRENT_IMG_DIR));
        }
        if (orderDTO.getCode().isEmpty()) {
            orderDTO.setCode("unknow" + MySimpleDateFormat.get().format(new Date()));
        }
        Path orderPath = Paths.get(CURRENT_IMG_DIR + "\\" + orderDTO.getCode());
        if (!Files.exists(orderPath)) {
            Files.createDirectory(orderPath);
        }
        Order order = new Order();
        order.setCode(orderDTO.getCode());
        order.setName(orderDTO.getName());
        order.setNote(orderDTO.getNote());
        order.setChecked(orderDTO.isChecked());
        saveImg(orderDTO.getFiles(), order, orderPath);
        return new ResponseEntity<>(orderRepository.save(order), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Order> updateOrder(@ModelAttribute OrderDTO orderDTO) throws IOException {
        if (orderDTO.getCode().isEmpty()) {
            orderDTO.setCode("unknow" + MySimpleDateFormat.get().format(new Date()));
        }
        Order order = orderRepository.findById(orderDTO.getId()).orElseThrow(() -> new ApiException(ORDER_ID_NOT_FOUND_MESSAGE + orderDTO.getId()));
        String newOrderPath = CURRENT_IMG_DIR + "\\" + orderDTO.getCode();
        if (!Files.exists(Paths.get(newOrderPath))) {
            Files.createDirectory(Paths.get(newOrderPath));
        }
        if (!order.getCode().equals(orderDTO.getCode())) {
            String oldOrderPath = CURRENT_IMG_DIR + "\\" + order.getCode();
            order.setCode(orderDTO.getCode());
            Set<FileImg> fileImgs = order.getListImg();
            for (FileImg f : fileImgs) {
                Files.move(Paths.get(f.getFullName()), Paths.get(newOrderPath + "\\" + f.getShortName()), REPLACE_EXISTING);
                Files.deleteIfExists(Paths.get(f.getFullName()));
                f.setFullName(newOrderPath + "\\" + f.getShortName());
            }
            Files.deleteIfExists(Paths.get(oldOrderPath));
        }
        order.setName(orderDTO.getName());
        order.setNote(orderDTO.getNote());
        order.setChecked(orderDTO.isChecked());
        saveImg(orderDTO.getFiles(), order, Paths.get(newOrderPath));
        return new ResponseEntity<>(orderRepository.save(order), HttpStatus.OK);
    }

    @GetMapping(value = "/image/{id}/{imgName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@PathVariable Integer id, @PathVariable String imgName) throws IOException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ApiException(ORDER_ID_NOT_FOUND_MESSAGE + id));
        FileImg img = getImgInSet(order.getListImg(), imgName);
        if (img != null) {
            InputStream in = new FileInputStream(img.getFullName());
            return IOUtils.toByteArray(in);
        } else throw new ApiException(IMG_NOT_FOUND_MESSAGE);
    }
}
