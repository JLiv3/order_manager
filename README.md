[![Build Status](https://travis-ci.com/JLiv3/order_manager.svg?branch=master)](https://travis-ci.com/JLiv3/order_manager)
![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/mrdonly93/order_manager)
[![codecov](https://codecov.io/gh/JLiv3/order_manager/branch/master/graph/badge.svg)](https://codecov.io/gh/JLiv3/order_manager)
![GitHub](https://img.shields.io/github/license/jliv3/order_manager)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=JLiv3_order_manager&metric=alert_status)](https://sonarcloud.io/dashboard?id=JLiv3_order_manager)
# order_manager
Nguyễn Hùng - Quản lý đơn hàng (Spring Boot, Security, Data Jpa, Thymeleaf, AngularJS, MySQL in Production / H2 memory in Deverlopment )

1, Live demo: [Click vào đây](https://order-manag3r.herokuapp.com/).  
- Tài khoản admin defaul : admin/admin.  

2, Download release tại [đây](https://github.com/JLiv3/order_manager/releases), chạy localhost bằng command: 
```cmd
java -jar <your path of file build .jar>
```
Ví dụ: 
```cmd
java -jar %USERPROFILE%\order_manager-1.0.3.jar
```
Yêu cầu: Jre or Jdk8.

3, Tính năng: 
- Có phân quyền 2 role: admin ( full quyền ) ; user. 
- Responsive trên cả PC desktop lẫn mobile. 
- Order page: Thông tin về các đơn hàng như mã đơn; số lượng kiểm đếm, hình ảnh; người tạo đơn, ngày tạo đơn; tạo mới / sửa / xóa đơn; hình ảnh có action view lớn riêng theo carousel; paging; phân quyền admin sẽ đc duyệt đơn / view all các đơn / sửa đơn / xóa đơn; user chỉ đc view các đơn của chính user đó tạo / chỉ được quyền sửa đơn. |
- User page: Chỉ admin mới có quyền truy cập page; tạo mới / sửa / xóa tài khoản. 
- Multi lang: English / VN. 
- Apply CI/CD: sử dụng travis-ci kết hợp với docker; demo deloy trên free host heroku; codecov ; sonarcloud.

